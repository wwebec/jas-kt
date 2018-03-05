package ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement;

import ch.admin.seco.jobs.services.jobadservice.core.conditions.Condition;
import ch.admin.seco.jobs.services.jobadservice.core.domain.ValueObject;

import javax.persistence.*;
import java.util.Objects;

@Embeddable
@Access(AccessType.FIELD)
public class Occupation implements ValueObject<Occupation> {

    private String avamCode;

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

    public Occupation(String avamCode) {
        this.avamCode = Condition.notBlank(avamCode);
    }

    public Occupation(String avamCode, WorkExperience workExperience, String educationCode) {
        this(avamCode);
        this.workExperience = workExperience;
        this.educationCode = educationCode;
    }

    public Occupation(String avamCode, String sbn3Code, String sbn5Code, String bfsCode, String label, WorkExperience workExperience, String educationCode) {
        this(avamCode, workExperience, educationCode);
        this.sbn3Code = sbn3Code;
        this.sbn5Code = sbn5Code;
        this.bfsCode = bfsCode;
        this.label = label;
    }

    public String getAvamCode() {
        return avamCode;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Occupation that = (Occupation) o;
        return Objects.equals(avamCode, that.avamCode) &&
                Objects.equals(sbn3Code, that.sbn3Code) &&
                Objects.equals(sbn5Code, that.sbn5Code) &&
                Objects.equals(bfsCode, that.bfsCode) &&
                Objects.equals(label, that.label) &&
                workExperience == that.workExperience &&
                Objects.equals(educationCode, that.educationCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(avamCode, sbn3Code, sbn5Code, bfsCode, label, workExperience, educationCode);
    }

    @Override
    public String toString() {
        return "Occupation{" +
                "avamCode='" + avamCode + '\'' +
                ", sbn3Code='" + sbn3Code + '\'' +
                ", sbn5Code='" + sbn5Code + '\'' +
                ", bfsCode='" + bfsCode + '\'' +
                ", label='" + label + '\'' +
                ", workExperience=" + workExperience +
                ", educationCode='" + educationCode + '\'' +
                '}';
    }
}
