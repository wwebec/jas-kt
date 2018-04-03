package ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.create.legacy;

public class LegacyLanguageSkillDto {

    private String language;
    private LegacyLanguageLevelEnum spokenLevel;
    private LegacyLanguageLevelEnum writtenLevel;

    protected LegacyLanguageSkillDto() {
        // For reflection libs
    }

    public LegacyLanguageSkillDto(String language, LegacyLanguageLevelEnum spokenLevel, LegacyLanguageLevelEnum writtenLevel) {
        this.language = language;
        this.spokenLevel = spokenLevel;
        this.writtenLevel = writtenLevel;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
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
