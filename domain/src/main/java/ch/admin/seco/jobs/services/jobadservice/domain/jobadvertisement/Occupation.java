package ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement;

import ch.admin.seco.jobs.services.jobadservice.core.domain.ValueObject;
import ch.admin.seco.jobs.services.jobadservice.domain.profession.ProfessionId;

import javax.persistence.*;
import java.util.Objects;

@Embeddable
@Access(AccessType.FIELD)
public class Occupation implements ValueObject<Occupation> {

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "PROFESSION_ID"))
    private ProfessionId professionId;

    @Enumerated(EnumType.STRING)
    private WorkExperience workExperience;

    protected Occupation() {
        // For reflection libs
    }

    public Occupation(ProfessionId professionId, WorkExperience workExperience) {
        this.professionId = professionId;
        this.workExperience = workExperience;
    }

    public ProfessionId getProfessionId() {
        return professionId;
    }

    public WorkExperience getWorkExperience() {
        return workExperience;
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
        return Objects.equals(professionId, that.professionId) &&
                workExperience == that.workExperience;
    }

    @Override
    public int hashCode() {
        return Objects.hash(professionId, workExperience);
    }

}
