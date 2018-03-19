package ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement;

import ch.admin.seco.jobs.services.jobadservice.application.LocationService;
import ch.admin.seco.jobs.services.jobadservice.application.ProfessionService;
import ch.admin.seco.jobs.services.jobadservice.application.RavRegistrationService;
import ch.admin.seco.jobs.services.jobadservice.application.ReportingObligationService;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.*;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.CreateJobAdvertisementApiDto;
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

import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisementStatus.REFINING;

@Service
@Transactional(rollbackFor = {Exception.class})
public class JobAdvertisementApplicationService {

    private static Logger LOG = LoggerFactory.getLogger(JobAdvertisementApplicationService.class);

    private final JobAdvertisementRepository jobAdvertisementRepository;

    private final JobAdvertisementFactory jobAdvertisementFactory;

    private final RavRegistrationService ravRegistrationService;

    private final ReportingObligationService reportingObligationService;

    private final LocationService locationService;

    private final ProfessionService professionSerivce;

    @Autowired
    public JobAdvertisementApplicationService(JobAdvertisementRepository jobAdvertisementRepository,
                                              JobAdvertisementFactory jobAdvertisementFactory,
                                              RavRegistrationService ravRegistrationService,
                                              ReportingObligationService reportingObligationService,
                                              LocationService locationService,
                                              ProfessionService professionSerivce) {
        this.jobAdvertisementRepository = jobAdvertisementRepository;
        this.jobAdvertisementFactory = jobAdvertisementFactory;
        this.ravRegistrationService = ravRegistrationService;
        this.reportingObligationService = reportingObligationService;
        this.locationService = locationService;
        this.professionSerivce = professionSerivce;
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

        final JobAdvertisementUpdater updater = new JobAdvertisementUpdater.Builder(null)
                .setLocation(location)
                .setOccupations(Collections.singletonList(occupation))
                .setReportingObligation(reportingObligation)
                // FIXME eval eures if anonymous or not (User/Security)
                .setEures(createJobAdvertisementWebFormDto.isEures(), true)
                .setEmployment(toEmployment(createJobAdvertisementWebFormDto.getEmployment()))
                .setApplyChannel(toApplyChannel(createJobAdvertisementWebFormDto.getApplyChannel()))
                .setCompany(toCompany(createJobAdvertisementWebFormDto.getCompany()))
                .setContact(toContact(createJobAdvertisementWebFormDto.getContact()))
                .setLanguageSkills(toLanguageSkills(createJobAdvertisementWebFormDto.getLanguageSkills()))
                .build();

        JobAdvertisement jobAdvertisement = jobAdvertisementFactory.createFromWebForm(
                new Locale(createJobAdvertisementWebFormDto.getLanguageIsoCode()),
                createJobAdvertisementWebFormDto.getTitle(),
                createJobAdvertisementWebFormDto.getDescription(),
                updater
        );
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

        final JobAdvertisementUpdater updater = new JobAdvertisementUpdater.Builder(null)
                .setLocation(location)
                .setOccupations(Collections.singletonList(occupation))
                .setReportingObligation(reportingObligation)
                .setEmployment(toEmployment(createJobAdvertisementApiDto.getJob()))
                .setApplyChannel(toApplyChannel(createJobAdvertisementApiDto.getApplyChannel()))
                .setCompany(toCompany(createJobAdvertisementApiDto.getCompany()))
                .setContact(toContact(createJobAdvertisementApiDto.getContact()))
                .setLanguageSkills(toLanguageSkills(createJobAdvertisementApiDto.getJob().getLanguageSkills()))
                .build();

        JobAdvertisement jobAdvertisement = jobAdvertisementFactory.createFromApi(
                new Locale(createJobAdvertisementApiDto.getJob().getLanguageIsoCode()),
                createJobAdvertisementApiDto.getJob().getTitle(),
                createJobAdvertisementApiDto.getJob().getDescription(),
                updater,
                createJobAdvertisementApiDto.isReportToRav()
        );
        return jobAdvertisement.getId();
    }

    public List<JobAdvertisementDto> findAll() {
        List<JobAdvertisement> jobAdvertisements = jobAdvertisementRepository.findAll();
        return jobAdvertisements.stream().map(JobAdvertisementDto::toDto).collect(Collectors.toList());
    }

    public JobAdvertisementDto findById(JobAdvertisementId jobAdvertisementId) throws AggregateNotFoundException {
        JobAdvertisement jobAdvertisement = getJobAdvertisement(jobAdvertisementId);
        return JobAdvertisementDto.toDto(jobAdvertisement);
    }

    public void inspect(JobAdvertisementId jobAdvertisementId) {
        Condition.notNull(jobAdvertisementId, "JobAdvertisementId can't be null");
        LOG.debug("Starting inspect for JobAdvertisementId: {}", jobAdvertisementId.getValue());
        JobAdvertisement jobAdvertisement = getJobAdvertisement(jobAdvertisementId);
        jobAdvertisement.inspect();
        // FIXME Registration should be called by JOB_ADVERTISEMENT_INSPECTING (Create event listener in package messagebroker)
        //ravRegistrationService.registrate(jobAdvertisement);
    }

    public void approve(ApprovalDto approvalDto) {
        // TODO tbd where/when the data updates has to be done (over ApprovalDto --> JobAdUpdater?)
        Condition.notNull(approvalDto.getJobAdvertisementId(), "JobAdvertisementId can't be null");
        LOG.debug("Starting approve for JobAdvertisementId: {}", approvalDto.getJobAdvertisementId());
        JobAdvertisementId jobAdvertisementId = new JobAdvertisementId(approvalDto.getJobAdvertisementId());
        JobAdvertisement jobAdvertisement = getJobAdvertisement(jobAdvertisementId);
        jobAdvertisement.approve(approvalDto.getStellennummerAvam(), approvalDto.getDate(), approvalDto.isReportingObligation(), approvalDto.getReportingObligationEndDate());
    }

    public void reject(RejectionDto rejectionDto) {
        Condition.notNull(rejectionDto.getJobAdvertisementId(), "JobAdvertisementId can't be null");
        LOG.debug("Starting reject for JobAdvertisementId: {}", rejectionDto.getJobAdvertisementId());
        JobAdvertisementId jobAdvertisementId = new JobAdvertisementId(rejectionDto.getJobAdvertisementId());
        JobAdvertisement jobAdvertisement = getJobAdvertisement(jobAdvertisementId);
        jobAdvertisement.reject(rejectionDto.getStellennummerAvam(), rejectionDto.getDate(), rejectionDto.getCode(), rejectionDto.getReason());
    }

    public void refining(JobAdvertisementId jobAdvertisementId) {
        Condition.notNull(jobAdvertisementId, "JobAdvertisementId can't be null");
        LOG.debug("Starting refining for JobAdvertisementId: {}", jobAdvertisementId.getValue());
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
        LOG.debug("Starting publish for JobAdvertisementId: {}", jobAdvertisementId.getValue());
        JobAdvertisement jobAdvertisement = getJobAdvertisement(jobAdvertisementId);
        if (jobAdvertisement.isReportingObligation() && REFINING.equals(jobAdvertisement.getStatus())) {
            LOG.debug("Publish in restricted area for JobAdvertisementId: {}", jobAdvertisementId.getValue());
            jobAdvertisement.publishRestricted();
        } else {
            LOG.debug("Publish in public area for JobAdvertisementId: {}", jobAdvertisementId.getValue());
            jobAdvertisement.publishPublic();
        }
    }

    public void cancel(CancellationDto cancellationDto) {
        Condition.notNull(cancellationDto.getJobAdvertisementId(), "JobAdvertisementId can't be null");
        LOG.debug("Starting cancel for JobAdvertisementId: {}", cancellationDto.getJobAdvertisementId());
        JobAdvertisementId jobAdvertisementId = new JobAdvertisementId(cancellationDto.getJobAdvertisementId());
        JobAdvertisement jobAdvertisement = getJobAdvertisement(jobAdvertisementId);
        jobAdvertisement.cancel(cancellationDto.getDate(), cancellationDto.getCode());
    }

    public void archive(JobAdvertisementId jobAdvertisementId) {
        Condition.notNull(jobAdvertisementId, "JobAdvertisementId can't be null");
        LOG.debug("Starting archive for JobAdvertisementId: {}", jobAdvertisementId.getValue());
        JobAdvertisement jobAdvertisement = getJobAdvertisement(jobAdvertisementId);
        jobAdvertisement.archive();
    }

    private JobAdvertisement getJobAdvertisement(JobAdvertisementId jobAdvertisementId) throws AggregateNotFoundException {
        Optional<JobAdvertisement> jobAdvertisement = jobAdvertisementRepository.findById(jobAdvertisementId);
        //Optional<JobAdvertisement> jobAdvertisement = jobAdvertisementRepository.findOne(jobAdvertisementId);
        return jobAdvertisement.orElseThrow(() -> new AggregateNotFoundException(JobAdvertisement.class, jobAdvertisementId.getValue()));
    }

    private Occupation enrichOccupationWithProfessionCodes(Occupation occupation) {
        Condition.notNull(occupation, "Occupation can't be null");
        Profession profession = professionSerivce.findByAvamCode(occupation.getAvamOccupationCode());
        if (profession != null) {
            return new Occupation(
                    occupation.getAvamOccupationCode(),
                    profession.getSbn3Code(),
                    profession.getSbn5Code(),
                    profession.getBfsCode(),
                    profession.getLabel(),
                    occupation.getWorkExperience(),
                    occupation.getEducationCode()
            );
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
        return new Employment(
                jobApiDto.getStartDate(),
                jobApiDto.getEndDate(),
                jobApiDto.getDurationInDays(),
                jobApiDto.getStartsImmediately(),
                jobApiDto.getPermanent(),
                jobApiDto.getWorkingTimePercentageFrom(),
                jobApiDto.getWorkingTimePercentageTo()
        );
    }
    private Employment toEmployment(EmploymentDto employmentDto) {
        if (employmentDto != null) {
            return new Employment(
                    employmentDto.getStartDate(),
                    employmentDto.getEndDate(),
                    employmentDto.getDurationInDays(),
                    employmentDto.getImmediately(),
                    employmentDto.getPermanent(),
                    employmentDto.getWorkloadPercentageMin(),
                    employmentDto.getWorkloadPercentageMax()
            );
        }
        return null;
    }

    private ApplyChannel toApplyChannel(ApplyChannelDto applyChannelDto) {
        if (applyChannelDto != null) {
            return new ApplyChannel(
                    applyChannelDto.getMailAddress(),
                    applyChannelDto.getEmailAddress(),
                    applyChannelDto.getPhoneNumber(),
                    applyChannelDto.getFormUrl(),
                    applyChannelDto.getAdditionalInfo()
            );
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

    private Contact toContact(ContactDto contactDto) {
        if (contactDto != null) {
            return new Contact(
                    contactDto.getSalutation(),
                    contactDto.getFirstName(),
                    contactDto.getLastName(),
                    contactDto.getPhone(),
                    contactDto.getEmail(),
                    new Locale(contactDto.getLanguageIsoCode())
            );
        }
        return null;
    }

    private Location toLocation(CreateLocationDto createLocationDto) {
        if (createLocationDto != null) {
            return new Location(
                    createLocationDto.getRemarks(),
                    createLocationDto.getCity(),
                    createLocationDto.getPostalCode(),
                    null,
                    null,
                    null,
                    createLocationDto.getCountryIsoCode(),
                    null
            );
        }
        return null;
    }

    private Occupation toOccupation(OccupationDto occupationDto) {
        if (occupationDto != null) {
            return new Occupation(
                    occupationDto.getAvamOccupationCode(),
                    occupationDto.getWorkExperience(),
                    occupationDto.getEducationCode()
            );
        }
        return null;
    }

    private List<LanguageSkill> toLanguageSkills(List<LanguageSkillDto> languageSkillDtos) {
        if (languageSkillDtos != null) {
            return languageSkillDtos.stream()
                    .map(languageSkillDto -> new LanguageSkill(
                            languageSkillDto.getLanguageIsoCode(),
                            languageSkillDto.getSpokenLevel(),
                            languageSkillDto.getWrittenLevel()
                    ))
                    .collect(Collectors.toList());
        }
        return null;
    }
}
