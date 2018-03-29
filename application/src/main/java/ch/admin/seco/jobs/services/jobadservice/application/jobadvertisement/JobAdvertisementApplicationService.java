package ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement;

import ch.admin.seco.jobs.services.jobadservice.application.LocationService;
import ch.admin.seco.jobs.services.jobadservice.application.ProfessionService;
import ch.admin.seco.jobs.services.jobadservice.application.ReportingObligationService;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.*;
import ch.admin.seco.jobs.services.jobadservice.core.conditions.Condition;
import ch.admin.seco.jobs.services.jobadservice.core.domain.AggregateNotFoundException;
import ch.admin.seco.jobs.services.jobadservice.core.time.TimeMachine;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.*;
import ch.admin.seco.jobs.services.jobadservice.domain.profession.Profession;
import ch.admin.seco.jobs.services.jobadservice.domain.profession.ProfessionCodeType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.*;

import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisementStatus.REFINING;
import static java.util.stream.Collectors.toList;

@Service
@Transactional(rollbackFor = {Exception.class})
public class JobAdvertisementApplicationService {

    public static final int X28_PUBLICATION_MAX_DAYS = 60;
    private static Logger LOG = LoggerFactory.getLogger(JobAdvertisementApplicationService.class);

    private final JobAdvertisementRepository jobAdvertisementRepository;

    private final JobAdvertisementFactory jobAdvertisementFactory;

    private final ReportingObligationService reportingObligationService;

    private final LocationService locationService;

    private final ProfessionService professionSerivce;

    @Autowired
    public JobAdvertisementApplicationService(JobAdvertisementRepository jobAdvertisementRepository,
                                              JobAdvertisementFactory jobAdvertisementFactory,

                                              ReportingObligationService reportingObligationService,
                                              LocationService locationService,
                                              ProfessionService professionService) {
        this.jobAdvertisementRepository = jobAdvertisementRepository;
        this.jobAdvertisementFactory = jobAdvertisementFactory;
        this.reportingObligationService = reportingObligationService;
        this.locationService = locationService;
        this.professionSerivce = professionService;
    }

    public JobAdvertisementId createFromWebForm(CreateJobAdvertisementWebFormDto createJobAdvertisementWebFormDto) {
        LOG.debug("Create '{}' from Webform", createJobAdvertisementWebFormDto.getTitle());

        Location location = toLocation(createJobAdvertisementWebFormDto.getLocation());
        location = locationService.enrichCodes(location);

        Occupation occupation = toOccupation(createJobAdvertisementWebFormDto.getOccupation());
        occupation = enrichOccupationWithProfessionCodes(occupation);

        boolean reportingObligation = checkReportingObligation(
                occupation,
                location
        );

        // TODO resolve jobCenterCode (see: JobPublicationServiceImpl.computeArbeitsamtbereich)
        String jobCenterCode = null;

        JobContent jobContent = new JobContent.Builder()
                .setJobDescriptions(Collections.singletonList(
                        new JobDescription.Builder()
                                .setLanguage(new Locale(createJobAdvertisementWebFormDto.getLanguageIsoCode()))
                                .setTitle(createJobAdvertisementWebFormDto.getTitle())
                                .setDescription(createJobAdvertisementWebFormDto.getDescription())
                                .build()
                ))
                .setLocation(location)
                .setOccupations(Collections.singletonList(occupation))
                .setEmployment(toEmployment(createJobAdvertisementWebFormDto.getEmployment()))
                .setApplyChannel(toApplyChannel(createJobAdvertisementWebFormDto.getApplyChannel()))
                .setCompany(toCompany(createJobAdvertisementWebFormDto.getCompany()))
                .setPublicContact(toPublicContact(createJobAdvertisementWebFormDto.getContact()))
                .setLanguageSkills(toLanguageSkills(createJobAdvertisementWebFormDto.getLanguageSkills()))
                .build();

        //TODO What should be the EURES value?
        Publication publication = new Publication.Builder()
                // TODO Add fields of publication
                .setEures(createJobAdvertisementWebFormDto.isEures())
                .build();

        final JobAdvertisementCreator creator = new JobAdvertisementCreator.Builder(null)
                .setReportingObligation(reportingObligation)
                .setJobCenterCode(jobCenterCode)
                .setJobContent(jobContent)
                .setContact(toContact(createJobAdvertisementWebFormDto.getContact()))
                .setPublication(publication)
                .build();

        JobAdvertisement jobAdvertisement = jobAdvertisementFactory.createFromWebForm(creator);
        return jobAdvertisement.getId();
    }

    public JobAdvertisementId createFromApi(CreateJobAdvertisementApiDto createJobAdvertisementApiDto) {
        LOG.debug("Create '{}' from API", createJobAdvertisementApiDto.getJob().getTitle());

        Location location = toLocation(createJobAdvertisementApiDto.getJob().getLocation());
        location = locationService.enrichCodes(location);

        Occupation occupation = toOccupation(createJobAdvertisementApiDto.getOccupation());
        occupation = enrichOccupationWithProfessionCodes(occupation);

        boolean reportingObligation = checkReportingObligation(
                occupation,
                location
        );

        // TODO resolve jobCenterCode (see: JobPublicationServiceImpl.computeArbeitsamtbereich)
        String jobCenterCode = null;

        JobContent jobContent = new JobContent.Builder()
                .setJobDescriptions(Collections.singletonList(
                        new JobDescription.Builder()
                                .setLanguage(new Locale(createJobAdvertisementApiDto.getJob().getLanguageIsoCode()))
                                .setTitle(createJobAdvertisementApiDto.getJob().getTitle())
                                .setDescription(createJobAdvertisementApiDto.getJob().getDescription())
                                .build()
                ))
                .setLocation(location)
                .setOccupations(Collections.singletonList(occupation))
                .setEmployment(toEmployment(createJobAdvertisementApiDto.getJob()))
                .setApplyChannel(toApplyChannel(createJobAdvertisementApiDto.getApplyChannel()))
                .setCompany(toCompany(createJobAdvertisementApiDto.getCompany()))
                .setPublicContact(toPublicContact(createJobAdvertisementApiDto.getContact()))
                .setLanguageSkills(toLanguageSkills(createJobAdvertisementApiDto.getJob().getLanguageSkills()))
                .build();

        Publication publication = new Publication.Builder()
                // TODO Add fields of publication
                .setEures(false)
                .build();

        final JobAdvertisementCreator creator = new JobAdvertisementCreator.Builder(null)
                .setReportingObligation(reportingObligation)
                .setJobCenterCode(jobCenterCode)
                .setJobContent(jobContent)
                .setContact(toContact(createJobAdvertisementApiDto.getContact()))
                .setPublication(publication)
                .build();

        JobAdvertisement jobAdvertisement = jobAdvertisementFactory.createFromApi(creator);
        return jobAdvertisement.getId();
    }

    public JobAdvertisementId createFromAvam(CreateJobAdvertisementAvamDto createJobAdvertisementAvamDto) {
        LOG.debug("Create or update '{}' from AVAM", createJobAdvertisementAvamDto.getTitle());

        Location location = toLocation(createJobAdvertisementAvamDto.getLocation());
        location = locationService.enrichCodes(location);

        List<Occupation> occupations = createJobAdvertisementAvamDto.getOccupations().stream()
                .map(this::toOccupation)
                .map(this::enrichOccupationWithProfessionCodes)
                .collect(toList());

        JobContent jobContent = new JobContent.Builder()
                .setJobDescriptions(Collections.singletonList(
                        new JobDescription.Builder()
                                .setLanguage(Locale.GERMAN)
                                .setTitle(createJobAdvertisementAvamDto.getTitle())
                                .setDescription(createJobAdvertisementAvamDto.getDescription())
                                .build()
                ))
                .setLocation(location)
                .setOccupations(occupations)
                .setEmployment(toEmployment(createJobAdvertisementAvamDto.getEmployment()))
                .setApplyChannel(toApplyChannel(createJobAdvertisementAvamDto.getApplyChannel()))
                .setCompany(toCompany(createJobAdvertisementAvamDto.getCompany()))
                .setPublicContact(toPublicContact(createJobAdvertisementAvamDto.getContact()))
                .setLanguageSkills(toLanguageSkills(createJobAdvertisementAvamDto.getLanguageSkills()))
                .build();

        Publication publication = new Publication.Builder()
                .setEures(createJobAdvertisementAvamDto.getPublication().isEures())
                .setEuresAnonymous(createJobAdvertisementAvamDto.getPublication().isEuresAnonymous())
                .setPublicDisplay(createJobAdvertisementAvamDto.getPublication().isPublicDisplay())
                .setPublicAnonynomous(createJobAdvertisementAvamDto.getPublication().isPublicAnonynomous())
                .setRestrictedDisplay(createJobAdvertisementAvamDto.getPublication().isRestrictedDisplay())
                .setRestrictedAnonymous(createJobAdvertisementAvamDto.getPublication().isRestrictedAnonymous())
                .build();

        final JobAdvertisementCreator creator = new JobAdvertisementCreator.Builder(null)
                // FIXME .setReportingObligation(createJobAdvertisementAvamDto.getReportingObligation())
                .setJobCenterCode(createJobAdvertisementAvamDto.getJobCenterCode())
                .setJobContent(jobContent)
                .setContact(toContact(createJobAdvertisementAvamDto.getContact()))
                .setPublication(publication)
                .build();

        JobAdvertisement jobAdvertisement = jobAdvertisementFactory.createFromAvam(creator);
        return jobAdvertisement.getId();
    }

    public void updateFromAvam(UpdateJobAdvertisementFromAvamDto updateJobAdvertisementFromAvamDto) {
        final String stellennummerAvam = updateJobAdvertisementFromAvamDto.getStellennummerAvam();
        JobAdvertisement jobAdvertisement = jobAdvertisementRepository.findByStellennummerAvam(stellennummerAvam)
                .orElseThrow(() -> new EntityNotFoundException("JobAdvertisement not found. stellennummerAvam: " + stellennummerAvam));

        JobAdvertisementUpdater updater = new JobAdvertisementUpdater.Builder(null)
                // TODO TBD what can be updated from AVAM
                .build();

        jobAdvertisement.update(updater);
    }

    public JobAdvertisementId createFromX28(CreateJobAdvertisementFromX28Dto createJobAdvertisementFromX28Dto) {
        LOG.debug("Create '{}' from X28", createJobAdvertisementFromX28Dto.getTitle());

        Location location = toLocation(createJobAdvertisementFromX28Dto.getLocation());
        location = locationService.enrichCodes(location);

        List<Occupation> occupations = createJobAdvertisementFromX28Dto.getOccupations().stream()
                .map(this::toOccupation)
                .map(this::enrichOccupationWithProfessionCodes)
                .collect(toList());

        JobContent jobContent = new JobContent.Builder()
                .setJobDescriptions(Collections.singletonList(
                        new JobDescription.Builder()
                                .setLanguage(Locale.GERMAN)
                                .setTitle(createJobAdvertisementFromX28Dto.getTitle())
                                .setDescription(createJobAdvertisementFromX28Dto.getDescription())
                                .build()
                ))
                .setLocation(location)
                .setOccupations(occupations)
                .setEmployment(toEmployment(createJobAdvertisementFromX28Dto.getEmployment()))
                .setApplyChannel(toApplyChannel(createJobAdvertisementFromX28Dto.getApplyChannel()))
                .setCompany(toCompany(createJobAdvertisementFromX28Dto.getCompany()))
                .build();

        final JobAdvertisementCreator creator = new JobAdvertisementCreator.Builder(null)
                .setJobContent(jobContent)
                .setPublication(
                        new Publication.Builder()
                                .setEndDate(TimeMachine.now().plusDays(X28_PUBLICATION_MAX_DAYS).toLocalDate())
                                .build()
                )
                .build();

        JobAdvertisement jobAdvertisement = jobAdvertisementFactory.createFromExtern(creator);
        return jobAdvertisement.getId();
    }

    public void updateFromX28(UpdateJobAdvertisementFromX28Dto updateJobAdvertisementFromX28Dto) {
        final String stellennummerEgov = updateJobAdvertisementFromX28Dto.getStellennummerEgov();
        JobAdvertisement jobAdvertisement = jobAdvertisementRepository.findByStellennummerEgov(stellennummerEgov)
                .orElseThrow(() -> new EntityNotFoundException("JobAdvertisement not found. stellennummerEgov: " + stellennummerEgov));

        JobAdvertisementUpdater updater = new JobAdvertisementUpdater.Builder(null)
                .setFingerprint(updateJobAdvertisementFromX28Dto.getFingerprint())
                .setX28OccupationCodes(updateJobAdvertisementFromX28Dto.getX28OccupationCodes())
                .build();

        jobAdvertisement.update(updater);
    }

    public List<JobAdvertisementDto> findAll() {
        List<JobAdvertisement> jobAdvertisements = jobAdvertisementRepository.findAll();
        return jobAdvertisements.stream().map(JobAdvertisementDto::toDto).collect(toList());
    }

    public JobAdvertisementDto findById(JobAdvertisementId jobAdvertisementId) throws AggregateNotFoundException {
        JobAdvertisement jobAdvertisement = getJobAdvertisement(jobAdvertisementId);
        return JobAdvertisementDto.toDto(jobAdvertisement);
    }

    public void inspect(JobAdvertisementId jobAdvertisementId) {
        Condition.notNull(jobAdvertisementId, "JobAdvertisementId can't be null");
        LOG.debug("Starting inspect for JobAdvertisementId: '{}'", jobAdvertisementId.getValue());
        JobAdvertisement jobAdvertisement = getJobAdvertisement(jobAdvertisementId);
        jobAdvertisement.inspect();
    }

    public void approve(ApprovalDto approvalDto) {
        // TODO tbd where/when the data updates has to be done (over ApprovalDto --> JobAdUpdater?)
        Condition.notNull(approvalDto.getStellennummerAvam(), "StellennummerAvam can't be null");
        JobAdvertisement jobAdvertisement = getJobAdvertisementByStellennummerAvam(approvalDto.getStellennummerAvam());
        LOG.debug("Starting approve for JobAdvertisementId: '{}'", jobAdvertisement.getId().getValue());
        jobAdvertisement.approve(approvalDto.getStellennummerAvam(), approvalDto.getDate(), approvalDto.isReportingObligation(), approvalDto.getReportingObligationEndDate());
    }

    public void reject(RejectionDto rejectionDto) {
        Condition.notNull(rejectionDto.getStellennummerAvam(), "StellennummerAvam can't be null");
        JobAdvertisement jobAdvertisement = getJobAdvertisementByStellennummerAvam(rejectionDto.getStellennummerAvam());
        LOG.debug("Starting reject for JobAdvertisementId: '{}'", jobAdvertisement.getId().getValue());
        jobAdvertisement.reject(rejectionDto.getStellennummerAvam(), rejectionDto.getDate(), rejectionDto.getCode(), rejectionDto.getReason());
    }

    public void cancel(CancellationDto cancellationDto) {
        Condition.notNull(cancellationDto.getStellennummerAvam(), "StellennummerAvam can't be null");
        JobAdvertisement jobAdvertisement = getJobAdvertisementByStellennummerAvam(cancellationDto.getStellennummerAvam());
        LOG.debug("Starting cancel for JobAdvertisementId: '{}'", jobAdvertisement.getId().getValue());
        jobAdvertisement.cancel(cancellationDto.getDate(), cancellationDto.getCode());
    }

    public void refining(JobAdvertisementId jobAdvertisementId) {
        Condition.notNull(jobAdvertisementId, "JobAdvertisementId can't be null");
        LOG.debug("Starting refining for JobAdvertisementId: '{}'", jobAdvertisementId.getValue());
        JobAdvertisement jobAdvertisement = getJobAdvertisement(jobAdvertisementId);
        jobAdvertisement.refining();
    }

    @Scheduled(cron = "${jobAdvertisement.checkBlackoutPolicyExpiration.cron}")
    public void checkBlackoutPolicyExpiration() {
        this.jobAdvertisementRepository
                .findAllWhereBlackoutNeedToExpire(TimeMachine.now().toLocalDate())
                .forEach(JobAdvertisement::expireBlackout);

    }

    @Scheduled(cron = "${jobAdvertisement.checkPublicationExpiration.cron}")
    public void checkPublicationExpiration() {
        this.jobAdvertisementRepository
                .findAllWherePublicationNeedToExpire(TimeMachine.now().toLocalDate())
                .forEach(JobAdvertisement::expirePublication);

    }

    public void publish(JobAdvertisementId jobAdvertisementId) {
        Condition.notNull(jobAdvertisementId, "JobAdvertisementId can't be null");
        LOG.debug("Starting publish for JobAdvertisementId: '{}'", jobAdvertisementId.getValue());
        JobAdvertisement jobAdvertisement = getJobAdvertisement(jobAdvertisementId);
        if (jobAdvertisement.isReportingObligation() && REFINING.equals(jobAdvertisement.getStatus())) {
            LOG.debug("Publish in restricted area for JobAdvertisementId: '{}'", jobAdvertisementId.getValue());
            jobAdvertisement.publishRestricted();
        } else {
            LOG.debug("Publish in public area for JobAdvertisementId: '{}'", jobAdvertisementId.getValue());
            jobAdvertisement.publishPublic();
        }
    }

    public void archive(JobAdvertisementId jobAdvertisementId) {
        Condition.notNull(jobAdvertisementId, "JobAdvertisementId can't be null");
        LOG.debug("Starting archive for JobAdvertisementId: '{}'", jobAdvertisementId.getValue());
        JobAdvertisement jobAdvertisement = getJobAdvertisement(jobAdvertisementId);
        jobAdvertisement.archive();
    }

    private JobAdvertisement getJobAdvertisement(JobAdvertisementId jobAdvertisementId) throws AggregateNotFoundException {
        Optional<JobAdvertisement> jobAdvertisement = jobAdvertisementRepository.findById(jobAdvertisementId);
        return jobAdvertisement.orElseThrow(() -> new AggregateNotFoundException(JobAdvertisement.class, jobAdvertisementId.getValue()));
    }

    private JobAdvertisement getJobAdvertisementByStellennummerAvam(String stellennummerAvam) throws AggregateNotFoundException {
        Optional<JobAdvertisement> jobAdvertisement = jobAdvertisementRepository.findByStellennummerAvam(stellennummerAvam);
        return jobAdvertisement.orElseThrow(() -> new AggregateNotFoundException(JobAdvertisement.class, AggregateNotFoundException.IndentifierType.EXTERNAL_ID, stellennummerAvam));
    }

    private Occupation enrichOccupationWithProfessionCodes(Occupation occupation) {
        Condition.notNull(occupation, "Occupation can't be null");
        Profession profession = professionSerivce.findByAvamCode(occupation.getAvamOccupationCode());
        if (profession != null) {
            return new Occupation.Builder()
                    .setAvamOccupationCode(occupation.getAvamOccupationCode())
                    .setSbn3Code(profession.getSbn3Code())
                    .setSbn5Code(profession.getSbn5Code())
                    .setBfsCode(profession.getBfsCode())
                    .setLabel(profession.getLabel())
                    .setWorkExperience(occupation.getWorkExperience())
                    .setEducationCode(occupation.getEducationCode())
                    .build();
        }
        return occupation;
    }

    private boolean checkReportingObligation(Occupation occupation, Location location) {
        Condition.notNull(occupation, "Occupation can't be null");
        Condition.notNull(location, "Location can't be null");
        String avamOccupationCode = occupation.getAvamOccupationCode();
        String cantonCode = location.getCantonCode();
        return (cantonCode != null) && reportingObligationService.hasReportingObligation(ProfessionCodeType.AVAM, avamOccupationCode, cantonCode);
    }

    private Employment toEmployment(JobApiDto jobApiDto) {
        return new Employment.Builder()
                .setStartDate(jobApiDto.getStartDate())
                .setEndDate(jobApiDto.getEndDate())
                .setShortEmployment(jobApiDto.getShortEmployment())
                .setImmediately(jobApiDto.getStartsImmediately())
                .setPermanent(jobApiDto.getPermanent())
                .setWorkloadPercentageMin(jobApiDto.getWorkingTimePercentageFrom())
                .setWorkloadPercentageMax(jobApiDto.getWorkingTimePercentageTo())
                // FIXME .setWorkForms(jobApiDto.getWorkForms())
                .build();
    }

    private Employment toEmployment(EmploymentDto employmentDto) {
        if (employmentDto != null) {
            return new Employment.Builder()
                    .setStartDate(employmentDto.getStartDate())
                    .setEndDate(employmentDto.getEndDate())
                    .setShortEmployment(employmentDto.getShortEmployment())
                    .setImmediately(employmentDto.getImmediately())
                    .setPermanent(employmentDto.getPermanent())
                    .setWorkloadPercentageMin(employmentDto.getWorkloadPercentageMin())
                    .setWorkloadPercentageMax(employmentDto.getWorkloadPercentageMax())
                    .setWorkForms(employmentDto.getWorkForms())
                    .build();
        }
        return null;
    }

    private ApplyChannel toApplyChannel(ApplyChannelDto applyChannelDto) {
        if (applyChannelDto != null) {
            return new ApplyChannel.Builder()
                    .setMailAddress(applyChannelDto.getMailAddress())
                    .setEmailAddress(applyChannelDto.getEmailAddress())
                    .setPhoneNumber(applyChannelDto.getPhoneNumber())
                    .setFormUrl(applyChannelDto.getFormUrl())
                    .setAdditionalInfo(applyChannelDto.getAdditionalInfo())
                    .build();
        }
        return null;
    }

    private Company toCompany(CompanyDto companyDto) {
        if (companyDto != null) {
            return new Company.Builder()
                    .setName(companyDto.getName())
                    .setStreet(companyDto.getStreet())
                    .setHouseNumber(companyDto.getHouseNumber())
                    .setPostalCode(companyDto.getPostalCode())
                    .setCity(companyDto.getCity())
                    .setCountryIsoCode(companyDto.getCountryIsoCode())
                    .setPostOfficeBoxNumber(companyDto.getPostOfficeBoxNumber())
                    .setPostOfficeBoxPostalCode(companyDto.getPostOfficeBoxPostalCode())
                    .setPostOfficeBoxCity(companyDto.getPostOfficeBoxCity())
                    .setPhone(companyDto.getPhone())
                    .setEmail(companyDto.getEmail())
                    .setWebsite(companyDto.getWebsite())
                    .build();
        }
        return null;
    }

    private PublicContact toPublicContact(ContactDto contactDto) {
        if (contactDto != null) {
            return new PublicContact.Builder()
                    .setSalutation(contactDto.getSalutation())
                    .setFirstName(contactDto.getFirstName())
                    .setLastName(contactDto.getLastName())
                    .setPhone(contactDto.getPhone())
                    .setEmail(contactDto.getEmail())
                    .build();
        }
        return null;
    }

    private Contact toContact(ContactDto contactDto) {
        if (contactDto != null) {
            return new Contact.Builder()
                    .setSalutation(contactDto.getSalutation())
                    .setFirstName(contactDto.getFirstName())
                    .setLastName(contactDto.getLastName())
                    .setPhone(contactDto.getPhone())
                    .setEmail(contactDto.getEmail())
                    .setLanguage(new Locale(contactDto.getLanguageIsoCode()))
                    .build();
        }
        return null;
    }

    private Location toLocation(CreateLocationDto createLocationDto) {
        if (createLocationDto != null) {
            return new Location.Builder()
                    .setRemarks(createLocationDto.getRemarks())
                    .setCity(createLocationDto.getCity())
                    .setPostalCode(createLocationDto.getPostalCode())
                    .setCommunalCode(createLocationDto.getCommunalCode())
                    .setCountryIsoCode(createLocationDto.getCountryIsoCode())
                    .build();
        }
        return null;
    }

    private Location toLocation(LocationDto locationDto) {
        if (locationDto != null) {
            return new Location.Builder()
                    .setRemarks(locationDto.getRemarks())
                    .setCity(locationDto.getCity())
                    .setPostalCode(locationDto.getPostalCode())
                    .setCommunalCode(locationDto.getCommunalCode())
                    .setRegionCode(locationDto.getRegionCode())
                    .setCantonCode(locationDto.getCantonCode())
                    .setCountryIsoCode(locationDto.getCountryIsoCode())
                    .setCoordinates(locationDto.getCoordinates())
                    .build();
        }
        return null;
    }

    private Occupation toOccupation(OccupationDto occupationDto) {
        if (occupationDto != null) {
            return new Occupation.Builder()
                    .setAvamOccupationCode(occupationDto.getAvamOccupationCode())
                    .setWorkExperience(occupationDto.getWorkExperience())
                    .setEducationCode(occupationDto.getEducationCode())
                    .build();
        }
        return null;
    }

    private List<LanguageSkill> toLanguageSkills(List<LanguageSkillDto> languageSkillDtos) {
        if (languageSkillDtos != null) {
            return languageSkillDtos.stream()
                    .map(languageSkillDto -> new LanguageSkill.Builder()
                            .setLanguageIsoCode(languageSkillDto.getLanguageIsoCode())
                            .setSpokenLevel(languageSkillDto.getSpokenLevel())
                            .setWrittenLevel(languageSkillDto.getWrittenLevel())
                            .build()
                    )
                    .collect(toList());
        }
        return null;
    }
}
