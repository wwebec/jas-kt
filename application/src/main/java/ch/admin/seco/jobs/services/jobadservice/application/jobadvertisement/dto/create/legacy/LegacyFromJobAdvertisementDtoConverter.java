package ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.create.legacy;

import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.*;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

public class LegacyFromJobAdvertisementDtoConverter {

    public static LegacyPageDto convert(Page<JobAdvertisementDto> page) {
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
        return new LegacyJobAdvertisementDto(
                jobAdvertisementDto.getId(),
                jobAdvertisementDto.getPublication().getStartDate(),
                jobAdvertisementDto.getPublication().getEndDate(),
                jobAdvertisementDto.getExternalReference(),
                jobAdvertisementDto.getJobContent().getExternalUrl(),
                jobAdvertisementDto.getJobContent().getApplyChannel().getFormUrl(),
                convertToJob(jobAdvertisementDto),
                convertToCompany(jobAdvertisementDto.getJobContent().getCompany()),
                convertToContact(jobAdvertisementDto.getJobContent().getPublicContact()),
                "https://api.job-room.ch/0.2/joboffers/" + jobAdvertisementDto.getId(),
                "https://api.job-room.ch/0.2/joboffers/" + jobAdvertisementDto.getId()
        );
    }

    private static LegacyJobDto convertToJob(JobAdvertisementDto jobAdvertisementDto) {
        return new LegacyJobDto(
                jobAdvertisementDto.getJobContent().getJobDescriptions().get(0).getTitle(),
                jobAdvertisementDto.getJobContent().getJobDescriptions().get(0).getDescription(),
                jobAdvertisementDto.getJobContent().getEmployment().getWorkloadPercentageMin(),
                jobAdvertisementDto.getJobContent().getEmployment().getWorkloadPercentageMax(),
                jobAdvertisementDto.getJobContent().getEmployment().isImmediately(),
                jobAdvertisementDto.getJobContent().getEmployment().getStartDate(),
                jobAdvertisementDto.getJobContent().getEmployment().isPermanent(),
                jobAdvertisementDto.getJobContent().getEmployment().getEndDate(),
                convertToLanguageSkills(jobAdvertisementDto.getJobContent().getLanguageSkills()),
                convertToLocation(jobAdvertisementDto.getJobContent().getLocation())
        );
    }

    private static LegacyCompanyDto convertToCompany(CompanyDto company) {
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
        return new LegacyContactDto(
                LegacyTitleEnum.MAPPING_TITLE.getLeft(publicContact.getSalutation()),
                publicContact.getFirstName(),
                publicContact.getLastName(),
                publicContact.getPhone(),
                publicContact.getEmail()
        );
    }

    private static List<LegacyLanguageSkillDto> convertToLanguageSkills(List<LanguageSkillDto> languageSkills) {
        return languageSkills.stream()
                .map(languageSkillDto -> new LegacyLanguageSkillDto(
                        languageSkillDto.getLanguageIsoCode(),
                        LegacyLanguageLevelEnum.MAPPING_LANGUAGE_LEVEL.getLeft(languageSkillDto.getSpokenLevel()),
                        LegacyLanguageLevelEnum.MAPPING_LANGUAGE_LEVEL.getLeft(languageSkillDto.getWrittenLevel())
                ))
                .collect(Collectors.toList());
    }

    private static LegacyLocationDto convertToLocation(LocationDto location) {
        return new LegacyLocationDto(
                location.getPostalCode(),
                location.getCity(),
                location.getCountryIsoCode(),
                location.getRemarks()
        );
    }

}
