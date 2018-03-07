package ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement;

import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisementStatus.REFINING;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ch.admin.seco.jobs.services.jobadservice.application.LocalityService;
import ch.admin.seco.jobs.services.jobadservice.application.ProfessionService;
import ch.admin.seco.jobs.services.jobadservice.application.RavRegistrationService;
import ch.admin.seco.jobs.services.jobadservice.application.ReportingObligationService;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.ApplyChannelDto;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.ApprovalDto;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.CancellationDto;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.CompanyDto;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.ContactDto;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.CreateJobAdvertisementWebFormDto;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.EmploymentDto;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.JobAdvertisementDto;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.LanguageSkillDto;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.LocalityDto;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.OccupationDto;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.RejectionDto;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.api.ApiDtoConverter;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.api.CreateJobAdvertisementApiDto;
import ch.admin.seco.jobs.services.jobadservice.core.conditions.Condition;
import ch.admin.seco.jobs.services.jobadservice.core.domain.AggregateNotFoundException;
import ch.admin.seco.jobs.services.jobadservice.core.time.TimeMachine;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.ApplyChannel;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.Company;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.Contact;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.Employment;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisement;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisementFactory;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisementId;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisementRepository;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisementUpdater;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.LanguageSkill;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.Locality;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.Occupation;
import ch.admin.seco.jobs.services.jobadservice.domain.profession.Profession;
import ch.admin.seco.jobs.services.jobadservice.domain.profession.ProfessionCodeType;

@Service
@Transactional(rollbackFor = {Exception.class})
public class JobAdvertisementApplicationService {

    private final JobAdvertisementRepository jobAdvertisementRepository;

    private final JobAdvertisementFactory jobAdvertisementFactory;

    private final RavRegistrationService ravRegistrationService;

    private final ReportingObligationService reportingObligationService;

    private final LocalityService localityService;

    private final ProfessionService professionSerivce;

    @Autowired
    public JobAdvertisementApplicationService(JobAdvertisementRepository jobAdvertisementRepository,
            JobAdvertisementFactory jobAdvertisementFactory,
            RavRegistrationService ravRegistrationService,
            ReportingObligationService reportingObligationService,
            LocalityService localityService,
            ProfessionService professionSerivce) {
        this.jobAdvertisementRepository = jobAdvertisementRepository;
        this.jobAdvertisementFactory = jobAdvertisementFactory;
        this.ravRegistrationService = ravRegistrationService;
        this.reportingObligationService = reportingObligationService;
        this.localityService = localityService;
        this.professionSerivce = professionSerivce;
    }

    public JobAdvertisementId createFromWebForm(CreateJobAdvertisementWebFormDto createJobAdvertisementWebFormDto) {
        Locality locality = toLocality(createJobAdvertisementWebFormDto.getLocality());
        locality = localityService.enrichCodes(locality);

        Occupation occupation = toOccupation(createJobAdvertisementWebFormDto.getOccupation());
        occupation = enrichOccupationWithProfessionCodes(occupation);

        boolean reportingObligation = checkReportingObligation(
                occupation,
                locality
        );

        final JobAdvertisementUpdater updater = new JobAdvertisementUpdater.Builder(null)
                .setLocality(locality)
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
                createJobAdvertisementWebFormDto.getTitle(),
                createJobAdvertisementWebFormDto.getDescription(),
                updater
        );
        return jobAdvertisement.getId();
    }

    public JobAdvertisementId createFromApi(CreateJobAdvertisementApiDto createJobAdvertisementApiDto) {
        Locality locality = ApiDtoConverter.toLocality(createJobAdvertisementApiDto.getJob().getLocation());
        locality = localityService.enrichCodes(locality);

        Occupation occupation = toOccupation(createJobAdvertisementApiDto.getOccupation());
        occupation = enrichOccupationWithProfessionCodes(occupation);

        boolean reportingObligation = checkReportingObligation(
                occupation,
                locality
        );

        final JobAdvertisementUpdater updater = new JobAdvertisementUpdater.Builder(null)
                .setLocality(locality)
                .setOccupations(Collections.singletonList(occupation))
                .setReportingObligation(reportingObligation)
                .setEmployment(ApiDtoConverter.toEmployment(createJobAdvertisementApiDto.getJob()))
                .setApplyChannel(toApplyChannel(createJobAdvertisementApiDto.getApplyChannel()))
                .setCompany(toCompany(createJobAdvertisementApiDto.getCompany()))
                .setContact(toContact(createJobAdvertisementApiDto.getContact()))
                .setLanguageSkills(ApiDtoConverter.toLanguageSkills(createJobAdvertisementApiDto.getJob().getLanguageSkills()))
                .build();

        JobAdvertisement jobAdvertisement = jobAdvertisementFactory.createFromApi(
                createJobAdvertisementApiDto.getJob().getTitle(),
                createJobAdvertisementApiDto.getJob().getDescription(),
                updater
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
        Condition.notNull(jobAdvertisementId, "JobAdvertisementId should not be null");
        JobAdvertisement jobAdvertisement = getJobAdvertisement(jobAdvertisementId);
        jobAdvertisement.inspect();
        // FIXME Registration should be called by JOB_ADVERTISEMENT_INSPECTING (Create event listener in package messagebroker)
        //ravRegistrationService.registrate(jobAdvertisement);
    }

    public void approve(ApprovalDto approvalDto) {
        // TODO tbd where/when the data updates has to be done (over ApprovalDto --> JobAdUpdater?)
        Condition.notNull(approvalDto.getJobAdvertisementId(), "JobAdvertisementId should not be null");
        JobAdvertisementId jobAdvertisementId = new JobAdvertisementId(approvalDto.getJobAdvertisementId());
        JobAdvertisement jobAdvertisement = getJobAdvertisement(jobAdvertisementId);
        jobAdvertisement.approve(approvalDto.getStellennummerAvam(), approvalDto.getDate(), approvalDto.isReportingObligation(), approvalDto.getReportingObligationEndDate());
    }

    public void reject(RejectionDto rejectionDto) {
        Condition.notNull(rejectionDto.getJobAdvertisementId(), "JobAdvertisementId should not be null");
        JobAdvertisementId jobAdvertisementId = new JobAdvertisementId(rejectionDto.getJobAdvertisementId());
        JobAdvertisement jobAdvertisement = getJobAdvertisement(jobAdvertisementId);
        jobAdvertisement.reject(rejectionDto.getStellennummerAvam(), rejectionDto.getDate(), rejectionDto.getCode(), rejectionDto.getReason());
    }

    public void refining(JobAdvertisementId jobAdvertisementId) {
        Condition.notNull(jobAdvertisementId, "JobAdvertisementId should not be null");
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
        Condition.notNull(jobAdvertisementId, "JobAdvertisementId should not be null");
        JobAdvertisement jobAdvertisement = getJobAdvertisement(jobAdvertisementId);
        if (jobAdvertisement.isReportingObligation() && REFINING.equals(jobAdvertisement.getStatus())) {
            jobAdvertisement.publishRestricted();
        } else {
            jobAdvertisement.publishPublic();
        }
    }

    public void cancel(CancellationDto cancellationDto) {
        Condition.notNull(cancellationDto.getJobAdvertisementId(), "JobAdvertisementId should not be null");
        JobAdvertisementId jobAdvertisementId = new JobAdvertisementId(cancellationDto.getJobAdvertisementId());
        JobAdvertisement jobAdvertisement = getJobAdvertisement(jobAdvertisementId);
        jobAdvertisement.cancel(cancellationDto.getDate(), cancellationDto.getCode());
    }

    public void archive(JobAdvertisementId jobAdvertisementId) {
        Condition.notNull(jobAdvertisementId, "JobAdvertisementId should not be null");
        JobAdvertisement jobAdvertisement = getJobAdvertisement(jobAdvertisementId);
        jobAdvertisement.archive();
    }

    private JobAdvertisement getJobAdvertisement(JobAdvertisementId jobAdvertisementId) throws AggregateNotFoundException {
        Optional<JobAdvertisement> jobAdvertisement = jobAdvertisementRepository.findById(jobAdvertisementId);
        return jobAdvertisement.orElseThrow(() -> new AggregateNotFoundException(JobAdvertisement.class, jobAdvertisementId.getValue()));
    }

    private Occupation enrichOccupationWithProfessionCodes(Occupation occupation) {
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

    private boolean checkReportingObligation(Occupation occupation, Locality locality) {
        String avamOccupationCode = occupation.getAvamOccupationCode();
        String cantonCode = locality.getCantonCode();
        return (cantonCode != null) && reportingObligationService.hasReportingObligation(ProfessionCodeType.AVAM, avamOccupationCode, cantonCode);
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
                    contactDto.getEmail()
            );
        }
        return null;
    }

    private Locality toLocality(LocalityDto localityDto) {
        if (localityDto != null) {
            return new Locality(
                    localityDto.getRemarks(),
                    localityDto.getCity(),
                    localityDto.getPostalCode(),
                    localityDto.getCommunalCode(),
                    localityDto.getRegionCode(),
                    localityDto.getCantonCode(),
                    localityDto.getCountryIsoCode(),
                    localityDto.getLocation()
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
