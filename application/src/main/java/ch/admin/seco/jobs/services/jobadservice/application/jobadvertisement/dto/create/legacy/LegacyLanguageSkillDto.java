package ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.create.legacy;

import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.LanguageLevel;

public class LegacyLanguageSkillDto {

    private LegacyLanguageEnum code;
    private LanguageLevel spokenLevel;
    private LanguageLevel writtenLevel;

    protected LegacyLanguageSkillDto() {
        // For reflection libs
    }

    public LegacyLanguageSkillDto(LegacyLanguageEnum code, LanguageLevel spokenLevel, LanguageLevel writtenLevel) {
        this.code = code;
        this.spokenLevel = spokenLevel;
        this.writtenLevel = writtenLevel;
    }

    public LegacyLanguageEnum getCode() {
        return code;
    }

    public void setCode(LegacyLanguageEnum code) {
        this.code = code;
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

}
