package ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.api;

public class LegacyLanguageSkillDto {

    private LegacyLanguageEnum code;
    private LegacyLanguageLevelEnum spokenLevel;
    private LegacyLanguageLevelEnum writtenLevel;

    protected LegacyLanguageSkillDto() {
        // For reflection libs
    }

    public LegacyLanguageSkillDto(LegacyLanguageEnum code, LegacyLanguageLevelEnum spokenLevel, LegacyLanguageLevelEnum writtenLevel) {
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

    public LegacyLanguageLevelEnum getSpokenLevel() {
        return spokenLevel;
    }

    public void setSpokenLevel(LegacyLanguageLevelEnum spokenLevel) {
        this.spokenLevel = spokenLevel;
    }

    public LegacyLanguageLevelEnum getWrittenLevel() {
        return writtenLevel;
    }

    public void setWrittenLevel(LegacyLanguageLevelEnum writtenLevel) {
        this.writtenLevel = writtenLevel;
    }

}
