package ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement;

import ch.admin.seco.jobs.services.jobadservice.domain.profession.ProfessionId;

import java.util.Set;

public class Occupation {

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
}
