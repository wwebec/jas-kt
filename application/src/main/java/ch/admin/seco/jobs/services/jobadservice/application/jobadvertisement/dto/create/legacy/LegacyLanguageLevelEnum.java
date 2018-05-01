package ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.create.legacy;

import ch.admin.seco.jobs.services.jobadservice.core.utils.MappingBuilder;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.LanguageLevel;

public enum LegacyLanguageLevelEnum {
    NO_KNOWLEDGE,
    BASIC_KNOWLEDGE,
    GOOD,
    VERY_GOOD;

    public static final MappingBuilder<LegacyLanguageLevelEnum, LanguageLevel> MAPPING_LANGUAGE_LEVEL = new MappingBuilder<LegacyLanguageLevelEnum, LanguageLevel>()
            .put(LegacyLanguageLevelEnum.NO_KNOWLEDGE, LanguageLevel.NONE)
            .put(LegacyLanguageLevelEnum.BASIC_KNOWLEDGE, LanguageLevel.BASIC)
            .put(LegacyLanguageLevelEnum.GOOD, LanguageLevel.INTERMEDIATE)
            .put(LegacyLanguageLevelEnum.VERY_GOOD, LanguageLevel.PROFICIENT)
            .toImmutable();

}
