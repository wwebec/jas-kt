package ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto;

import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.LanguageLevel;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.LanguageSkill;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

public class LanguageSkillDto {

    @NotBlank
    private String languageIsoCode;

    @NotNull
    private LanguageLevel spokenLevel;

    @NotNull
    private LanguageLevel writtenLevel;

    protected LanguageSkillDto() {
        // For reflection libs
    }

    public LanguageSkillDto(String languageIsoCode, LanguageLevel spokenLevel, LanguageLevel writtenLevel) {
        this.languageIsoCode = languageIsoCode;
        this.spokenLevel = spokenLevel;
        this.writtenLevel = writtenLevel;
    }

    public String getLanguageIsoCode() {
        return languageIsoCode;
    }

    public void setLanguageIsoCode(String languageIsoCode) {
        this.languageIsoCode = languageIsoCode;
    }

    public LanguageLevel getSpokenLevel() {
        return spokenLevel;
    }

    public void setSpokenLevel(LanguageLevel spokenLevel) {
        this.spokenLevel = spokenLevel;
    }

    public LanguageLevel getWrittenLevel() {
        return writtenLevel;
    }

    public void setWrittenLevel(LanguageLevel writtenLevel) {
        this.writtenLevel = writtenLevel;
    }

    public static LanguageSkillDto toDto(LanguageSkill languageSkill) {
        LanguageSkillDto languageSkillDto = new LanguageSkillDto();
        languageSkillDto.setLanguageIsoCode(languageSkill.getLanguageIsoCode());
        languageSkillDto.setSpokenLevel(languageSkill.getSpokenLevel());
        languageSkillDto.setWrittenLevel(languageSkill.getWrittenLevel());
        return languageSkillDto;
    }

    public static List<LanguageSkillDto> toDto(List<LanguageSkill> languageSkills) {
        return languageSkills.stream().map(LanguageSkillDto::toDto).collect(Collectors.toList());
    }
}
