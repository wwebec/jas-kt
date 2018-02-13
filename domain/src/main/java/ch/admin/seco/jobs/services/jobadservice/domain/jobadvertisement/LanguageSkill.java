package ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement;

import ch.admin.seco.jobs.services.jobadservice.core.domain.ValueObject;

import java.util.Objects;

public class LanguageSkill implements ValueObject<LanguageSkill> {

    private String languageCode;
    private LanguageLevel spokenLevel;
    private LanguageLevel writtenLevel;
    private boolean motherTongue = false; // TODO check if it is part of the LanguageLevel, or removable
    private boolean languageStayRequired = false; // TODO check if it is removable

    protected LanguageSkill() {
        // For reflection libs
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

    @Override
    public boolean sameValueObjectAs(LanguageSkill other) {
        return equals(other);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LanguageSkill that = (LanguageSkill) o;
        return motherTongue == that.motherTongue &&
                languageStayRequired == that.languageStayRequired &&
                Objects.equals(languageCode, that.languageCode) &&
                spokenLevel == that.spokenLevel &&
                writtenLevel == that.writtenLevel;
    }

    @Override
    public int hashCode() {
        return Objects.hash(languageCode, spokenLevel, writtenLevel, motherTongue, languageStayRequired);
    }

}
