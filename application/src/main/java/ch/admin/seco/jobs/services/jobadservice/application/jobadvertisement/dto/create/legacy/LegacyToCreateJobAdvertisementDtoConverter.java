package ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.create.legacy;

import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.*;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.create.CreateJobAdvertisementDto;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.create.CreateLocationDto;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.utils.WorkingTimePercentage;

import java.util.Collections;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class LegacyToCreateJobAdvertisementDtoConverter {

    private static final String DEFAULT_LANGUAGE_ISO_CODE = "de";

    public static CreateJobAdvertisementDto convert(LegacyCreateJobAdvertisementDto legacyCreateJobAdvertisementDto) {
        return new CreateJobAdvertisementDto(
                false,
                legacyCreateJobAdvertisementDto.getUrl(),
                legacyCreateJobAdvertisementDto.getReference(),
                convertContactDto(legacyCreateJobAdvertisementDto.getContact()),
                convertPublicationDto(legacyCreateJobAdvertisementDto),
                convertJobDescriptions(legacyCreateJobAdvertisementDto),
                convertCompanyDto(legacyCreateJobAdvertisementDto.getCompany()),
                null,
                convertEmploymentDto(legacyCreateJobAdvertisementDto.getJob()),
                convertLocationDto(legacyCreateJobAdvertisementDto.getJob().getLocation()),
                null,
                convertLanguageSkills(legacyCreateJobAdvertisementDto.getJob().getLanguageSkills()),
                convertApplyChannelDto(legacyCreateJobAdvertisementDto),
                convertPublicContactDto(legacyCreateJobAdvertisementDto.getContact())
        );
    }

    private static PublicContactDto convertPublicContactDto(LegacyContactDto contact) {
        return new PublicContactDto(
                LegacyTitleEnum.MAPPING_TITLE.getRight(contact.getTitle()),
                contact.getFirstName(),
                contact.getLastName(),
                contact.getPhoneNumber(),
                contact.getEmail()
        );
    }

    private static ApplyChannelDto convertApplyChannelDto(LegacyCreateJobAdvertisementDto legacyCreateJobAdvertisementDto) {
        return new ApplyChannelDto(
                null,
                legacyCreateJobAdvertisementDto.getCompany().getEmail(),
                legacyCreateJobAdvertisementDto.getCompany().getPhoneNumber(),
                legacyCreateJobAdvertisementDto.getApplicationUrl(),
                null
        );
    }

    private static List<LanguageSkillDto> convertLanguageSkills(List<LegacyLanguageSkillDto> languageSkills) {
        return languageSkills.stream()
                .map(legacyLanguageSkillDto -> new LanguageSkillDto(
                                legacyLanguageSkillDto.getLanguage(),
                                LegacyLanguageLevelEnum.MAPPING_LANGUAGE_LEVEL.getRight(legacyLanguageSkillDto.getSpokenLevel()),
                                LegacyLanguageLevelEnum.MAPPING_LANGUAGE_LEVEL.getRight(legacyLanguageSkillDto.getWrittenLevel())
                        )
                )
                .collect(toList());
    }

    private static CreateLocationDto convertLocationDto(LegacyLocationDto legacyLocationDto) {
        return new CreateLocationDto(
                legacyLocationDto.getAdditionalDetails(),
                legacyLocationDto.getLocality(),
                legacyLocationDto.getPostalCode(),
                legacyLocationDto.getCountryCode()
        );
    }

    private static EmploymentDto convertEmploymentDto(LegacyJobDto legacyJobDto) {
        WorkingTimePercentage workingTimePercentage = WorkingTimePercentage.evaluate(legacyJobDto.getWorkingTimePercentageFrom(), legacyJobDto.getWorkingTimePercentageTo());
        return new EmploymentDto(
                legacyJobDto.getStartDate(),
                legacyJobDto.getEndDate(),
                false,
                (legacyJobDto.isStartsImmediately() != null) ? legacyJobDto.isStartsImmediately() : false,
                (legacyJobDto.isPermanent() != null) ? legacyJobDto.isPermanent() : (legacyJobDto.getEndDate() == null),
                workingTimePercentage.getMin(),
                workingTimePercentage.getMax(),
                null
        );
    }

    private static CompanyDto convertCompanyDto(LegacyCompanyDto company) {
        return new CompanyDto(
                company.getName(),
                company.getStreet(),
                company.getHouseNumber(),
                company.getPostalCode(),
                company.getLocality(),
                company.getCountryCode(),
                (company.getPostbox() != null) ? company.getPostbox().getNumber() : null,
                (company.getPostbox() != null) ? company.getPostbox().getPostalCode() : null,
                (company.getPostbox() != null) ? company.getPostbox().getLocality() : null,
                company.getPhoneNumber(),
                company.getEmail(),
                company.getWebsite(),
                false
        );
    }

    private static List<JobDescriptionDto> convertJobDescriptions(LegacyCreateJobAdvertisementDto legacyCreateJobAdvertisementDto) {
        return Collections.singletonList(
                new JobDescriptionDto(
                        DEFAULT_LANGUAGE_ISO_CODE,
                        legacyCreateJobAdvertisementDto.getJob().getTitle(),
                        legacyCreateJobAdvertisementDto.getJob().getDescription()
                )
        );
    }

    private static PublicationDto convertPublicationDto(LegacyCreateJobAdvertisementDto legacyCreateJobAdvertisementDto) {
        return new PublicationDto(
                legacyCreateJobAdvertisementDto.getPublicationStartDate(),
                legacyCreateJobAdvertisementDto.getPublicationEndDate(),
                false,
                false,
                true,
                false,
                true,
                false
        );
    }

    private static ContactDto convertContactDto(LegacyContactDto contact) {
        return new ContactDto(
                LegacyTitleEnum.MAPPING_TITLE.getRight(contact.getTitle()),
                contact.getFirstName(),
                contact.getLastName(),
                contact.getPhoneNumber(),
                contact.getEmail(),
                DEFAULT_LANGUAGE_ISO_CODE
        );
    }
}
