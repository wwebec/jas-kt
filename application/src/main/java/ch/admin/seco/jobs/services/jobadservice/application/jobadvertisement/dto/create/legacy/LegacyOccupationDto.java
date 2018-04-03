package ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.create.legacy;

import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.WorkExperience;

import javax.validation.constraints.NotNull;

public class LegacyOccupationDto {

    @NotNull
    private String avamOccupation;
    private LegacyDegreeEnum degree;
    private LegacyExperienceEnum experience;

    protected LegacyOccupationDto() {
        // For reflection libs
    }

    public LegacyOccupationDto(String avamOccupation, LegacyDegreeEnum degree, LegacyExperienceEnum experience) {
        this.avamOccupation = avamOccupation;
        this.degree = degree;
        this.experience = experience;
    }

    public String getAvamOccupation() {
        return avamOccupation;
    }

    public void setAvamOccupation(String avamOccupation) {
        this.avamOccupation = avamOccupation;
    }

    public LegacyDegreeEnum getDegree() {
        return degree;
    }

    public void setDegree(LegacyDegreeEnum degree) {
        this.degree = degree;
    }

    public LegacyExperienceEnum getExperience() {
        return experience;
    }

    public void setExperience(LegacyExperienceEnum experience) {
        this.experience = experience;
    }

}
