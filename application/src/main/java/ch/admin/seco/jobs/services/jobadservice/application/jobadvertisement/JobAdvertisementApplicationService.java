package ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement;

import ch.admin.seco.jobs.services.jobadservice.application.JobCenterService;
import ch.admin.seco.jobs.services.jobadservice.application.LocationService;
import ch.admin.seco.jobs.services.jobadservice.application.ProfessionService;
import ch.admin.seco.jobs.services.jobadservice.application.ReportingObligationService;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.*;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.api.ApiCreateJobAdvertisementDto;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.create.CreateJobAdvertisementDto;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.create.CreateJobAdvertisementFromAvamDto;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.create.CreateJobAdvertisementFromX28Dto;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.create.CreateLocationDto;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.update.ApprovalDto;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.update.RejectionDto;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.update.UpdateJobAdvertisementFromAvamDto;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.update.UpdateJobAdvertisementFromX28Dto;
import ch.admin.seco.jobs.services.jobadservice.application.security.CurrentUser;
import ch.admin.seco.jobs.services.jobadservice.application.security.CurrentUserContext;
import ch.admin.seco.jobs.services.jobadservice.core.conditions.Condition;
import ch.admin.seco.jobs.services.jobadservice.core.domain.AggregateNotFoundException;
import ch.admin.seco.jobs.services.jobadservice.core.time.TimeMachine;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.*;
import ch.admin.seco.jobs.services.jobadservice.domain.profession.Profession;
import ch.admin.seco.jobs.services.jobadservice.domain.profession.ProfessionCodeType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisementStatus.INSPECTING;
import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisementStatus.REFINING;
import static java.util.stream.Collectors.toList;
import static org.springframework.util.StringUtils.hasText;

@Service
@Transactional(rollbackFor = {Exception.class})
public class JobAdvertisementApplicationService {

    public static final int PUBLICATION_MAX_DAYS = 60;

    public static final String COUNTRY_ISO_CODE_SWITZERLAND = "CH";

    private static Logger LOG = LoggerFactory.getLogger(JobAdvertisementApplicationService.class);

    private final CurrentUserContext currentUserContext;

    private final JobAdvertisementRepository jobAdvertisementRepository;

    private final JobAdvertisementFactory jobAdvertisementFactory;

    private final ReportingObligationService reportingObligationService;

    private final LocationService locationService;

    private final ProfessionService professionService;

    private final JobCenterService jobCenterService;

    @Autowired
    public JobAdvertisementApplicationService(CurrentUserContext currentUserContext,
                                              JobAdvertisementRepository jobAdvertisementRepository,
                                              JobAdvertisementFactory jobAdvertisementFactory,
                                              ReportingObligationService reportingObligationService,
                                              LocationService locationService,
                                              ProfessionService professionService,
                                              JobCenterService jobCenterService) {
        this.currentUserContext = currentUserContext;
        this.jobAdvertisementRepository = jobAdvertisementRepository;
        this.jobAdvertisementFactory = jobAdvertisementFactory;
        this.reportingObligationService = reportingObligationService;
        this.locationService = locationService;
        this.professionService = professionService;
        this.jobCenterService = jobCenterService;
    }

    public JobAdvertisementId createFromWebForm(CreateJobAdvertisementDto createJobAdvertisementDto) {
        LOG.debug("Start creating new job ad from WebForm");
        Condition.notNull(createJobAdvertisementDto, "CreateJobAdvertisementDto can't be null");
        LOG.debug("Create '{}'", createJobAdvertisementDto.getJobDescriptions().get(0).getTitle());

        final JobAdvertisementCreator creator = getJobAdvertisementCreatorFromInternal(createJobAdvertisementDto);
        JobAdvertisement jobAdvertisement = jobAdvertisementFactory.createFromWebForm(creator);
        return jobAdvertisement.getId();
    }

    public JobAdvertisementId createFromApi(CreateJobAdvertisementDto createJobAdvertisementDto) {
        LOG.debug("Start creating new job ad from API");
        Condition.notNull(createJobAdvertisementDto, "CreateJobAdvertisementDto can't be null");
        LOG.debug("Create '{}'", createJobAdvertisementDto.getJobDescriptions().get(0).getTitle());

        final JobAdvertisementCreator creator = getJobAdvertisementCreatorFromInternal(createJobAdvertisementDto);
        JobAdvertisement jobAdvertisement = jobAdvertisementFactory.createFromApi(creator);
        return jobAdvertisement.getId();
    }

    public JobAdvertisementId createFromAvam(CreateJobAdvertisementFromAvamDto createJobAdvertisementFromAvamDto) {
        LOG.debug("Start creating new job ad from AVAM");

        Condition.notNull(createJobAdvertisementFromAvamDto, "CreateJobAdvertisementFromAvamDto can't be null");

        Optional<JobAdvertisement> existingJobAdvertisement = jobAdvertisementRepository.findByStellennummerAvam(createJobAdvertisementFromAvamDto.getStellennummerAvam());
        if (existingJobAdvertisement.isPresent()) {
            JobAdvertisement jobAdvertisement = existingJobAdvertisement.get();
            LOG.debug("Update StellennummerAvam '{}' from AVAM", createJobAdvertisementFromAvamDto.getStellennummerAvam());
            jobAdvertisement.update(prepareUpdaterFromAvam(createJobAdvertisementFromAvamDto));
            return jobAdvertisement.getId();
        }

        LOG.debug("Create StellennummerAvam: {}", createJobAdvertisementFromAvamDto.getStellennummerAvam());
        checkifJobAdvertisementAlreadExists(createJobAdvertisementFromAvamDto);

        Condition.notNull(createJobAdvertisementFromAvamDto.getLocation(), "Location can't be null");
        Location location = toLocation(createJobAdvertisementFromAvamDto.getLocation());
        location = locationService.enrichCodes(location);

        Condition.notEmpty(createJobAdvertisementFromAvamDto.getOccupations(), "Occupations can't be empty");
        List<Occupation> occupations = enrichAndToOccupations(createJobAdvertisementFromAvamDto.getOccupations());

        JobContent jobContent = new JobContent.Builder()
                .setNumberOfJobs(createJobAdvertisementFromAvamDto.getNumberOfJobs())
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

        final JobAdvertisementCreator creator = new JobAdvertisementCreator.Builder(currentUserContext.getAuditUser())
                .setStellennummerAvam(createJobAdvertisementFromAvamDto.getStellennummerAvam())
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

    public JobAdvertisementId createFromX28(CreateJobAdvertisementFromX28Dto createJobAdvertisementFromX28Dto) {
        LOG.debug("Start creating new job ad from X28");

        Condition.notNull(createJobAdvertisementFromX28Dto, "CreateJobAdvertisementFromX28Dto can't be null");
        LOG.debug("Create '{}'", createJobAdvertisementFromX28Dto.getTitle());

        checkifJobAdvertisementAlreadExists(createJobAdvertisementFromX28Dto);

        Location location = toLocation(createJobAdvertisementFromX28Dto.getLocation());
        location = locationService.enrichCodes(location);

        List<Occupation> occupations = enrichAndToOccupations(createJobAdvertisementFromX28Dto.getOccupations());

        JobContent jobContent = new JobContent.Builder()
                .setNumberOfJobs(createJobAdvertisementFromX28Dto.getNumberOfJobs())
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
                .setX28OccupationCodes(createJobAdvertisementFromX28Dto.getProfessionCodes())
                .setEmployment(toEmployment(createJobAdvertisementFromX28Dto.getEmployment()))
                .setCompany(toCompany(createJobAdvertisementFromX28Dto.getCompany()))
                .setPublicContact(toPublicContact(createJobAdvertisementFromX28Dto.getContact()))
                .setLanguageSkills(toLanguageSkills(createJobAdvertisementFromX28Dto.getLanguageSkills()))
                .build();

        LocalDate publicationStartDate = createJobAdvertisementFromX28Dto.getPublicationStartDate();
        if (publicationStartDate == null) {
            publicationStartDate = TimeMachine.now().toLocalDate();
        }
        LocalDate publicationEndDate = createJobAdvertisementFromX28Dto.getPublicationEndDate();
        if (publicationEndDate == null) {
            publicationEndDate = publicationStartDate.plusDays(PUBLICATION_MAX_DAYS);
        }
        final JobAdvertisementCreator creator = new JobAdvertisementCreator.Builder(currentUserContext.getAuditUser())
                .setStellennummerEgov(createJobAdvertisementFromX28Dto.getStellennummerEgov())
                .setStellennummerAvam(createJobAdvertisementFromX28Dto.getStellennummerAvam())
                .setFingerprint(createJobAdvertisementFromX28Dto.getFingerprint())
                .setJobContent(jobContent)
                .setContact(toContact(createJobAdvertisementFromX28Dto.getContact()))
                .setPublication(
                        new Publication.Builder()
                                .setStartDate(publicationStartDate)
                                .setEndDate(publicationEndDate)
                                .setEuresDisplay(false)
                                .setEuresAnonymous(false)
                                .setPublicDisplay(true)
                                .setPublicAnonymous(true)
                                .build()
                )
                .build();

        JobAdvertisement jobAdvertisement = jobAdvertisementFactory.createFromExtern(creator);
        return jobAdvertisement.getId();
    }

    public JobAdvertisementId updateFromX28(UpdateJobAdvertisementFromX28Dto updateJobAdvertisementFromX28Dto) {
        LOG.debug("Update StellennummerEgov '{}' from X28", updateJobAdvertisementFromX28Dto.getStellennummerEgov());

        final String stellennummerEgov = updateJobAdvertisementFromX28Dto.getStellennummerEgov();
        JobAdvertisement jobAdvertisement = jobAdvertisementRepository.findByStellennummerEgov(stellennummerEgov)
                .orElseThrow(() -> new EntityNotFoundException("JobAdvertisement not found. stellennummerEgov: " + stellennummerEgov));

        JobAdvertisementUpdater updater = new JobAdvertisementUpdater.Builder(currentUserContext.getAuditUser())
                .setFingerprint(updateJobAdvertisementFromX28Dto.getFingerprint())
                .setX28OccupationCodes(updateJobAdvertisementFromX28Dto.getX28OccupationCodes())
                .build();

        jobAdvertisement.update(updater);
        return jobAdvertisement.getId();
    }

    public List<JobAdvertisementDto> findAll() {
        List<JobAdvertisement> jobAdvertisements = jobAdvertisementRepository.findAll();
        return jobAdvertisements.stream().map(JobAdvertisementDto::toDto).collect(toList());
    }

    public Page<JobAdvertisementDto> findOwnJobAdvertisements(Pageable pageable) {
        CurrentUser currentUser = currentUserContext.getCurrentUser();
        if (currentUser == null) {
            return new PageImpl<>(Collections.EMPTY_LIST, pageable, 0);
        }
        Page<JobAdvertisement> jobAdvertisements = jobAdvertisementRepository.findOwnJobAdvertisements(pageable, currentUser.getUserId(), currentUser.getCompanyId());
        return new PageImpl<>(
                jobAdvertisements.getContent().stream().map(JobAdvertisementDto::toDto).collect(toList()),
                jobAdvertisements.getPageable(),
                jobAdvertisements.getTotalElements()
        );
    }

    public Page<JobAdvertisementDto> findAllPaginated(Pageable pageable) {
        Page<JobAdvertisement> jobAdvertisements = jobAdvertisementRepository.findAll(pageable);
        return new PageImpl<>(
                jobAdvertisements.getContent().stream().map(JobAdvertisementDto::toDto).collect(toList()),
                jobAdvertisements.getPageable(),
                jobAdvertisements.getTotalElements()
        );
    }

    public JobAdvertisementDto getById(JobAdvertisementId jobAdvertisementId) throws AggregateNotFoundException {
        JobAdvertisement jobAdvertisement = getJobAdvertisement(jobAdvertisementId);
        return JobAdvertisementDto.toDto(jobAdvertisement);
    }

    public JobAdvertisementDto getByAccessToken(String accessToken) {
        JobAdvertisement jobAdvertisement = getJobAdvertisementByAccessToken(accessToken);
        return JobAdvertisementDto.toDto(jobAdvertisement);
    }

    public JobAdvertisementDto findByStellennummerAvam(String stellennummerAvam) {
        Optional<JobAdvertisement> jobAdvertisement = jobAdvertisementRepository.findByStellennummerAvam(stellennummerAvam);
        return jobAdvertisement.map(JobAdvertisementDto::toDto).orElse(null);
    }

    public JobAdvertisementDto getByStellennummerAvam(String stellennummerAvam) {
        JobAdvertisement jobAdvertisement = getJobAdvertisementByStellennummerAvam(stellennummerAvam);
        return JobAdvertisementDto.toDto(jobAdvertisement);
    }

    public JobAdvertisementDto findByStellennummerEgov(String stellennummerEgov) {
        Optional<JobAdvertisement> jobAdvertisement = jobAdvertisementRepository.findByStellennummerEgov(stellennummerEgov);
        return jobAdvertisement.map(JobAdvertisementDto::toDto).orElse(null);
    }

    public JobAdvertisementDto getByStellennummerEgov(String stellennummerEgov) {
        JobAdvertisement jobAdvertisement = getJobAdvertisementByStellennummerEgov(stellennummerEgov);
        return JobAdvertisementDto.toDto(jobAdvertisement);
    }

    public JobAdvertisementDto getByFingerprint(String fingerprint) {
        final Optional<JobAdvertisement> jobAdvertisement = jobAdvertisementRepository.findByFingerprint(fingerprint);
        return jobAdvertisement.map(JobAdvertisementDto::toDto)
                .orElseThrow(() -> new AggregateNotFoundException(JobAdvertisement.class, AggregateNotFoundException.IndentifierType.EXTERNAL_ID, fingerprint));
    }

    public void inspect(JobAdvertisementId jobAdvertisementId) {
        Condition.notNull(jobAdvertisementId, "JobAdvertisementId can't be null");
        LOG.debug("Starting inspect for JobAdvertisementId: '{}'", jobAdvertisementId.getValue());
        JobAdvertisement jobAdvertisement = getJobAdvertisement(jobAdvertisementId);
        jobAdvertisement.inspect();
    }

    public void approve(ApprovalDto approvalDto) {
        Condition.notNull(approvalDto.getStellennummerEgov(), "StellennummerEgov can't be null");
        JobAdvertisement jobAdvertisement = getJobAdvertisementByStellennummerEgov(approvalDto.getStellennummerEgov());
        LOG.debug("Starting approve for JobAdvertisementId: '{}'", jobAdvertisement.getId().getValue());
        // FIXME This is a workaround when updating after approved, until AVAM add an actionType on there message.
        if (jobAdvertisement.getStatus().equals(INSPECTING)) {
            jobAdvertisement.approve(approvalDto.getStellennummerAvam(), approvalDto.getDate(), approvalDto.isReportingObligation(), approvalDto.getReportingObligationEndDate());
        }
        UpdateJobAdvertisementFromAvamDto updateJobAdvertisement = approvalDto.getUpdateJobAdvertisement();
        jobAdvertisement.update(prepareUpdaterFromAvam(updateJobAdvertisement));
    }

    private JobAdvertisementUpdater prepareUpdaterFromAvam(CreateJobAdvertisementFromAvamDto createJobAdvertisement) {
        Condition.notNull(createJobAdvertisement.getLocation(), "Location can't be null");
        Location location = toLocation(createJobAdvertisement.getLocation());
        location = locationService.enrichCodes(location);

        List<OccupationDto> occupationDtos = createJobAdvertisement.getOccupations();
        Condition.notEmpty(occupationDtos, "Occupations can't be empty");
        List<Occupation> occupations = enrichAndToOccupations(occupationDtos);

        return new JobAdvertisementUpdater.Builder(currentUserContext.getAuditUser())
                .setNumberOfJobs(createJobAdvertisement.getNumberOfJobs())
                .setJobDescription(createJobAdvertisement.getTitle(), createJobAdvertisement.getDescription())
                .setReportingObligation(createJobAdvertisement.isReportingObligation(), createJobAdvertisement.getReportingObligationEndDate())
                .setJobCenterCode(createJobAdvertisement.getJobCenterCode())
                .setCompany(toCompany(createJobAdvertisement.getCompany()))
                .setEmployment(toEmployment(createJobAdvertisement.getEmployment()))
                .setLocation(location)
                .setOccupations(occupations)
                .setLanguageSkills(toLanguageSkills(createJobAdvertisement.getLanguageSkills()))
                .setApplyChannel(toApplyChannel(createJobAdvertisement.getApplyChannel()))
                .setContact(toContact(createJobAdvertisement.getContact()))
                .setPublication(toPublication(createJobAdvertisement.getPublication()))
                .build();
    }

    private JobAdvertisementUpdater prepareUpdaterFromAvam(UpdateJobAdvertisementFromAvamDto updateJobAdvertisement) {
        Condition.notNull(updateJobAdvertisement.getLocation(), "Location can't be null");
        Location location = toLocation(updateJobAdvertisement.getLocation());
        location = locationService.enrichCodes(location);

        List<OccupationDto> occupationDtos = updateJobAdvertisement.getOccupations();
        Condition.notEmpty(occupationDtos, "Occupations can't be empty");
        List<Occupation> occupations = enrichAndToOccupations(occupationDtos);

        return new JobAdvertisementUpdater.Builder(currentUserContext.getAuditUser())
                .setNumberOfJobs(updateJobAdvertisement.getNumberOfJobs())
                .setJobDescription(updateJobAdvertisement.getTitle(), updateJobAdvertisement.getDescription())
                .setJobCenterCode(updateJobAdvertisement.getJobCenterCode())
                .setCompany(toCompany(updateJobAdvertisement.getCompany()))
                .setEmployment(toEmployment(updateJobAdvertisement.getEmployment()))
                .setLocation(location)
                .setOccupations(occupations)
                .setLanguageSkills(toLanguageSkills(updateJobAdvertisement.getLanguageSkills()))
                .setApplyChannel(toApplyChannel(updateJobAdvertisement.getApplyChannel()))
                .setContact(toContact(updateJobAdvertisement.getContact()))
                .setPublication(toPublication(updateJobAdvertisement.getPublication()))
                .build();
    }

    private List<Occupation> enrichAndToOccupations(List<OccupationDto> occupationDtos) {
        return occupationDtos.stream()
                .map(this::toOccupation)
                .map(this::enrichOccupationWithProfessionCodes)
                .collect(toList());
    }

    public void reject(RejectionDto rejectionDto) {
        Condition.notNull(rejectionDto.getStellennummerEgov(), "StellennummerEgov can't be null");
        JobAdvertisement jobAdvertisement = getJobAdvertisementByStellennummerEgov(rejectionDto.getStellennummerEgov());
        LOG.debug("Starting reject for JobAdvertisementId: '{}'", jobAdvertisement.getId().getValue());
        jobAdvertisement.reject(rejectionDto.getStellennummerAvam(), rejectionDto.getDate(), rejectionDto.getCode(), rejectionDto.getReason());
    }

    // FIXME @PreAuthorize("@jobAdvertisementAuthorizationService.canCancel(jobAdvertisementId, token)")
    public void cancel(JobAdvertisementId jobAdvertisementId, LocalDate date, CancellationCode cancellationCode, String token) {
        Condition.notNull(jobAdvertisementId, "JobAdvertisementId can't be null");
        JobAdvertisement jobAdvertisement = getJobAdvertisement(jobAdvertisementId);
        LOG.debug("Starting cancel for JobAdvertisementId: '{}'", jobAdvertisement.getId().getValue());
        jobAdvertisement.cancel(date, cancellationCode);
    }

    public void refining(JobAdvertisementId jobAdvertisementId) {
        Condition.notNull(jobAdvertisementId, "JobAdvertisementId can't be null");
        LOG.debug("Starting refining for JobAdvertisementId: '{}'", jobAdvertisementId.getValue());
        JobAdvertisement jobAdvertisement = getJobAdvertisement(jobAdvertisementId);
        jobAdvertisement.refining();
    }

    public void publish(JobAdvertisementId jobAdvertisementId) {
        Condition.notNull(jobAdvertisementId, "JobAdvertisementId can't be null");
        LOG.debug("Starting publish for JobAdvertisementId: '{}'", jobAdvertisementId.getValue());
        JobAdvertisement jobAdvertisement = getJobAdvertisement(jobAdvertisementId);
        publish(jobAdvertisement);
    }

    private void publish(JobAdvertisement jobAdvertisement) {
        Condition.notNull(jobAdvertisement, "JobAdvertisement can't be null");
        if (jobAdvertisement.isReportingObligation() && REFINING.equals(jobAdvertisement.getStatus())) {
            LOG.debug("Publish in restricted area for JobAdvertisementId: '{}'", jobAdvertisement.getId().getValue());
            jobAdvertisement.publishRestricted();
        } else {
            LOG.debug("Publish in public area for JobAdvertisementId: '{}'", jobAdvertisement.getId().getValue());
            jobAdvertisement.publishPublic();
        }
    }

    public void archive(JobAdvertisementId jobAdvertisementId) {
        Condition.notNull(jobAdvertisementId, "JobAdvertisementId can't be null");
        LOG.debug("Starting archive for JobAdvertisementId: '{}'", jobAdvertisementId.getValue());
        JobAdvertisement jobAdvertisement = getJobAdvertisement(jobAdvertisementId);
        jobAdvertisement.archive();
    }

    @Scheduled(cron = "${jobAdvertisement.checkPublicationStarts.cron}")
    public void scheduledCheckPublicationStarts() {
        this.jobAdvertisementRepository
                .findAllWherePublicationShouldStart(TimeMachine.now().toLocalDate())
                .forEach(this::publish);

    }

    @Scheduled(cron = "${jobAdvertisement.checkBlackoutPolicyExpiration.cron}")
    public void scheduledCheckBlackoutPolicyExpiration() {
        this.jobAdvertisementRepository
                .findAllWhereBlackoutNeedToExpire(TimeMachine.now().toLocalDate())
                .forEach(JobAdvertisement::expireBlackout);

    }

    @Scheduled(cron = "${jobAdvertisement.checkPublicationExpiration.cron}")
    public void scheduledCheckPublicationExpiration() {
        this.jobAdvertisementRepository
                .findAllWherePublicationNeedToExpire(TimeMachine.now().toLocalDate())
                .forEach(JobAdvertisement::expirePublication);

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
        Condition.notNull(createJobAdvertisementDto.getLocation(), "Location can't be null");
        Location location = toLocation(createJobAdvertisementDto.getLocation());
        location = locationService.enrichCodes(location);

        Condition.notNull(createJobAdvertisementDto.getOccupation(), "Occupation can't be null");

        Occupation occupation = toOccupation(createJobAdvertisementDto.getOccupation());
        occupation = enrichOccupationWithProfessionCodes(occupation);
        List<Occupation> occupations = Collections.singletonList(occupation);

        boolean reportingObligation = checkReportingObligation(
                occupation,
                location
        );

        String jobCenterCode = jobCenterService.findJobCenterCode(location.getCountryIsoCode(), location.getPostalCode());

        JobContent jobContent = new JobContent.Builder()
                .setNumberOfJobs(createJobAdvertisementDto.getNumberOfJobs())
                .setExternalUrl(createJobAdvertisementDto.getExternalUrl())
                .setJobDescriptions(toJobDescriptions(createJobAdvertisementDto.getJobDescriptions()))
                .setLocation(location)
                .setOccupations(occupations)
                .setEmployment(toEmployment(createJobAdvertisementDto.getEmployment()))
                .setApplyChannel(toApplyChannel(createJobAdvertisementDto.getApplyChannel()))
                .setCompany(toCompany(createJobAdvertisementDto.getCompany()))
                .setEmployer(toEmployer(createJobAdvertisementDto.getEmployer()))
                .setPublicContact(toPublicContact(createJobAdvertisementDto.getPublicContact()))
                .setLanguageSkills(toLanguageSkills(createJobAdvertisementDto.getLanguageSkills()))
                .build();

        PublicationDto publicationDto = createJobAdvertisementDto.getPublication();

        return new JobAdvertisementCreator.Builder(currentUserContext.getAuditUser())
                .setExternalReference(createJobAdvertisementDto.getExternalReference())
                .setReportToAvam(createJobAdvertisementDto.isReportToAvam())
                .setReportingObligation(reportingObligation)
                .setJobCenterCode(jobCenterCode)
                .setJobContent(jobContent)
                .setContact(toContact(createJobAdvertisementDto.getContact()))
                .setPublication(
                        new Publication.Builder()
                                .setStartDate(publicationDto.getStartDate())
                                .setEndDate((publicationDto.getEndDate() != null) ? publicationDto.getEndDate() : TimeMachine.now().plusDays(PUBLICATION_MAX_DAYS).toLocalDate())
                                .setEuresDisplay(publicationDto.isEuresDisplay())
                                .setEuresAnonymous(publicationDto.isEuresAnonymous())
                                .setRestrictedDisplay(true)
                                .setRestrictedAnonymous(true)
                                .setPublicDisplay(publicationDto.isPublicDisplay())
                                .setPublicAnonymous(publicationDto.isPublicAnonymous())
                                .build()
                )
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

    private JobAdvertisement getJobAdvertisementByAccessToken(String accessToken) {
        Optional<JobAdvertisement> jobAdvertisement = jobAdvertisementRepository.findOneByAccessToken(accessToken);
        return jobAdvertisement.orElseThrow(() -> new AggregateNotFoundException(JobAdvertisement.class, AggregateNotFoundException.IndentifierType.ACCESS_TOKEN, accessToken));
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
        if (COUNTRY_ISO_CODE_SWITZERLAND.equals(location.getCountryIsoCode())) {
            return reportingObligationService.hasReportingObligation(ProfessionCodeType.AVAM, avamOccupationCode, cantonCode);
        }
        return false;
    }

    private List<JobDescription> toJobDescriptions(List<JobDescriptionDto> jobDescriptionDtos) {
        if (jobDescriptionDtos != null) {
            return jobDescriptionDtos.stream()
                    .map(jobDescriptionDto -> new JobDescription.Builder()
                            .setLanguage(hasText(jobDescriptionDto.getLanguageIsoCode()) ? new Locale(jobDescriptionDto.getLanguageIsoCode()) : Locale.GERMAN)
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
                    .setEuresDisplay(publicationDto.isEuresDisplay())
                    .setEuresAnonymous(publicationDto.isEuresAnonymous())
                    .setPublicDisplay(publicationDto.isPublicDisplay())
                    .setPublicAnonymous(publicationDto.isPublicAnonymous())
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
                    .setShortEmployment(employmentDto.isShortEmployment())
                    .setImmediately(employmentDto.isImmediately())
                    .setPermanent(employmentDto.isPermanent())
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
                    .setSurrogate(companyDto.isSurrogate())
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

    private PublicContact toPublicContact(PublicContactDto publicContactDto) {
        if (publicContactDto != null) {
            return new PublicContact.Builder()
                    .setSalutation(publicContactDto.getSalutation())
                    .setFirstName(publicContactDto.getFirstName())
                    .setLastName(publicContactDto.getLastName())
                    .setPhone(publicContactDto.getPhone())
                    .setEmail(publicContactDto.getEmail())
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
            String countryIsoCode = Optional.ofNullable(createLocationDto.getCountryIsoCode())
                    .orElse(COUNTRY_ISO_CODE_SWITZERLAND);
            return new Location.Builder()
                    .setRemarks(createLocationDto.getRemarks())
                    .setCity(createLocationDto.getCity())
                    .setPostalCode(createLocationDto.getPostalCode())
                    .setCountryIsoCode(countryIsoCode)
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
