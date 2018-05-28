package ch.admin.seco.jobs.services.jobadservice.application.apiuser.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public class UpdateDetailsApiUserDto {

    @NotBlank
    private String username;

    @NotBlank
    private String companyName;

    @NotBlank
    @Email
    private String companyEmail;

    @NotBlank
    private String technicalContactName;

    @NotBlank
    @Email
    private String technicalContactEmail;

    protected UpdateDetailsApiUserDto() {
        // For reflection libs
    }

    public UpdateDetailsApiUserDto(String username, String companyName, String companyEmail, String technicalContactName, String technicalContactEmail) {
        this.username = username;
        this.companyName = companyName;
        this.companyEmail = companyEmail;
        this.technicalContactName = technicalContactName;
        this.technicalContactEmail = technicalContactEmail;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCompanyEmail() {
        return companyEmail;
    }

    public void setCompanyEmail(String companyEmail) {
        this.companyEmail = companyEmail;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
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

}
