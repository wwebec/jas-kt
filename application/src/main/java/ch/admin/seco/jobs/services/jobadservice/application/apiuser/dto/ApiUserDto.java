package ch.admin.seco.jobs.services.jobadservice.application.apiuser.dto;

import ch.admin.seco.jobs.services.jobadservice.domain.apiuser.ApiUser;

import java.time.LocalDate;

public class ApiUserDto {
    private String id;
    private String username;
    private String companyName;
    private String companyEmail;
    private String technicalContactName;
    private String technicalContactEmail;
    private boolean active;
    private LocalDate createDate;
    private LocalDate lastAccessDate;

    protected ApiUserDto() {
        // For reflection libs
    }

    public ApiUserDto(String id, String username, String companyName, String companyEmail, String technicalContactName, String technicalContactEmail, boolean active, LocalDate createDate, LocalDate lastAccessDate) {
        this.id = id;
        this.username = username;
        this.companyName = companyName;
        this.companyEmail = companyEmail;
        this.technicalContactName = technicalContactName;
        this.technicalContactEmail = technicalContactEmail;
        this.active = active;
        this.createDate = createDate;
        this.lastAccessDate = lastAccessDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyEmail() {
        return companyEmail;
    }

    public void setCompanyEmail(String companyEmail) {
        this.companyEmail = companyEmail;
    }

    public String getTechnicalContactName() {
        return technicalContactName;
    }

    public void setTechnicalContactName(String technicalContactName) {
        this.technicalContactName = technicalContactName;
    }

    public String getTechnicalContactEmail() {
        return technicalContactEmail;
    }

    public void setTechnicalContactEmail(String technicalContactEmail) {
        this.technicalContactEmail = technicalContactEmail;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public LocalDate getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDate createDate) {
        this.createDate = createDate;
    }

    public LocalDate getLastAccessDate() {
        return lastAccessDate;
    }

    public void setLastAccessDate(LocalDate lastAccessDate) {
        this.lastAccessDate = lastAccessDate;
    }

    public static ApiUserDto toDto(ApiUser apiUser) {
        ApiUserDto dto = new ApiUserDto();
        dto.setId(apiUser.getId().getValue());
        dto.setUsername(apiUser.getUsername());
        dto.setCompanyEmail(apiUser.getCompanyEmail());
        dto.setCompanyName(apiUser.getCompanyName());
        dto.setTechnicalContactName(apiUser.getTechnicalContactName());
        dto.setTechnicalContactEmail(apiUser.getTechnicalContactEmail());
        dto.setActive(apiUser.isActive());
        dto.setCreateDate(apiUser.getCreateDate());
        dto.setLastAccessDate(apiUser.getLastAccessDate());
        return dto;
    }
}
