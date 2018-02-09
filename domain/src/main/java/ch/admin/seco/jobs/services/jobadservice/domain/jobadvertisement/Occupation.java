package ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement;

import ch.admin.seco.jobs.services.jobadservice.core.domain.ValueObject;
import ch.admin.seco.jobs.services.jobadservice.domain.profession.ProfessionId;

import java.util.Objects;
import java.util.Set;

public class Occupation implements ValueObject<Occupation> {

    private ProfessionId professionId;
    private WorkExperience workExperience;
    private Set<String> professionCodes;

    protected Occupation() {
    }

    public Occupation(ProfessionId professionId, WorkExperience workExperience, Set<String> professionCodes) {
        this.professionId = professionId;
        this.workExperience = workExperience;
        this.professionCodes = professionCodes;
    }

    public ProfessionId getProfessionId() {
        return professionId;
    }

    public WorkExperience getWorkExperience() {
        return workExperience;
    }

    public Set<String> getProfessionCodes() {
        return professionCodes;
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
                workExperience == that.workExperience &&
                Objects.equals(professionCodes, that.professionCodes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(professionId, workExperience, professionCodes);
    }

}
