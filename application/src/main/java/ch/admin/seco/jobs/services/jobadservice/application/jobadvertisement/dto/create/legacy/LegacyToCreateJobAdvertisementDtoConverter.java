package ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.create.legacy;

import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.*;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.create.CreateJobAdvertisementDto;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.create.CreateLocationDto;
import ch.admin.seco.jobs.services.jobadservice.core.conditions.Condition;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.utils.WorkingTimePercentage;

import java.util.Collections;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class LegacyToCreateJobAdvertisementDtoConverter {

    private static final String DEFAULT_LANGUAGE_ISO_CODE = "de";

    public static CreateJobAdvertisementDto convert(LegacyCreateJobAdvertisementDto legacyCreateJobAdvertisementDto) {
        Condition.notNull(legacyCreateJobAdvertisementDto);

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
                convertLocationDto(legacyCreateJobAdvertisementDto.getJob()),
                null,
                convertLanguageSkills(legacyCreateJobAdvertisementDto.getJob()),
                convertApplyChannelDto(legacyCreateJobAdvertisementDto),
                convertPublicContactDto(legacyCreateJobAdvertisementDto)
        );
    }

    private static PublicContactDto convertPublicContactDto(LegacyCreateJobAdvertisementDto legacyCreateJobAdvertisementDto) {
        LegacyContactDto contact = legacyCreateJobAdvertisementDto.getContact();
        if (contact == null) {
            return null;
        }

        return new PublicContactDto(
                LegacyTitleEnum.MAPPING_TITLE.getRight(contact.getTitle()),
                contact.getFirstName(),
                contact.getLastName(),
                contact.getPhoneNumber(),
                contact.getEmail()
        );
    }

    private static ApplyChannelDto convertApplyChannelDto(LegacyCreateJobAdvertisementDto legacyCreateJobAdvertisementDto) {
        LegacyCompanyDto company = legacyCreateJobAdvertisementDto.getCompany();
        return new ApplyChannelDto(
                null,
                company == null ? null : company.getEmail(),
                company == null ? null : company.getPhoneNumber(),
                legacyCreateJobAdvertisementDto.getApplicationUrl(),
                null
        );
    }

    private static List<LanguageSkillDto> convertLanguageSkills(LegacyJobDto legacyJobDto) {
        if (legacyJobDto == null || legacyJobDto.getLanguageSkills() == null) {
            return null;
        }

        return legacyJobDto.getLanguageSkills().stream()
                .map(legacyLanguageSkillDto -> new LanguageSkillDto(
                                legacyLanguageSkillDto.getLanguage(),
                                LegacyLanguageLevelEnum.MAPPING_LANGUAGE_LEVEL.getRight(safeString2LegacyLanguageLevelEnum(legacyLanguageSkillDto.getSpokenLevel())),
                                LegacyLanguageLevelEnum.MAPPING_LANGUAGE_LEVEL.getRight(safeString2LegacyLanguageLevelEnum(legacyLanguageSkillDto.getWrittenLevel()))
                        )
                )
                .collect(toList());
    }

    private static LegacyLanguageLevelEnum safeString2LegacyLanguageLevelEnum(String value) {
        return (value != null) ? LegacyLanguageLevelEnum.valueOf(value.toUpperCase()) : LegacyLanguageLevelEnum.NO_KNOWLEDGE;
    }

    private static CreateLocationDto convertLocationDto(LegacyJobDto legacyJobDto) {
        if (legacyJobDto == null || legacyJobDto.getLocation() == null) {
            return null;
        }

        return new CreateLocationDto(
                legacyJobDto.getLocation().getAdditionalDetails(),
                legacyJobDto.getLocation().getLocality(),
                legacyJobDto.getLocation().getPostalCode(),
                legacyJobDto.getLocation().getCountryCode()
        );
    }

    private static EmploymentDto convertEmploymentDto(LegacyJobDto legacyJobDto) {
        if (legacyJobDto == null) {
            return null;
        }

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
        if (company == null) {
            return null;
        }

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
        LegacyJobDto job = legacyCreateJobAdvertisementDto.getJob();
        if (job == null) {
            return Collections.emptyList();
        }

        return Collections.singletonList(
                new JobDescriptionDto(
                        DEFAULT_LANGUAGE_ISO_CODE,
                        job.getTitle(),
                        job.getDescription()
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
        if (contact == null) {
            return null;
        }

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
