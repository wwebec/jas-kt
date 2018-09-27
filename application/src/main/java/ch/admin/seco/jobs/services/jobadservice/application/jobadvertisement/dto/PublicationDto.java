package ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto;

import java.time.LocalDate;

import javax.validation.constraints.NotNull;

import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.Publication;

public class PublicationDto {

    @NotNull
    private LocalDate startDate;

    private LocalDate endDate;

    private boolean euresDisplay;

    private boolean euresAnonymous;

    private boolean publicDisplay;

    private boolean restrictedDisplay;

    private boolean companyAnonymous;

    public LocalDate getStartDate() {
        return startDate;
    }

    public PublicationDto setStartDate(LocalDate startDate) {
        this.startDate = startDate;
        return this;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public PublicationDto setEndDate(LocalDate endDate) {
        this.endDate = endDate;
        return this;
    }

    public boolean isEuresDisplay() {
        return euresDisplay;
    }

    public PublicationDto setEuresDisplay(boolean euresDisplay) {
        this.euresDisplay = euresDisplay;
        return this;
    }

    public boolean isEuresAnonymous() {
        return euresAnonymous;
    }

    public PublicationDto setEuresAnonymous(boolean euresAnonymous) {
        this.euresAnonymous = euresAnonymous;
        return this;
    }

    public boolean isPublicDisplay() {
        return publicDisplay;
    }

    public PublicationDto setPublicDisplay(boolean publicDisplay) {
        this.publicDisplay = publicDisplay;
        return this;
    }

    public boolean isRestrictedDisplay() {
        return restrictedDisplay;
    }

    public PublicationDto setRestrictedDisplay(boolean restrictedDisplay) {
        this.restrictedDisplay = restrictedDisplay;
        return this;
    }

    public boolean isCompanyAnonymous() {
        return companyAnonymous;
    }

    public PublicationDto setCompanyAnonymous(boolean companyAnonymous) {
        this.companyAnonymous = companyAnonymous;
        return this;
    }

    public static PublicationDto toDto(Publication publication) {
        return new PublicationDto()
                .setStartDate(publication.getStartDate())
                .setEndDate(publication.getEndDate())
                .setEuresDisplay(publication.isEuresDisplay())
                .setEuresAnonymous(publication.isEuresAnonymous())
                .setPublicDisplay(publication.isPublicDisplay())
                .setRestrictedDisplay(publication.isRestrictedDisplay())
                .setCompanyAnonymous(publication.isCompanyAnonymous());
    }
}
