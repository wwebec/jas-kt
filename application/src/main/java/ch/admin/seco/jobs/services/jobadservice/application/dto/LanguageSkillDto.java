package ch.admin.seco.jobs.services.jobadservice.application.dto;

import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.LanguageSkill;

import java.util.Set;
import java.util.stream.Collectors;

public class LanguageSkillDto {

    private String languageIsoCode;
    private String spokenLevel;
    private String writtenLevel;

    protected LanguageSkillDto() {
        // For reflection libs
    }

    public LanguageSkillDto(String languageIsoCode, String spokenLevel, String writtenLevel) {
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

    public String getSpokenLevel() {
        return spokenLevel;
    }

    public void setSpokenLevel(String spokenLevel) {
        this.spokenLevel = spokenLevel;
    }

    public String getWrittenLevel() {
        return writtenLevel;
    }

    public void setWrittenLevel(String writtenLevel) {
        this.writtenLevel = writtenLevel;
    }

    public static LanguageSkillDto toDto(LanguageSkill languageSkill) {
        LanguageSkillDto languageSkillDto = new LanguageSkillDto();
        languageSkillDto.setLanguageIsoCode(languageSkill.getLanguageIsoCode());
        languageSkillDto.setSpokenLevel(languageSkill.getSpokenLevel().toString());
        languageSkillDto.setWrittenLevel(languageSkill.getWrittenLevel().toString());
        return languageSkillDto;
    }

    public static Set<LanguageSkillDto> toDto(Set<LanguageSkill> languageSkills) {
        return languageSkills.stream().map(LanguageSkillDto::toDto).collect(Collectors.toSet());
    }
}
