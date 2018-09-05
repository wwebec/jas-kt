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

    private boolean restrictedDisplay;

    private boolean companyAnonymous;

    protected ApiPublicationDto() {
        // For reflection libs
    }

    public ApiPublicationDto(LocalDate startDate, LocalDate endDate, boolean euresDisplay, boolean euresAnonymous, boolean publicDisplay, boolean restrictedDisplay, boolean companyAnonymous) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.euresDisplay = euresDisplay;
        this.euresAnonymous = euresAnonymous;
        this.publicDisplay = publicDisplay;
        this.restrictedDisplay = restrictedDisplay;
        this.companyAnonymous = companyAnonymous;
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

    public boolean isRestrictedDisplay() {
        return restrictedDisplay;
    }

    public void setRestrictedDisplay(boolean restrictedDisplay) {
        this.restrictedDisplay = restrictedDisplay;
    }

    public boolean isCompanyAnonymous() {
        return companyAnonymous;
    }

    public void setCompanyAnonymous(boolean companyAnonymous) {
        this.companyAnonymous = companyAnonymous;
    }

    public static ApiPublicationDto toDto(Publication publication) {
        ApiPublicationDto publicationDto = new ApiPublicationDto();
        publicationDto.setStartDate(publication.getStartDate());
        publicationDto.setEndDate(publication.getEndDate());
        publicationDto.setEuresDisplay(publication.isEuresDisplay());
        publicationDto.setEuresAnonymous(publication.isEuresAnonymous());
        publicationDto.setPublicDisplay(publication.isPublicDisplay());
        publicationDto.setRestrictedDisplay(publication.isRestrictedDisplay());
        publicationDto.setCompanyAnonymous(publication.isCompanyAnonymous());
        return publicationDto;
    }
}
