package ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.api;

import ch.admin.seco.jobs.services.jobadservice.core.utils.MappingBuilder;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.Employment;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.LanguageLevel;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.LanguageSkill;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.Location;

import java.util.List;
import java.util.stream.Collectors;

public class ApiDtoConverter {

    private static final MappingBuilder<LanguageSkillApiDto.LanguageLevel, LanguageLevel> LANGUAGE_SKILLS = new MappingBuilder<LanguageSkillApiDto.LanguageLevel, LanguageLevel>()
            .put(LanguageSkillApiDto.LanguageLevel.no_knowledge, LanguageLevel.NONE)
            .put(LanguageSkillApiDto.LanguageLevel.basic_knowledge, LanguageLevel.BASIC)
            .put(LanguageSkillApiDto.LanguageLevel.good, LanguageLevel.INTERMEDIATE)
            .put(LanguageSkillApiDto.LanguageLevel.very_good, LanguageLevel.PROFICIENT)
            .toImmutable();

    public static Employment toEmployment(JobApiDto jobApiDto) {
        return new Employment(
                jobApiDto.getStartDate(),
                jobApiDto.getEndDate(),
                jobApiDto.getDurationInDays(),
                jobApiDto.getStartsImmediately(),
                jobApiDto.getPermanent(),
                jobApiDto.getWorkingTimePercentageFrom(),
                jobApiDto.getWorkingTimePercentageTo()
        );
    }

    public static Location toLocation(LocationApiDto locationApiDto) {
        if (locationApiDto != null) {
            return new Location(
                    locationApiDto.getRemarks(),
                    locationApiDto.getCity(),
                    locationApiDto.getPostalCode(),
                    null,
                    null,
                    locationApiDto.getCantonCode(),
                    locationApiDto.getCountryIsoCode(),
                    null
            );
        }
        return null;
    }

    public static List<LanguageSkill> toLanguageSkills(List<LanguageSkillApiDto> languageSkillApiDtos) {
        if (languageSkillApiDtos != null) {
            return languageSkillApiDtos.stream()
                    .map(languageSkillApiDto -> new LanguageSkill(
                            languageSkillApiDto.getLanguage(),
                            LANGUAGE_SKILLS.getRight(languageSkillApiDto.getSpokenLevel()),
                            LANGUAGE_SKILLS.getRight(languageSkillApiDto.getWrittenLevel())
                    ))
                    .collect(Collectors.toList());
        }
        return null;
    }

}
