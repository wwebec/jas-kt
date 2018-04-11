package ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement;

import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisementStatus.REFINING;
import static java.util.stream.Collectors.toList;

import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import ch.admin.seco.jobs.services.jobadservice.application.JobCenterService;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.*;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ch.admin.seco.jobs.services.jobadservice.application.LocationService;
import ch.admin.seco.jobs.services.jobadservice.application.ProfessionService;
import ch.admin.seco.jobs.services.jobadservice.application.ReportingObligationService;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.create.CreateJobAdvertisementDto;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.create.CreateJobAdvertisementFromAvamDto;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.create.CreateJobAdvertisementFromX28Dto;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.create.CreateLocationDto;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.update.ApprovalDto;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.update.CancellationDto;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.update.RejectionDto;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.update.UpdateJobAdvertisementFromAvamDto;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.update.UpdateJobAdvertisementFromX28Dto;
import ch.admin.seco.jobs.services.jobadservice.core.conditions.Condition;
import ch.admin.seco.jobs.services.jobadservice.core.domain.AggregateNotFoundException;
import ch.admin.seco.jobs.services.jobadservice.core.time.TimeMachine;
import ch.admin.seco.jobs.services.jobadservice.domain.profession.Profession;
import ch.admin.seco.jobs.services.jobadservice.domain.profession.ProfessionCodeType;

@Service
@Transactional(rollbackFor = {Exception.class})
public class JobAdvertisementApplicationService {

    public static final int X28_PUBLICATION_MAX_DAYS = 60;
    private static Logger LOG = LoggerFactory.getLogger(JobAdvertisementApplicationService.class);

    private final JobAdvertisementRepository jobAdvertisementRepository;

    private final JobAdvertisementFactory jobAdvertisementFactory;

    private final ReportingObligationService reportingObligationService;

    private final LocationService locationService;

    private final ProfessionService professionService;

    private final JobCenterService jobCenterService;

    @Autowired
    public JobAdvertisementApplicationService(JobAdvertisementRepository jobAdvertisementRepository,
                                              JobAdvertisementFactory jobAdvertisementFactory,
                                              ReportingObligationService reportingObligationService,
                                              LocationService locationService,
                                              ProfessionService professionService,
                                              JobCenterService jobCenterService) {
        this.jobAdvertisementRepository = jobAdvertisementRepository;
        this.jobAdvertisementFactory = jobAdvertisementFactory;
        this.reportingObligationService = reportingObligationService;
        this.locationService = locationService;
        this.professionService = professionService;
        this.jobCenterService = jobCenterService;
    }

    public JobAdvertisementId createFromWebForm(CreateJobAdvertisementDto createJobAdvertisementDto) {
        LOG.debug("Create '{}' from Webform", createJobAdvertisementDto.getJobDescriptions().get(0));

        final JobAdvertisementCreator creator = getJobAdvertisementCreatorFromInternal(createJobAdvertisementDto);
        JobAdvertisement jobAdvertisement = jobAdvertisementFactory.createFromWebForm(creator);
        return jobAdvertisement.getId();
    }

    public JobAdvertisementId createFromApi(CreateJobAdvertisementDto createJobAdvertisementDto) {
        LOG.debug("Create '{}' from API", createJobAdvertisementDto.getJobDescriptions().get(0));

        final JobAdvertisementCreator creator = getJobAdvertisementCreatorFromInternal(createJobAdvertisementDto);
        JobAdvertisement jobAdvertisement = jobAdvertisementFactory.createFromApi(creator);
        return jobAdvertisement.getId();
    }

    public JobAdvertisementId createFromAvam(CreateJobAdvertisementFromAvamDto createJobAdvertisementFromAvamDto) {
        checkifJobAdvertisementAlreadExists(createJobAdvertisementFromAvamDto);
        LOG.debug("Create '{}' from AVAM", createJobAdvertisementFromAvamDto.getTitle());

        Location location = toLocation(createJobAdvertisementFromAvamDto.getLocation());
        location = locationService.enrichCodes(location);

        List<Occupation> occupations = createJobAdvertisementFromAvamDto.getOccupations().stream()
                .map(this::toOccupation)
                .map(this::enrichOccupationWithProfessionCodes)
                .collect(toList());

        JobContent jobContent = new JobContent.Builder()
                .setJobDescriptions(Collections.singletonList(
                        new JobDescription.Builder()
                                .setLanguage(Locale.GERMAN)
                                .setTitle(createJobAdvertisementFromAvamDto.getTitle())
                                .setDescription(createJobAdvertisementFromAvamDto.getDescription())
                                .build()
                ))
                .setLocation(location)
                .setOccupations(occupations)
                .setEmployment(toEmployment(createJobAdvertisementFromAvamDto.getEmployment()))
                .setApplyChannel(toApplyChannel(createJobAdvertisementFromAvamDto.getApplyChannel()))
                .setCompany(toCompany(createJobAdvertisementFromAvamDto.getCompany()))
                .setPublicContact(toPublicContact(createJobAdvertisementFromAvamDto.getContact()))
                .setLanguageSkills(toLanguageSkills(createJobAdvertisementFromAvamDto.getLanguageSkills()))
                .build();

        // TODO Add auditUser
        final JobAdvertisementCreator creator = new JobAdvertisementCreator.Builder(null)
                .setReportingObligation(createJobAdvertisementFromAvamDto.isReportingObligation())
                .setReportingObligationEndDate(createJobAdvertisementFromAvamDto.getReportingObligationEndDate())
                .setJobCenterCode(createJobAdvertisementFromAvamDto.getJobCenterCode())
                .setApprovalDate(createJobAdvertisementFromAvamDto.getApprovalDate())
                .setJobContent(jobContent)
                .setContact(toContact(createJobAdvertisementFromAvamDto.getContact()))
                .setPublication(toPublication(createJobAdvertisementFromAvamDto.getPublication()))
                .build();

        JobAdvertisement jobAdvertisement = jobAdvertisementFactory.createFromAvam(creator);
        return jobAdvertisement.getId();
    }

    public void updateFromAvam(UpdateJobAdvertisementFromAvamDto updateJobAdvertisementFromAvamDto) {
        LOG.debug("Update StellennummerAvam '{}' from AVAM", updateJobAdvertisementFromAvamDto.getStellennummerAvam());
        final String stellennummerAvam = updateJobAdvertisementFromAvamDto.getStellennummerAvam();
        JobAdvertisement jobAdvertisement = jobAdvertisementRepository.findByStellennummerAvam(stellennummerAvam)
                .orElseThrow(() -> new EntityNotFoundException("JobAdvertisement not found. stellennummerAvam: " + stellennummerAvam));

        // TODO Add auditUser
        JobAdvertisementUpdater updater = new JobAdvertisementUpdater.Builder(null)
                // TODO TBD what can be updated from AVAM
                .build();

        jobAdvertisement.update(updater);
    }

    public JobAdvertisementId createFromX28(CreateJobAdvertisementFromX28Dto createJobAdvertisementFromX28Dto) {
        checkifJobAdvertisementAlreadExists(createJobAdvertisementFromX28Dto);

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
                .setExternalUrl(createJobAdvertisementFromX28Dto.getExternalUrl())
                .setLocation(location)
                .setOccupations(occupations)
                .setEmployment(toEmployment(createJobAdvertisementFromX28Dto.getEmployment()))
                .setCompany(toCompany(createJobAdvertisementFromX28Dto.getCompany()))
                .build();

        // TODO Add auditUser
        final JobAdvertisementCreator creator = new JobAdvertisementCreator.Builder(null)
                .setFingerprint(createJobAdvertisementFromX28Dto.getFingerprint())
                .setJobContent(jobContent)
                .setPublication(
                        new Publication.Builder()
                                .setStartDate(TimeMachine.now().toLocalDate())
                                .setEndDate(TimeMachine.now().plusDays(X28_PUBLICATION_MAX_DAYS).toLocalDate())
                                .setEures(false)
                                .setEuresAnonymous(false)
                                .setPublicDisplay(true)
                                .setPublicAnonynomous(true)
                                .build()
                )
                .build();

        JobAdvertisement jobAdvertisement = jobAdvertisementFactory.createFromExtern(creator);
        return jobAdvertisement.getId();
    }

    public void updateFromX28(UpdateJobAdvertisementFromX28Dto updateJobAdvertisementFromX28Dto) {
        LOG.debug("Update StellennummerEgov '{}' from X28", updateJobAdvertisementFromX28Dto.getStellennummerEgov());
        final String stellennummerEgov = updateJobAdvertisementFromX28Dto.getStellennummerEgov();
        JobAdvertisement jobAdvertisement = jobAdvertisementRepository.findByStellennummerEgov(stellennummerEgov)
                .orElseThrow(() -> new EntityNotFoundException("JobAdvertisement not found. stellennummerEgov: " + stellennummerEgov));

        // TODO Add auditUser
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
        Condition.notNull(approvalDto.getStellennummerEgov(), "StellennummerEgov can't be null");
        JobAdvertisement jobAdvertisement = getJobAdvertisementByStellennummerEgov(approvalDto.getStellennummerEgov());
        LOG.debug("Starting approve for JobAdvertisementId: '{}'", jobAdvertisement.getId().getValue());
        jobAdvertisement.approve(approvalDto.getStellennummerAvam(), approvalDto.getDate(), approvalDto.isReportingObligation(), approvalDto.getReportingObligationEndDate());
    }

    public void reject(RejectionDto rejectionDto) {
        Condition.notNull(rejectionDto.getStellennummerEgov(), "StellennummerEgov can't be null");
        JobAdvertisement jobAdvertisement = getJobAdvertisementByStellennummerEgov(rejectionDto.getStellennummerEgov());
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

    private void checkifJobAdvertisementAlreadExists(CreateJobAdvertisementFromX28Dto createJobAdvertisementFromX28Dto) {
        Optional<JobAdvertisement> jobAdvertisement = jobAdvertisementRepository.findByFingerprint(createJobAdvertisementFromX28Dto.getFingerprint());
        jobAdvertisement.ifPresent(jobAd -> {
            String message = String.format("JobAdvertisement '%s' with fingerprint '%s' already exists", jobAd.getId().getValue(), jobAd.getFingerprint());
            throw new JobAdvertisementAlreadyExistsException(message);
        });
    }

    private void checkifJobAdvertisementAlreadExists(CreateJobAdvertisementFromAvamDto createJobAdvertisementFromAvamDto) {
        Optional<JobAdvertisement> jobAdvertisement = jobAdvertisementRepository.findByStellennummerAvam(createJobAdvertisementFromAvamDto.getStellennummerAvam());
        jobAdvertisement.ifPresent(jobAd -> {
            String message = String.format("JobAdvertisement '%s' with stellennummerAvam '%s' already exists", jobAd.getId().getValue(), jobAd.getStellennummerAvam());
            throw new JobAdvertisementAlreadyExistsException(message);
        });
    }

    private JobAdvertisementCreator getJobAdvertisementCreatorFromInternal(CreateJobAdvertisementDto createJobAdvertisementDto) {
        Location location = toLocation(createJobAdvertisementDto.getLocation());
        location = locationService.enrichCodes(location);

        String jobCenterCode = jobCenterService.findJobCenterCode(location.getCountryIsoCode(), location.getPostalCode());

        List<Occupation> occupations = null;
        boolean reportingObligation = false;

        if (createJobAdvertisementDto.getOccupation() != null) {
            Occupation occupation = toOccupation(createJobAdvertisementDto.getOccupation());
            // FIXME remove this if, when the legacy API is deleted
            if (occupation != null) {
                occupation = enrichOccupationWithProfessionCodes(occupation);
                reportingObligation = checkReportingObligation(
                        occupation,
                        location
                );
            }
            occupations = Collections.singletonList(occupation);
        }

        JobContent jobContent = new JobContent.Builder()
                .setJobDescriptions(toJobDescriptions(createJobAdvertisementDto.getJobDescriptions()))
                .setLocation(location)
                .setOccupations(occupations)
                .setEmployment(toEmployment(createJobAdvertisementDto.getEmployment()))
                .setApplyChannel(toApplyChannel(createJobAdvertisementDto.getApplyChannel()))
                .setCompany(toCompany(createJobAdvertisementDto.getCompany()))
                .setEmployer(toEmployer(createJobAdvertisementDto.getEmployer()))
                .setPublicContact(toPublicContact(createJobAdvertisementDto.getContact()))
                .setLanguageSkills(toLanguageSkills(createJobAdvertisementDto.getLanguageSkills()))
                .build();

        // TODO Add auditUser
        return new JobAdvertisementCreator.Builder(null)
                .setReportingObligation(reportingObligation)
                .setJobCenterCode(jobCenterCode)
                .setJobContent(jobContent)
                .setContact(toContact(createJobAdvertisementDto.getContact()))
                .setPublication(toPublication(createJobAdvertisementDto.getPublication()))
                .build();
    }

    private JobAdvertisement getJobAdvertisement(JobAdvertisementId jobAdvertisementId) throws AggregateNotFoundException {
        Optional<JobAdvertisement> jobAdvertisement = jobAdvertisementRepository.findById(jobAdvertisementId);
        return jobAdvertisement.orElseThrow(() -> new AggregateNotFoundException(JobAdvertisement.class, jobAdvertisementId.getValue()));
    }

    private JobAdvertisement getJobAdvertisementByStellennummerEgov(String stellennummerEgov) throws AggregateNotFoundException {
        Optional<JobAdvertisement> jobAdvertisement = jobAdvertisementRepository.findByStellennummerEgov(stellennummerEgov);
        return jobAdvertisement.orElseThrow(() -> new AggregateNotFoundException(JobAdvertisement.class, AggregateNotFoundException.IndentifierType.EXTERNAL_ID, stellennummerEgov));
    }

    private JobAdvertisement getJobAdvertisementByStellennummerAvam(String stellennummerAvam) throws AggregateNotFoundException {
        Optional<JobAdvertisement> jobAdvertisement = jobAdvertisementRepository.findByStellennummerAvam(stellennummerAvam);
        return jobAdvertisement.orElseThrow(() -> new AggregateNotFoundException(JobAdvertisement.class, AggregateNotFoundException.IndentifierType.EXTERNAL_ID, stellennummerAvam));
    }

    private Occupation enrichOccupationWithProfessionCodes(Occupation occupation) {
        Condition.notNull(occupation, "Occupation can't be null");
        Profession profession = professionService.findByAvamCode(occupation.getAvamOccupationCode());
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

    private List<JobDescription> toJobDescriptions(List<JobDescriptionDto> jobDescriptionDtos) {
        if (jobDescriptionDtos != null) {
            return jobDescriptionDtos.stream()
                    .map(jobDescriptionDto -> new JobDescription.Builder()
                            .setLanguage(new Locale(jobDescriptionDto.getLanguageIsoCode()))
                            .setTitle(jobDescriptionDto.getTitle())
                            .setDescription(jobDescriptionDto.getDescription())
                            .build()
                    )
                    .collect(toList());
        }
        return null;
    }

    private Publication toPublication(PublicationDto publicationDto) {
        if (publicationDto != null) {
            return new Publication.Builder()
                    .setStartDate(publicationDto.getStartDate())
                    .setEndDate(publicationDto.getEndDate())
                    .setEures(publicationDto.isEures())
                    .setEuresAnonymous(publicationDto.isEuresAnonymous())
                    .setPublicDisplay(publicationDto.isPublicDisplay())
                    .setPublicAnonynomous(publicationDto.isPublicAnonynomous())
                    .setRestrictedDisplay(publicationDto.isRestrictedDisplay())
                    .setRestrictedAnonymous(publicationDto.isRestrictedAnonymous())
                    .build();
        }
        return null;
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

    private Employer toEmployer(EmployerDto employerDto) {
        if (employerDto != null) {
            return new Employer.Builder()
                    .setName(employerDto.getName())
                    .setPostalCode(employerDto.getPostalCode())
                    .setCity(employerDto.getCity())
                    .setCountryIsoCode(employerDto.getCountryIsoCode())
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
