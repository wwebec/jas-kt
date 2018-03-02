package ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement;

import ch.admin.seco.jobs.services.jobadservice.core.domain.ValueObject;
import ch.admin.seco.jobs.services.jobadservice.domain.profession.ProfessionCode;

import javax.persistence.*;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Embeddable
@Access(AccessType.FIELD)
public class Occupation implements ValueObject<Occupation> {

    private String avamCode;

    @Valid
    @ElementCollection(fetch = FetchType.EAGER)
    private List<ProfessionCode> professionCodes;

    @Enumerated(EnumType.STRING)
    private WorkExperience workExperience;

    private String educationCode;

    protected Occupation() {
        // For reflection libs
    }

    public Occupation(String avamCode, WorkExperience workExperience, String educationCode) {
        this(avamCode, new ArrayList<>(), workExperience, educationCode);
    }

    public Occupation(String avamCode, List<ProfessionCode> professionCodes, WorkExperience workExperience, String educationCode) {
        this.avamCode = avamCode;
        this.workExperience = workExperience;
        this.educationCode = educationCode;
        this.professionCodes = professionCodes;
    }

    public String getAvamCode() {
        return avamCode;
    }

    public List<ProfessionCode> getProfessionCodes() {
        return professionCodes;
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
                workExperience == that.workExperience &&
                Objects.equals(educationCode, that.educationCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(avamCode, workExperience, educationCode);
    }

}
