package ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement;

import java.util.Objects;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import ch.admin.seco.jobs.services.jobadservice.core.domain.ValueObject;

@Embeddable
@Access(AccessType.FIELD)
public class LanguageSkill implements ValueObject<LanguageSkill> {

    private String languageIsoCode;

    @Enumerated(EnumType.STRING)
    private LanguageLevel spokenLevel;

    @Enumerated(EnumType.STRING)
    private LanguageLevel writtenLevel;

    protected LanguageSkill() {
        // For reflection libs
    }

    public LanguageSkill(String languageIsoCode, LanguageLevel spokenLevel, LanguageLevel writtenLevel) {
        this.languageIsoCode = languageIsoCode;
        this.spokenLevel = spokenLevel;
        this.writtenLevel = writtenLevel;
    }

    public String getLanguageIsoCode() {
        return languageIsoCode;
    }

    public LanguageLevel getSpokenLevel() {
        return spokenLevel;
    }

    public LanguageLevel getWrittenLevel() {
        return writtenLevel;
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
        return Objects.equals(languageIsoCode, that.languageIsoCode) &&
                spokenLevel == that.spokenLevel &&
                writtenLevel == that.writtenLevel;
    }

    @Override
    public int hashCode() {
        return Objects.hash(languageIsoCode, spokenLevel, writtenLevel);
    }

    @Override
    public String toString() {
        return "LanguageSkill{" +
                "languageIsoCode='" + languageIsoCode + '\'' +
                ", spokenLevel=" + spokenLevel +
                ", writtenLevel=" + writtenLevel +
                '}';
    }
}
