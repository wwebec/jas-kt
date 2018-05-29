package ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.create.legacy;

import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.*;
import ch.admin.seco.jobs.services.jobadservice.core.conditions.Condition;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.utils.WorkingTimePercentage;

import org.springframework.data.domain.Page;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

public class LegacyFromJobAdvertisementDtoConverter {

    public static LegacyPageDto convert(Page<JobAdvertisementDto> page) {
        Condition.notNull(page);

        return new LegacyPageDto(
                page.getContent().stream().map(LegacyFromJobAdvertisementDtoConverter::convert).collect(toList()),
                "https://api.job-room.ch/0.2/joboffers",
                "https://api.job-room.ch/0.2/profile/joboffers",
                "https://api.job-room.ch/0.2/joboffers/search",
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.getNumber()
        );

    }

    public static LegacyJobAdvertisementDto convert(JobAdvertisementDto jobAdvertisementDto) {
        Condition.notNull(jobAdvertisementDto);

        PublicationDto publication = jobAdvertisementDto.getPublication();
        JobContentDto jobContent = jobAdvertisementDto.getJobContent();
        ApplyChannelDto applyChannel = jobContent == null ? null : jobContent.getApplyChannel();
        return new LegacyJobAdvertisementDto(
                jobAdvertisementDto.getId(),
                publication == null ? null : publication.getStartDate(),
                publication == null ? null : publication.getEndDate(),
                jobAdvertisementDto.getExternalReference(),
                jobContent == null ? null : jobContent.getExternalUrl(),
                applyChannel == null ? null : applyChannel.getFormUrl(),
                convertToJob(jobAdvertisementDto),
                jobContent == null ? null : convertToCompany(jobContent.getCompany()),
                jobContent == null ? null : convertToContact(jobContent.getPublicContact()),
                "https://api.job-room.ch/0.2/joboffers/" + jobAdvertisementDto.getId(),
                "https://api.job-room.ch/0.2/joboffers/" + jobAdvertisementDto.getId()
        );
    }

    private static LegacyJobDto convertToJob(JobAdvertisementDto jobAdvertisementDto) {
        JobContentDto jobContent = jobAdvertisementDto.getJobContent();
        if (jobContent == null || CollectionUtils.isEmpty(jobContent.getJobDescriptions())) {
            return null;
        }

        EmploymentDto employment = jobContent.getEmployment();
        WorkingTimePercentage workingTimePercentage = employment != null
                ? WorkingTimePercentage.evaluate(employment.getWorkloadPercentageMin(), employment.getWorkloadPercentageMax())
                : WorkingTimePercentage.defaultPercentage();
        return new LegacyJobDto(
                jobContent.getJobDescriptions().get(0).getTitle(),
                jobContent.getJobDescriptions().get(0).getDescription(),
                workingTimePercentage.getMin(),
                workingTimePercentage.getMax(),
                employment != null && employment.isImmediately(),
                employment == null ? null : employment.getStartDate(),
                employment != null && employment.isPermanent(),
                employment == null ? null : employment.getEndDate(),
                convertToLanguageSkills(jobContent.getLanguageSkills()),
                convertToLocation(jobContent.getLocation())
        );
    }

    private static LegacyCompanyDto convertToCompany(CompanyDto company) {
        if (company == null) {
            return null;
        }

        return new LegacyCompanyDto(
                company.getName(),
                company.getStreet(),
                company.getHouseNumber(),
                company.getPostalCode(),
                company.getCity(),
                new LegacyPostboxDto(
                        company.getPostOfficeBoxNumber(),
                        company.getPostOfficeBoxCity(),
                        company.getPostOfficeBoxPostalCode()
                ),
                company.getCountryIsoCode(),
                company.getPhone(),
                company.getEmail(),
                company.getWebsite()
        );
    }

    private static LegacyContactDto convertToContact(PublicContactDto publicContact) {
        if (publicContact == null) {
            return null;
        }

        return new LegacyContactDto(
                LegacyTitleEnum.MAPPING_TITLE.getLeft(publicContact.getSalutation()),
                publicContact.getFirstName(),
                publicContact.getLastName(),
                publicContact.getPhone(),
                publicContact.getEmail()
        );
    }

    private static List<LegacyLanguageSkillDto> convertToLanguageSkills(List<LanguageSkillDto> languageSkills) {
        if (languageSkills == null) {
            return Collections.emptyList();
        }

        return languageSkills.stream()
                .map(languageSkillDto -> new LegacyLanguageSkillDto(
                        languageSkillDto.getLanguageIsoCode(),
                        LegacyLanguageLevelEnum.MAPPING_LANGUAGE_LEVEL.getLeft(languageSkillDto.getSpokenLevel()),
                        LegacyLanguageLevelEnum.MAPPING_LANGUAGE_LEVEL.getLeft(languageSkillDto.getWrittenLevel())
                ))
                .collect(Collectors.toList());
    }

    private static LegacyLocationDto convertToLocation(LocationDto location) {
        if (location == null) {
            return null;
        }

        return new LegacyLocationDto(
                location.getPostalCode(),
                location.getCity(),
                location.getCountryIsoCode(),
                location.getRemarks()
        );
    }

}
