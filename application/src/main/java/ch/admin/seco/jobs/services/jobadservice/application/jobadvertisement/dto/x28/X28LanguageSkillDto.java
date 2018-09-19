package ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.x28;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.constraints.NotBlank;

import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.LanguageSkillDto;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.LanguageLevel;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.LanguageSkill;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.utils.SupportedLanguageIsoCode;

public class X28LanguageSkillDto {

    @NotBlank
    @SupportedLanguageIsoCode
    private String languageIsoCode;

    private LanguageLevel spokenLevel;

    private LanguageLevel writtenLevel;

    protected X28LanguageSkillDto() {
        // For reflection libs
    }

    public X28LanguageSkillDto(String languageIsoCode, LanguageLevel spokenLevel, LanguageLevel writtenLevel) {
        this.languageIsoCode = languageIsoCode;
        this.spokenLevel = spokenLevel;
        this.writtenLevel = writtenLevel;
    }

    public LanguageSkillDto toLanguageSkillDto() {
        return new LanguageSkillDto(this.languageIsoCode, this.spokenLevel, this.writtenLevel);
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

    public static X28LanguageSkillDto toDto(LanguageSkill languageSkill) {
        X28LanguageSkillDto languageSkillDto = new X28LanguageSkillDto();
        languageSkillDto.setLanguageIsoCode(languageSkill.getLanguageIsoCode());
        languageSkillDto.setSpokenLevel(languageSkill.getSpokenLevel());
        languageSkillDto.setWrittenLevel(languageSkill.getWrittenLevel());
        return languageSkillDto;
    }

    public static List<X28LanguageSkillDto> toDto(List<LanguageSkill> languageSkills) {
        if (languageSkills != null) {
            return languageSkills.stream().map(X28LanguageSkillDto::toDto).collect(Collectors.toList());
        }

        return null;
    }
}
