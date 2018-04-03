package ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.create.legacy;

import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.*;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.create.CreateJobAdvertisementDto;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.create.CreateLocationDto;
import ch.admin.seco.jobs.services.jobadservice.core.utils.MappingBuilder;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.LanguageLevel;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.Salutation;

import java.util.Collections;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class LegacyToJobAdvertisementConverter {

    private static final String DEFAULT_LANGUAGE_ISO_CODE = "de";

    private static final MappingBuilder<LegacyTitleEnum, Salutation> MAPPING_TITLE = new MappingBuilder<LegacyTitleEnum, Salutation>()
            .put(LegacyTitleEnum.MISTER, Salutation.MR)
            .put(LegacyTitleEnum.MADAM, Salutation.MS)
            .toImmutable();

    private static final MappingBuilder<LegacyLanguageLevelEnum, LanguageLevel> MAPPING_LANGUAGE_LEVEL = new MappingBuilder<LegacyLanguageLevelEnum, LanguageLevel>()
            .put(LegacyLanguageLevelEnum.NO_KNOWLEDGE, LanguageLevel.NONE)
            .put(LegacyLanguageLevelEnum.BASIC_KNOWLEDGE, LanguageLevel.BASIC)
            .put(LegacyLanguageLevelEnum.GOOD, LanguageLevel.INTERMEDIATE)
            .put(LegacyLanguageLevelEnum.VERY_GOOD, LanguageLevel.PROFICIENT)
            .toImmutable();

    public static CreateJobAdvertisementDto convert(LegacyJobAdvertisementDto legacyJobAdvertisementDto) {
        return new CreateJobAdvertisementDto(
                false,
                legacyJobAdvertisementDto.getUrl(),
                convertContactDto(legacyJobAdvertisementDto.getContact()),
                convertPublicationDto(legacyJobAdvertisementDto),
                convertJobDescriptions(legacyJobAdvertisementDto),
                convertCompanyDto(legacyJobAdvertisementDto.getCompany()),
                null,
                convertEmploymentDto(legacyJobAdvertisementDto.getJob()),
                convertLocationDto(legacyJobAdvertisementDto.getJob().getLocation()),
                null,
                convertLanguageSkills(legacyJobAdvertisementDto.getJob().getLanguageSkills()),
                convertApplyChannelDto(legacyJobAdvertisementDto),
                convertPublicContactDto(legacyJobAdvertisementDto.getContact())
        );
    }

    private static PublicContactDto convertPublicContactDto(LegacyContactDto contact) {
        return new PublicContactDto(
                MAPPING_TITLE.getRight(contact.getTitle()),
                contact.getFirstName(),
                contact.getLastName(),
                contact.getPhoneNumber(),
                contact.getEmail()
        );
    }

    private static ApplyChannelDto convertApplyChannelDto(LegacyJobAdvertisementDto legacyJobAdvertisementDto) {
        return new ApplyChannelDto(
                null,
                legacyJobAdvertisementDto.getCompany().getEmail(),
                legacyJobAdvertisementDto.getCompany().getPhoneNumber(),
                legacyJobAdvertisementDto.getApplicationUrl(),
                null
        );
    }

    private static List<LanguageSkillDto> convertLanguageSkills(List<LegacyLanguageSkillDto> languageSkills) {
        return languageSkills.stream()
                .map(legacyLanguageSkillDto -> new LanguageSkillDto(
                                legacyLanguageSkillDto.getLanguage(),
                                MAPPING_LANGUAGE_LEVEL.getRight(legacyLanguageSkillDto.getSpokenLevel()),
                                MAPPING_LANGUAGE_LEVEL.getRight(legacyLanguageSkillDto.getWrittenLevel())
                        )
                )
                .collect(toList());
    }

    private static CreateLocationDto convertLocationDto(LegacyLocationDto legacyLocationDto) {
        return new CreateLocationDto(
                legacyLocationDto.getAdditionalDetails(),
                legacyLocationDto.getLocality(),
                legacyLocationDto.getPostalCode(),
                legacyLocationDto.getCountryCode(),
                null
        );
    }

    private static EmploymentDto convertEmploymentDto(LegacyJobDto legacyJobDto) {
        int[] workingTimePercentage = calcWorkingTimePercentage(legacyJobDto.getWorkingTimePercentageFrom(), legacyJobDto.getWorkingTimePercentageTo());
        return new EmploymentDto(
                legacyJobDto.getStartDate(),
                legacyJobDto.getEndDate(),
                false,
                (legacyJobDto.isStartsImmediately() != null) ? legacyJobDto.isStartsImmediately() : false,
                (legacyJobDto.isPermanent() != null) ? legacyJobDto.isPermanent() : (legacyJobDto.getEndDate() == null),
                workingTimePercentage[0],
                workingTimePercentage[1],
                null
        );
    }

    private static int[] calcWorkingTimePercentage(Integer from, Integer to) {
        if(from == null && to == null ) {
            return new int[]{100, 100};
        }
        if(from == null) {
            return  new int[]{to, to};
        }
        if(to == null) {
            return new int[]{from, 100};
        }
        return new int[]{from, to};
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

    private static List<JobDescriptionDto> convertJobDescriptions(LegacyJobAdvertisementDto legacyJobAdvertisementDto) {
        return Collections.singletonList(
                new JobDescriptionDto(
                        DEFAULT_LANGUAGE_ISO_CODE,
                        legacyJobAdvertisementDto.getJob().getTitle(),
                        legacyJobAdvertisementDto.getJob().getDescription()
                )
        );
    }

    private static PublicationDto convertPublicationDto(LegacyJobAdvertisementDto legacyJobAdvertisementDto) {
        return new PublicationDto(
                legacyJobAdvertisementDto.getPublicationStartDate(),
                legacyJobAdvertisementDto.getPublicationEndDate(),
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
                MAPPING_TITLE.getRight(contact.getTitle()),
                contact.getFirstName(),
                contact.getLastName(),
                contact.getPhoneNumber(),
                contact.getEmail(),
                DEFAULT_LANGUAGE_ISO_CODE
        );
    }
}
