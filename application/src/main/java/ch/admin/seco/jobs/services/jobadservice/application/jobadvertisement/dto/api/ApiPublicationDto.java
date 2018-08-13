package ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.api;

import java.time.LocalDate;

import javax.validation.constraints.NotNull;

import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.Publication;

public class ApiPublicationDto {

    @NotNull
    private LocalDate startDate;

    private LocalDate endDate;

    private boolean euresDisplay;

    private boolean euresAnonymous;

    private boolean publicDisplay;

    private boolean publicAnonymous;

    private boolean restrictedDisplay;

    private boolean restrictedAnonymous;

    protected ApiPublicationDto() {
        // For reflection libs
    }

    public ApiPublicationDto(LocalDate startDate, LocalDate endDate, boolean euresDisplay, boolean euresAnonymous, boolean publicDisplay, boolean publicAnonymous, boolean restrictedDisplay, boolean restrictedAnonymous) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.euresDisplay = euresDisplay;
        this.euresAnonymous = euresAnonymous;
        this.publicDisplay = publicDisplay;
        this.publicAnonymous = publicAnonymous;
        this.restrictedDisplay = restrictedDisplay;
        this.restrictedAnonymous = restrictedAnonymous;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public boolean isEuresDisplay() {
        return euresDisplay;
    }

    public void setEuresDisplay(boolean euresDisplay) {
        this.euresDisplay = euresDisplay;
    }

    public boolean isEuresAnonymous() {
        return euresAnonymous;
    }

    public void setEuresAnonymous(boolean euresAnonymous) {
        this.euresAnonymous = euresAnonymous;
    }

    public boolean isPublicDisplay() {
        return publicDisplay;
    }

    public void setPublicDisplay(boolean publicDisplay) {
        this.publicDisplay = publicDisplay;
    }

    public boolean isPublicAnonymous() {
        return publicAnonymous;
    }

    public void setPublicAnonymous(boolean publicAnonymous) {
        this.publicAnonymous = publicAnonymous;
    }

    public boolean isRestrictedDisplay() {
        return restrictedDisplay;
    }

    public void setRestrictedDisplay(boolean restrictedDisplay) {
        this.restrictedDisplay = restrictedDisplay;
    }

    public boolean isRestrictedAnonymous() {
        return restrictedAnonymous;
    }

    public void setRestrictedAnonymous(boolean restrictedAnonymous) {
        this.restrictedAnonymous = restrictedAnonymous;
    }

    public static ApiPublicationDto toDto(Publication publication) {
        ApiPublicationDto publicationDto = new ApiPublicationDto();
        publicationDto.setStartDate(publication.getStartDate());
        publicationDto.setEndDate(publication.getEndDate());
        publicationDto.setEuresDisplay(publication.isEuresDisplay());
        publicationDto.setEuresAnonymous(publication.isEuresAnonymous());
        publicationDto.setPublicDisplay(publication.isPublicDisplay());
        publicationDto.setPublicAnonymous(publication.isPublicAnonymous());
        publicationDto.setRestrictedDisplay(publication.isRestrictedDisplay());
        publicationDto.setRestrictedAnonymous(publication.isRestrictedAnonymous());
        return publicationDto;
    }
}
