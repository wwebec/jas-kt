package ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement;

import java.util.Objects;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import ch.admin.seco.jobs.services.jobadservice.core.conditions.Condition;
import ch.admin.seco.jobs.services.jobadservice.core.domain.ValueObject;

@Embeddable
@Access(AccessType.FIELD)
public class Occupation implements ValueObject<Occupation> {

    private String avamOccupationCode;

    @Column(name = "SBN3_CODE")
    private String sbn3Code;

    @Column(name = "SBN5_CODE")
    private String sbn5Code;

    private String bfsCode;

    private String label;

    @Enumerated(EnumType.STRING)
    private WorkExperience workExperience;

    private String educationCode;

    protected Occupation() {
        // For reflection libs
    }

    public Occupation(String avamOccupationCode) {
        this.avamOccupationCode = Condition.notBlank(avamOccupationCode);
    }

    public Occupation(String avamOccupationCode, WorkExperience workExperience, String educationCode) {
        this(avamOccupationCode);
        this.workExperience = workExperience;
        this.educationCode = educationCode;
    }

    public Occupation(String avamOccupationCode, String sbn3Code, String sbn5Code, String bfsCode, String label, WorkExperience workExperience, String educationCode) {
        this(avamOccupationCode, workExperience, educationCode);
        this.sbn3Code = sbn3Code;
        this.sbn5Code = sbn5Code;
        this.bfsCode = bfsCode;
        this.label = label;
    }

    public String getAvamOccupationCode() {
        return avamOccupationCode;
    }

    public String getSbn3Code() {
        return sbn3Code;
    }

    public String getSbn5Code() {
        return sbn5Code;
    }

    public String getBfsCode() {
        return bfsCode;
    }

    public String getLabel() {
        return label;
    }

    public WorkExperience getWorkExperience() {
        return workExperience;
    }

    public String getEducationCode() {
        return educationCode;
    }

    @Override
    public boolean sameValueObjectAs(Occupation other) {
        return equals(other);
    }

    @Override
    public int hashCode() {
        return Objects.hash(avamOccupationCode, sbn3Code, sbn5Code, bfsCode, label, workExperience, educationCode);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Occupation that = (Occupation) o;
        return Objects.equals(avamOccupationCode, that.avamOccupationCode) &&
                Objects.equals(sbn3Code, that.sbn3Code) &&
                Objects.equals(sbn5Code, that.sbn5Code) &&
                Objects.equals(bfsCode, that.bfsCode) &&
                Objects.equals(label, that.label) &&
                workExperience == that.workExperience &&
                Objects.equals(educationCode, that.educationCode);
    }

    @Override
    public String toString() {
        return "Occupation{" +
                "avamOccupationCode='" + avamOccupationCode + '\'' +
                ", sbn3Code='" + sbn3Code + '\'' +
                ", sbn5Code='" + sbn5Code + '\'' +
                ", bfsCode='" + bfsCode + '\'' +
                ", label='" + label + '\'' +
                ", workExperience=" + workExperience +
                ", educationCode='" + educationCode + '\'' +
                '}';
    }
}
