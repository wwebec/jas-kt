package ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement;

import ch.admin.seco.jobs.services.jobadservice.application.LocalityService;
import ch.admin.seco.jobs.services.jobadservice.application.RavRegistrationService;
import ch.admin.seco.jobs.services.jobadservice.application.ReportingObligationService;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.*;
import ch.admin.seco.jobs.services.jobadservice.application.profession.ProfessionApplicationService;
import ch.admin.seco.jobs.services.jobadservice.core.conditions.Condition;
import ch.admin.seco.jobs.services.jobadservice.core.domain.AggregateNotFoundException;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.*;
import ch.admin.seco.jobs.services.jobadservice.domain.profession.ProfessionCodeType;
import ch.admin.seco.jobs.services.jobadservice.domain.profession.ProfessionId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisementStatus.REFINING;

@Service
@Transactional(rollbackFor = {Exception.class})
public class JobAdvertisementApplicationService {

    private final JobAdvertisementRepository jobAdvertisementRepository;

    private final JobAdvertisementFactory jobAdvertisementFactory;

    private final ProfessionApplicationService professionApplicationService;

    private final RavRegistrationService ravRegistrationService;

    private final ReportingObligationService reportingObligationService;

    private final LocalityService localityService;

    @Autowired
    public JobAdvertisementApplicationService(JobAdvertisementRepository jobAdvertisementRepository, JobAdvertisementFactory jobAdvertisementFactory, ProfessionApplicationService professionApplicationService, RavRegistrationService ravRegistrationService, ReportingObligationService reportingObligationService, LocalityService localityService) {
        this.jobAdvertisementRepository = jobAdvertisementRepository;
        this.jobAdvertisementFactory = jobAdvertisementFactory;
        this.professionApplicationService = professionApplicationService;
        this.ravRegistrationService = ravRegistrationService;
        this.reportingObligationService = reportingObligationService;
        this.localityService = localityService;
    }

    public JobAdvertisementId createFromWebForm(CreateJobAdvertisementWebformDto createJobAdvertisementWebformDto) {
        boolean reportingObligation = checkReportingObligation(
                createJobAdvertisementWebformDto.getLocalities(),
                Collections.singletonList(createJobAdvertisementWebformDto.getOccupation())
        );
        final JobAdvertisementUpdater updater = new JobAdvertisementUpdater.Builder(null)
                .setReportingObligation(reportingObligation)
                // FIXME eval eures if anonymous or not
                .setEures(createJobAdvertisementWebformDto.isEures(), true)
                .setEmployment(
                        createJobAdvertisementWebformDto.getEmploymentStartDate(),
                        createJobAdvertisementWebformDto.getEmploymentEndDate(),
                        createJobAdvertisementWebformDto.getDurationInDays(),
                        createJobAdvertisementWebformDto.getImmediately(),
                        createJobAdvertisementWebformDto.getPermanent(),
                        createJobAdvertisementWebformDto.getWorkloadPercentageMin(),
                        createJobAdvertisementWebformDto.getWorkloadPercentageMax()
                )
                .setApplyChannel(toApplyChannel(createJobAdvertisementWebformDto.getApplyChannel()))
                .setCompany(toCompany(createJobAdvertisementWebformDto.getCompany()))
                .setContact(toContact(createJobAdvertisementWebformDto.getContact()))
                .setLocalities(toLocalities(createJobAdvertisementWebformDto.getLocalities()))
                .setOccupations(toOccupations(Collections.singletonList(createJobAdvertisementWebformDto.getOccupation())))
                .setEducationCode(createJobAdvertisementWebformDto.getEducationCode())
                .setLanguageSkills(toLanguageSkills(createJobAdvertisementWebformDto.getLanguageSkills()))
                .build();

        JobAdvertisement jobAdvertisement = jobAdvertisementFactory.createFromWebForm(
                createJobAdvertisementWebformDto.getTitle(),
                createJobAdvertisementWebformDto.getDescription(),
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
        ravRegistrationService.registrate(jobAdvertisement);
        jobAdvertisement.inspect();
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

    private boolean checkReportingObligation(List<LocalityDto> localityDtos, List<OccupationDto> occupationDtos) {
        // FIXME Pseudo code to real implementation
        String cantonCode = localityDtos.get(0).getCantonCode();
        ProfessionId professionId = new ProfessionId(occupationDtos.get(0).getProfessionId());
        String avamProfessionCode = professionApplicationService.findAvamCode(professionId);
        return reportingObligationService.hasReportingObligation(ProfessionCodeType.AVAM, avamProfessionCode, cantonCode);
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
                    .setZipCode(companyDto.getZipCode())
                    .setCity(companyDto.getCity())
                    .setCountryIsoCode(companyDto.getCountryIsoCode())
                    .setPostOfficeBoxNumber(companyDto.getPostOfficeBoxNumber())
                    .setPostOfficeBoxZipCode(companyDto.getPostOfficeBoxZipCode())
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
                    Salutation.valueOf(contactDto.getSalutation()),
                    contactDto.getFirstName(),
                    contactDto.getLastName(),
                    contactDto.getPhone(),
                    contactDto.getEmail()
            );
        }
        return null;
    }

    private List<Locality> toLocalities(List<LocalityDto> localityDtos) {
        if (localityDtos != null) {
            return localityDtos.stream()
                    .map(localityDto -> localityService.enrichCodes(
                            new Locality(
                                    localityDto.getRemarks(),
                                    localityDto.getCity(),
                                    localityDto.getZipCode(),
                                    localityDto.getCommunalCode(),
                                    localityDto.getRegionCode(),
                                    localityDto.getCantonCode(),
                                    localityDto.getCountryIsoCode(),
                                    localityDto.getLocation()
                            )
                    ))
                    .collect(Collectors.toList());
        }
        return null;
    }

    private List<Occupation> toOccupations(List<OccupationDto> occupationDtos) {
        if (occupationDtos != null) {
            // TODO update professionCodes
            return occupationDtos.stream()
                    .map(occupationDto -> new Occupation(
                            new ProfessionId(occupationDto.getProfessionId()),
                            occupationDto.getWorkExperience()
                    ))
                    .collect(Collectors.toList());
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
