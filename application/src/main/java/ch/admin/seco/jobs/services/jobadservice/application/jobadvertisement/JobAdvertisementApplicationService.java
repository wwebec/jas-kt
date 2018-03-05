package ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement;

import ch.admin.seco.jobs.services.jobadservice.application.LocalityService;
import ch.admin.seco.jobs.services.jobadservice.application.ProfessionService;
import ch.admin.seco.jobs.services.jobadservice.application.RavRegistrationService;
import ch.admin.seco.jobs.services.jobadservice.application.ReportingObligationService;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.*;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.api.CreateJobAdvertisementApiDto;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.api.JobDto;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.api.LocationDto;
import ch.admin.seco.jobs.services.jobadservice.core.conditions.Condition;
import ch.admin.seco.jobs.services.jobadservice.core.domain.AggregateNotFoundException;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.*;
import ch.admin.seco.jobs.services.jobadservice.domain.profession.Profession;
import ch.admin.seco.jobs.services.jobadservice.domain.profession.ProfessionCodeType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisementStatus.REFINING;

import com.google.common.collect.ImmutableMap;

@Service
@Transactional(rollbackFor = {Exception.class})
public class JobAdvertisementApplicationService {

    private final static Map<ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.api.LanguageSkillDto.LanguageLevel, LanguageLevel> API_LANGUAGE_SKILL_MAPPING = ImmutableMap
            .<ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.api.LanguageSkillDto.LanguageLevel, LanguageLevel>builder()
            .put(ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.api.LanguageSkillDto.LanguageLevel.no_knowledge, LanguageLevel.NONE)
            .put(ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.api.LanguageSkillDto.LanguageLevel.basic_knowledge, LanguageLevel.BASIC)
            .put(ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.api.LanguageSkillDto.LanguageLevel.good, LanguageLevel.INTERMEDIATE)
            .put(ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.api.LanguageSkillDto.LanguageLevel.very_good, LanguageLevel.PROFICIENT)
            .build();

    private final JobAdvertisementRepository jobAdvertisementRepository;

    private final JobAdvertisementFactory jobAdvertisementFactory;

    private final RavRegistrationService ravRegistrationService;

    private final ReportingObligationService reportingObligationService;

    private final LocalityService localityService;

    private final ProfessionService professionSerivce;

    @Autowired
    public JobAdvertisementApplicationService(JobAdvertisementRepository jobAdvertisementRepository, JobAdvertisementFactory jobAdvertisementFactory, RavRegistrationService ravRegistrationService, ReportingObligationService reportingObligationService, LocalityService localityService, ProfessionService professionSerivce) {
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

    public JobAdvertisementId createFromApi(CreateJobAdvertisementApiDto createJobAdvertisementApiDto) {
        Locality locality = toLocality(createJobAdvertisementApiDto.getJob().getLocation());
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
                .setEmployment(toEmployment(createJobAdvertisementApiDto.getJob()))
                .setApplyChannel(toApplyChannel(createJobAdvertisementApiDto))
                .setCompany(toCompany(createJobAdvertisementApiDto.getCompany()))
                .setContact(toContact(createJobAdvertisementApiDto.getContact()))
                .setLanguageSkills(apiToLanguageSkills(createJobAdvertisementApiDto.getJob().getLanguageSkills()))
                .build();

        JobAdvertisement jobAdvertisement = jobAdvertisementFactory.createFromApi(
                createJobAdvertisementApiDto.getJob().getTitle(),
                createJobAdvertisementApiDto.getJob().getDescription(),
                updater
        );
        return jobAdvertisement.getId();
    }

    private JobAdvertisement getJobAdvertisement(JobAdvertisementId jobAdvertisementId) throws AggregateNotFoundException {
        Optional<JobAdvertisement> jobAdvertisement = jobAdvertisementRepository.findById(jobAdvertisementId);
        return jobAdvertisement.orElseThrow(() -> new AggregateNotFoundException(JobAdvertisement.class, jobAdvertisementId.getValue()));
    }

    private Occupation enrichOccupationWithProfessionCodes(Occupation occupation) {
        Profession profession = professionSerivce.findByAvamCode(occupation.getAvamCode());
        if (profession != null) {
            return new Occupation(
                    occupation.getAvamCode(),
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
        String avamCode = occupation.getAvamCode();
        String cantonCode = locality.getCantonCode();
        return (cantonCode != null) && reportingObligationService.hasReportingObligation(ProfessionCodeType.AVAM, avamCode, cantonCode);
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

    private Employment toEmployment(JobDto job) {
        return new Employment(
                job.getStartDate(),
                job.getEndDate(),
                job.getDurationInDays(),
                job.getStartsImmediately(),
                job.getPermanent(),
                job.getWorkingTimePercentageFrom(),
                job.getWorkingTimePercentageTo()
        );
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

    private ApplyChannel toApplyChannel(CreateJobAdvertisementApiDto createJobAdvertisementApiDto) {
        String mailAddress = getMailAddressFromApi(createJobAdvertisementApiDto);
        String emailAddress = createJobAdvertisementApiDto.getContact() != null
                ? createJobAdvertisementApiDto.getContact().getEmail() : null;
        String phoneNumber = createJobAdvertisementApiDto.getContact() != null
                ? createJobAdvertisementApiDto.getContact().getPhoneNumber() : null;

        return new ApplyChannel(
                mailAddress,
                emailAddress,
                phoneNumber,
                createJobAdvertisementApiDto.getApplicationUrl(),
                null
        );
    }

    private String getMailAddressFromApi(CreateJobAdvertisementApiDto createJobAdvertisementApiDto) {
        ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.api.CompanyDto company = createJobAdvertisementApiDto.getCompany();
        return String.join(", ", company.getName(), company.getStreet(), company.getPostalCode(), company.getLocality());
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

    private Company toCompany(ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.api.CompanyDto companyDto) {
        if (companyDto != null) {
            return new Company.Builder()
                    .setName(companyDto.getName())
                    .setStreet(companyDto.getStreet())
                    .setHouseNumber(companyDto.getHouseNumber())
                    .setZipCode(companyDto.getPostalCode())
                    .setCity(companyDto.getLocality())
                    .setCountryIsoCode(companyDto.getCountryCode())
                    .setPostOfficeBoxNumber(companyDto.getPostbox().getNumber())
                    .setPostOfficeBoxZipCode(companyDto.getPostbox().getPostalCode())
                    .setPostOfficeBoxCity(companyDto.getPostbox().getLocality())
                    .setPhone(companyDto.getPhoneNumber())
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

	private Contact toContact(ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.api.ContactDto contactDto) {
		if (contactDto != null) {
			return new Contact(
					mapContactTitleToSalutation(contactDto.getTitle()),
					contactDto.getFirstName(),
					contactDto.getLastName(),
					contactDto.getPhoneNumber(),
					contactDto.getEmail()
			);
		}
		return null;
	}

	private Salutation mapContactTitleToSalutation(ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.api.ContactDto.Title title) {
		return ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.api.ContactDto.Title.mister == title
				? Salutation.MR : Salutation.MS;
	}

    private Locality toLocality(LocalityDto localityDto) {
        if (localityDto != null) {
            return new Locality(
                    localityDto.getRemarks(),
                    localityDto.getCity(),
                    localityDto.getZipCode(),
                    localityDto.getCommunalCode(),
                    localityDto.getRegionCode(),
                    localityDto.getCantonCode(),
                    localityDto.getCountryIsoCode(),
                    localityDto.getLocation()
            );
        }
        return null;
    }

    private Locality toLocality(LocationDto localityDto) {
        if (localityDto != null) {
	        return new Locality(
			        localityDto.getAdditionalDetails(),
			        localityDto.getLocality(),
			        localityDto.getPostalCode(),
			        localityDto.getCountryCode()
	        );
        }
        return null;
    }

    private Occupation toOccupation(OccupationDto occupationDto) {
        if (occupationDto != null) {
            return new Occupation(
                    occupationDto.getAvamCode(),
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

    private List<LanguageSkill> apiToLanguageSkills(List<ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.api.LanguageSkillDto> languageSkillDtos) {
        if (languageSkillDtos != null) {
            return languageSkillDtos.stream()
                    .map(languageSkillDto -> new LanguageSkill(
                            languageSkillDto.getLanguage(),
                            mapLanguageLevel(languageSkillDto.getSpokenLevel()),
                            mapLanguageLevel(languageSkillDto.getWrittenLevel())
                    ))
                    .collect(Collectors.toList());
        }
        return null;
    }

    private LanguageLevel mapLanguageLevel(ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.api.LanguageSkillDto.LanguageLevel languageLevel) {
        return API_LANGUAGE_SKILL_MAPPING.get(languageLevel);
    }
}
