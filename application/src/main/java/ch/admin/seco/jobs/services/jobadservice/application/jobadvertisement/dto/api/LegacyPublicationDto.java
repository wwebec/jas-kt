package ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.api;

import javax.validation.constraints.NotNull;

public class LegacyPublicationDto {

    @NotNull
    private boolean jobroom;
    @NotNull
    private boolean eures;

    protected LegacyPublicationDto() {
        // For reflection libs
    }

    public LegacyPublicationDto(boolean jobroom, boolean eures) {
        this.jobroom = jobroom;
        this.eures = eures;
    }

    public boolean isJobroom() {
        return jobroom;
    }

    public void setJobroom(boolean jobroom) {
        this.jobroom = jobroom;
    }

    public boolean isEures() {
        return eures;
    }

    public void setEures(boolean eures) {
        this.eures = eures;
    }

}
