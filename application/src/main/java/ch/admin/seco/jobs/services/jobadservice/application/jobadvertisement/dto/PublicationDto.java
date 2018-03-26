package ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto;

import java.time.LocalDate;

public class PublicationDto {

    private boolean eures;
    private boolean euresAnonymous;
    private boolean publicAnonymous;
    private boolean publicPublication;
    private boolean restrictedAnonymous;
    private boolean restrictedPublication;

    protected PublicationDto() {
        // For reflection libs
    }

    public PublicationDto(boolean eures, boolean euresAnonymous, boolean publicAnonymous,
            boolean publicPublication, boolean restrictedAnonymous, boolean restrictedPublication) {
        this.eures = eures;
        this.euresAnonymous = euresAnonymous;
        this.publicAnonymous = publicAnonymous;
        this.publicPublication = publicPublication;
        this.restrictedAnonymous = restrictedAnonymous;
        this.restrictedPublication = restrictedPublication;
    }

    public boolean isEures() {
        return eures;
    }

    public void setEures(boolean eures) {
        this.eures = eures;
    }

    public boolean isEuresAnonymous() {
        return euresAnonymous;
    }

    public void setEuresAnonymous(boolean euresAnonymous) {
        this.euresAnonymous = euresAnonymous;
    }

    public boolean isPublicAnonymous() {
        return publicAnonymous;
    }

    public void setPublicAnonymous(boolean publicAnonymous) {
        this.publicAnonymous = publicAnonymous;
    }

    public boolean isPublicPublication() {
        return publicPublication;
    }

    public void setPublicPublication(boolean publicPublication) {
        this.publicPublication = publicPublication;
    }

    public boolean isRestrictedAnonymous() {
        return restrictedAnonymous;
    }

    public void setRestrictedAnonymous(boolean restrictedAnonymous) {
        this.restrictedAnonymous = restrictedAnonymous;
    }

    public boolean isRestrictedPublication() {
        return restrictedPublication;
    }

    public void setRestrictedPublication(boolean restrictedPublication) {
        this.restrictedPublication = restrictedPublication;
    }
}
