package ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.LanguageLevel;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.LanguageSkill;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.utils.SupportedLanguageIsoCode;

public class LanguageSkillDto {

    @NotBlank
    @SupportedLanguageIsoCode
    private String languageIsoCode;

    @NotNull
    private LanguageLevel spokenLevel;

    @NotNull
    private LanguageLevel writtenLevel;

    public String getLanguageIsoCode() {
        return languageIsoCode;
    }

    public LanguageSkillDto setLanguageIsoCode(String languageIsoCode) {
        this.languageIsoCode = languageIsoCode;
        return this;
    }

    public LanguageLevel getSpokenLevel() {
        return spokenLevel;
    }

    public LanguageSkillDto setSpokenLevel(LanguageLevel spokenLevel) {
        this.spokenLevel = spokenLevel;
        return this;
    }

    public LanguageLevel getWrittenLevel() {
        return writtenLevel;
    }

    public LanguageSkillDto setWrittenLevel(LanguageLevel writtenLevel) {
        this.writtenLevel = writtenLevel;
        return this;
    }

    public static LanguageSkillDto toDto(LanguageSkill languageSkill) {
        return new LanguageSkillDto()
                .setLanguageIsoCode(languageSkill.getLanguageIsoCode())
                .setSpokenLevel(languageSkill.getSpokenLevel())
                .setWrittenLevel(languageSkill.getWrittenLevel());
    }

    public static List<LanguageSkillDto> toDto(List<LanguageSkill> languageSkills) {
        if (languageSkills == null) {
            return null;
        }
            return languageSkills.stream().map(LanguageSkillDto::toDto).collect(Collectors.toList());
        }
}
