package ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto;

import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.Publication;

import java.time.LocalDate;

public class PublicationDto {

    private LocalDate startDate;
    private LocalDate endDate;
    private boolean eures;
    private boolean euresAnonymous;
    private boolean publicDisplay;
    private boolean publicAnonynomous;
    private boolean restrictedDisplay;
    private boolean restrictedAnonymous;

    protected PublicationDto() {
        // For reflection libs
    }

    public PublicationDto(LocalDate startDate, LocalDate endDate, boolean eures, boolean euresAnonymous, boolean publicDisplay, boolean publicAnonynomous, boolean restrictedDisplay, boolean restrictedAnonymous) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.eures = eures;
        this.euresAnonymous = euresAnonymous;
        this.publicDisplay = publicDisplay;
        this.publicAnonynomous = publicAnonynomous;
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

    public boolean isPublicDisplay() {
        return publicDisplay;
    }

    public void setPublicDisplay(boolean publicDisplay) {
        this.publicDisplay = publicDisplay;
    }

    public boolean isPublicAnonynomous() {
        return publicAnonynomous;
    }

    public void setPublicAnonynomous(boolean publicAnonynomous) {
        this.publicAnonynomous = publicAnonynomous;
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

    public static PublicationDto toDto(Publication publication) {
        PublicationDto publicationDto = new PublicationDto();
        publicationDto.setStartDate(publication.getStartDate());
        publicationDto.setEndDate(publication.getEndDate());
        publicationDto.setEures(publication.isEures());
        publicationDto.setEuresAnonymous(publication.isEuresAnonymous());
        publicationDto.setPublicDisplay(publication.isPublicDisplay());
        publicationDto.setPublicAnonynomous(publication.isPublicAnonynomous());
        publicationDto.setRestrictedDisplay(publication.isRestrictedDisplay());
        publicationDto.setRestrictedAnonymous(publication.isRestrictedAnonymous());
        return publicationDto;
    }
}
