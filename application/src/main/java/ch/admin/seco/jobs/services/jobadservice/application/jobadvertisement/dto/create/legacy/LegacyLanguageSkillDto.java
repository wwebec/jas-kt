package ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.create.legacy;

public class LegacyLanguageSkillDto {

    private String language;
    private String spokenLevel;
    private String writtenLevel;

    protected LegacyLanguageSkillDto() {
        // For reflection libs
    }

    public LegacyLanguageSkillDto(String language, String spokenLevel, String writtenLevel) {
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

}
