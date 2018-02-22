package ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement;

import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.*;
import ch.admin.seco.jobs.services.jobadservice.core.domain.AggregateNotFoundException;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.*;
import ch.admin.seco.jobs.services.jobadservice.domain.profession.ProfessionId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(rollbackFor = {Exception.class})
public class JobAdvertisementApplicationService {

    private final JobAdvertisementRepository jobAdvertisementRepository;

    private final JobAdvertisementFactory jobAdvertisementFactory;

    @Autowired
    public JobAdvertisementApplicationService(JobAdvertisementRepository jobAdvertisementRepository, JobAdvertisementFactory jobAdvertisementFactory) {
        this.jobAdvertisementRepository = jobAdvertisementRepository;
        this.jobAdvertisementFactory = jobAdvertisementFactory;
    }

    public JobAdvertisementId createFromWebForm(CreateJobAdvertisementDto createJobAdvertisementDto) {
        final JobAdvertisementUpdater updater = new JobAdvertisementUpdater.Builder(null)
                // FIXME eval eures if anonymous or not
                .setEures(createJobAdvertisementDto.isEures(), true)
                .setEmployment(
                        createJobAdvertisementDto.getEmploymentStartDate(),
                        createJobAdvertisementDto.getEmploymentEndDate(),
                        createJobAdvertisementDto.getDurationInDays(),
                        createJobAdvertisementDto.getImmediately(),
                        createJobAdvertisementDto.getPermanent(),
                        createJobAdvertisementDto.getWorkloadPercentageMin(),
                        createJobAdvertisementDto.getWorkloadPercentageMax()
                )
                .setApplyChannel(toApplyChannel(createJobAdvertisementDto.getApplyChannel()))
                .setCompany(toCompany(createJobAdvertisementDto.getCompany()))
                .setContact(toContact(createJobAdvertisementDto.getContact()))
                .setLocalities(toLocalities(createJobAdvertisementDto.getLocalities()))
                .setOccupations(toOccupations(Collections.singletonList(createJobAdvertisementDto.getOccupation())))
                .setEducationCode(createJobAdvertisementDto.getEducationCode())
                .setLanguageSkills(toLanguageSkills(createJobAdvertisementDto.getLanguageSkills()))
                .build();

        JobAdvertisement jobAdvertisement = jobAdvertisementFactory.createFromWebForm(
                createJobAdvertisementDto.getTitle(),
                createJobAdvertisementDto.getDescription(),
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

    public void approve(ApprovalDto approvalDto) {

    }

    public void reject(RejectionDto rejectionDto) {

    }

    public void cancel(CancellationDto cancellationDto) {

    }

    public void archive(JobAdvertisementId jobAdvertisementId) {

    }

    private JobAdvertisement getJobAdvertisement(JobAdvertisementId jobAdvertisementId) throws AggregateNotFoundException {
        Optional<JobAdvertisement> jobAdvertisement = jobAdvertisementRepository.findById(jobAdvertisementId);
        return jobAdvertisement.orElseThrow(() -> new AggregateNotFoundException(JobAdvertisement.class, jobAdvertisementId.getValue()));
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
            return new Company(
                    companyDto.getName(),
                    companyDto.getStreet(),
                    companyDto.getHouseNumber(),
                    companyDto.getZipCode(),
                    companyDto.getCity(),
                    companyDto.getCountryIsoCode(),
                    companyDto.getPostOfficeBoxNumber(),
                    companyDto.getPostOfficeBoxZipCode(),
                    companyDto.getPostOfficeBoxCity(),
                    companyDto.getPhone(),
                    companyDto.getEmail(),
                    companyDto.getWebsite()
            );
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
            // TODO Resolve codes for ch localities
            return localityDtos.stream()
                    .map(localityDto -> new Locality(
                            localityDto.getRemarks(),
                            localityDto.getCity(),
                            localityDto.getZipCode(),
                            localityDto.getCommunalCode(),
                            localityDto.getRegionCode(),
                            localityDto.getCantonCode(),
                            localityDto.getCountryIsoCode(),
                            localityDto.getLocation()
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
