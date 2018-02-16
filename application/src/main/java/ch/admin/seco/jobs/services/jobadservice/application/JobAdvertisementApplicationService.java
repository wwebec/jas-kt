package ch.admin.seco.jobs.services.jobadservice.application;

import ch.admin.seco.jobs.services.jobadservice.application.dto.*;
import ch.admin.seco.jobs.services.jobadservice.core.domain.AggregateNotFoundException;
import ch.admin.seco.jobs.services.jobadservice.core.domain.events.DomainEventPublisher;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.*;
import ch.admin.seco.jobs.services.jobadservice.domain.profession.ProfessionId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(rollbackFor = {Exception.class})
public class JobAdvertisementApplicationService {

    private final JobAdvertisementRepository jobAdvertisementRepository;

    @Autowired
    public JobAdvertisementApplicationService(JobAdvertisementRepository jobAdvertisementRepository) {
        this.jobAdvertisementRepository = jobAdvertisementRepository;
    }

    public JobAdvertisementId createJobAdvertisement(CreateJobAdvertisementDto createJobAdvertisementDto) {
        JobAdvertisement jobAdvertisement = new JobAdvertisement(
                new JobAdvertisementId(),
                JobAdvertisementStatus.CREATED,
                createJobAdvertisementDto.getTitle(),
                createJobAdvertisementDto.getDescription()
        );
        jobAdvertisement.updateEmployment(
                createJobAdvertisementDto.getEmploymentStartDate(),
                createJobAdvertisementDto.getEmploymentEndDate(),
                createJobAdvertisementDto.getDurationInDays(),
                createJobAdvertisementDto.getImmediately(),
                createJobAdvertisementDto.getPermanent(),
                createJobAdvertisementDto.getWorkloadPercentageMin(),
                createJobAdvertisementDto.getWorkloadPercentageMax()
        );
        ApplyChannelDto applyChannelDto = createJobAdvertisementDto.getApplyChannel();
        if (applyChannelDto != null) {
            jobAdvertisement.updateApplyChannel(new ApplyChannel(
                    applyChannelDto.getMailAddress(),
                    applyChannelDto.getEmailAddress(),
                    applyChannelDto.getPhoneNumber(),
                    applyChannelDto.getFormUrl(),
                    applyChannelDto.getAdditionalInfo()
            ));
        }
        CompanyDto companyDto = createJobAdvertisementDto.getCompany();
        if (companyDto != null) {
            jobAdvertisement.updateCompany(new Company(
                    companyDto.getName(),
                    companyDto.getStreet(),
                    companyDto.getHouseNumber(),
                    companyDto.getZipCode(),
                    companyDto.getCity(),
                    companyDto.getCountryCode(),
                    companyDto.getPostOfficeBoxNumber(),
                    companyDto.getPostOfficeBoxZipCode(),
                    companyDto.getPostOfficeBoxCity(),
                    companyDto.getPhone(),
                    companyDto.getEmail(),
                    companyDto.getWebsite()
            ));
        }
        ContactDto contactDto = createJobAdvertisementDto.getContact();
        if (contactDto != null) {
            jobAdvertisement.updateContact(new Contact(
                    Salutation.valueOf(contactDto.getSalutation()),
                    contactDto.getFirstName(),
                    contactDto.getLastName(),
                    contactDto.getPhone(),
                    contactDto.getEmail()
            ));
        }
        Set<LocalityDto> localityDtos = createJobAdvertisementDto.getLocalities();
        if (localityDtos != null) {
            // TODO Resolve codes for ch localities
            jobAdvertisement.updateLocalities(localityDtos.stream().map(localityDto -> new Locality(
                    localityDto.getRemarks(),
                    localityDto.getCity(),
                    localityDto.getZipCode(),
                    localityDto.getCommunalCode(),
                    localityDto.getRegionCode(),
                    localityDto.getCantonCode(),
                    localityDto.getCountryCode(),
                    localityDto.getLocation()
            )).collect(Collectors.toList()));
        }
        OccupationDto occupationDto = createJobAdvertisementDto.getOccupation();
        if (occupationDto != null) {
            // TODO update professionCodes
            List<Occupation> occupations = new ArrayList<>();
            occupations.add(new Occupation(
                    new ProfessionId(occupationDto.getProfessionId()),
                    occupationDto.getWorkExperience()
            ));
            jobAdvertisement.updateRequirements(occupations, createJobAdvertisementDto.getEducationCode());
        }
        Set<LanguageSkillDto> languageSkillDtos = createJobAdvertisementDto.getLanguageSkills();
        if (languageSkillDtos != null) {
            jobAdvertisement.updateLanguageSkills(languageSkillDtos.stream().map(languageSkillDto -> new LanguageSkill(
                    languageSkillDto.getLanguageIsoCode(),
                    LanguageLevel.valueOf(languageSkillDto.getSpokenLevel()),
                    LanguageLevel.valueOf(languageSkillDto.getWrittenLevel())
            )).collect(Collectors.toList()));
        }
        JobAdvertisement newJobAdvertisement = jobAdvertisementRepository.save(jobAdvertisement);
        DomainEventPublisher.publish(new JobAdvertisementEvent(JobAdvertisementEvents.JOB_ADVERTISEMENT_CREATED, newJobAdvertisement));
        return newJobAdvertisement.getId();
    }

    public List<JobAdvertisementDto> findAll() {
        List<JobAdvertisement> jobAdvertisements = jobAdvertisementRepository.findAll();
        return jobAdvertisements.stream().map(JobAdvertisementDto::toDto).collect(Collectors.toList());
    }

    public JobAdvertisementDto findById(JobAdvertisementId jobAdvertisementId) throws AggregateNotFoundException {
        JobAdvertisement jobAdvertisement = getJobAdvertisement(jobAdvertisementId);
        return JobAdvertisementDto.toDto(jobAdvertisement);
    }

    private JobAdvertisement getJobAdvertisement(JobAdvertisementId jobAdvertisementId) throws AggregateNotFoundException {
        Optional<JobAdvertisement> jobAdvertisement = jobAdvertisementRepository.findById(jobAdvertisementId);
        return jobAdvertisement.orElseThrow(() -> new AggregateNotFoundException(JobAdvertisement.class, jobAdvertisementId.getValue()));
    }
}
