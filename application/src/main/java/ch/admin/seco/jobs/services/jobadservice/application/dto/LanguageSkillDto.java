package ch.admin.seco.jobs.services.jobadservice.application.dto;

import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.LanguageLevel;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.LanguageSkill;

import java.util.Set;
import java.util.stream.Collectors;

public class LanguageSkillDto {

    private String languageCode;
    private String spokenLevel;
    private String writtenLevel;
    private boolean motherTongue = false; // TODO check if it is part of the LanguageLevel, or removable
    private boolean languageStayRequired = false; // TODO check if it is removable

    protected LanguageSkillDto() {
        // For reflection libs
    }

    public LanguageSkillDto(String languageCode, String spokenLevel, String writtenLevel, boolean motherTongue, boolean languageStayRequired) {
        this.languageCode = languageCode;
        this.spokenLevel = spokenLevel;
        this.writtenLevel = writtenLevel;
        this.motherTongue = motherTongue;
        this.languageStayRequired = languageStayRequired;
    }

    public String getLanguageCode() {
        return languageCode;
    }

    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
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

    public boolean isMotherTongue() {
        return motherTongue;
    }

    public void setMotherTongue(boolean motherTongue) {
        this.motherTongue = motherTongue;
    }

    public boolean isLanguageStayRequired() {
        return languageStayRequired;
    }

    public void setLanguageStayRequired(boolean languageStayRequired) {
        this.languageStayRequired = languageStayRequired;
    }

    public static LanguageSkillDto toDto(LanguageSkill languageSkill) {
        LanguageSkillDto languageSkillDto = new LanguageSkillDto();
        languageSkillDto.setLanguageCode(languageSkill.getLanguageCode());
        languageSkillDto.setSpokenLevel(languageSkill.getSpokenLevel().toString());
        languageSkillDto.setWrittenLevel(languageSkill.getWrittenLevel().toString());
        languageSkillDto.setMotherTongue(languageSkill.isMotherTongue());
        languageSkillDto.setLanguageStayRequired(languageSkill.isLanguageStayRequired());
        return languageSkillDto;
    }

    public static Set<LanguageSkillDto> toDto(Set<LanguageSkill> languageSkills) {
        return languageSkills.stream().map(LanguageSkillDto::toDto).collect(Collectors.toSet());
    }
}
