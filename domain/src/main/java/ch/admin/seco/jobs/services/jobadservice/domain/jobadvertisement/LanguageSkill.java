package ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement;

public class LanguageSkill {

    private String languageCode;
    private LanguageLevel spokenLevel;
    private LanguageLevel writtenLevel;
    private boolean motherTongue = false; // TODO check if it is part of the LanguageLevel, or removable
    private boolean languageStayRequired = false; // TODO check if it is removable

    protected LanguageSkill() {
    }

    public LanguageSkill(String languageCode, LanguageLevel spokenLevel, LanguageLevel writtenLevel, boolean motherTongue, boolean languageStayRequired) {
        this.languageCode = languageCode;
        this.spokenLevel = spokenLevel;
        this.writtenLevel = writtenLevel;
        this.motherTongue = motherTongue;
        this.languageStayRequired = languageStayRequired;
    }

    public String getLanguageCode() {
        return languageCode;
    }

    public LanguageLevel getSpokenLevel() {
        return spokenLevel;
    }

    public LanguageLevel getWrittenLevel() {
        return writtenLevel;
    }

    public boolean isMotherTongue() {
        return motherTongue;
    }

    public boolean isLanguageStayRequired() {
        return languageStayRequired;
    }
}
