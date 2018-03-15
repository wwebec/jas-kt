package ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

public class CreateJobAdvertisementWebFormDto {

    private boolean eures;

    @NotBlank
    private String languageIsoCode;

    @NotBlank
    private String title;

    @NotBlank
    private String description;

    @NotNull
    private EmploymentDto employment;

    private String drivingLicenseLevel;

    private ApplyChannelDto applyChannel;

    @NotNull
    private CompanyDto company;

    @NotNull
    private ContactDto contact;

    @NotNull
    private CreateLocationDto location;

    @NotNull
    private OccupationDto occupation;

    private List<LanguageSkillDto> languageSkills;

    protected CreateJobAdvertisementWebFormDto() {
        // For reflection libs
    }

    public CreateJobAdvertisementWebFormDto(boolean eures, String languageIsoCode, String title, String description, EmploymentDto employment, String drivingLicenseLevel, ApplyChannelDto applyChannel, CompanyDto company, ContactDto contact, CreateLocationDto location, OccupationDto occupation, List<LanguageSkillDto> languageSkills) {
        this.eures = eures;
        this.languageIsoCode = languageIsoCode;
        this.title = title;
        this.description = description;
        this.employment = employment;
        this.drivingLicenseLevel = drivingLicenseLevel;
        this.applyChannel = applyChannel;
        this.company = company;
        this.contact = contact;
        this.location = location;
        this.occupation = occupation;
        this.languageSkills = languageSkills;
    }

    public boolean isEures() {
        return eures;
    }

    public void setEures(boolean eures) {
        this.eures = eures;
    }

    public String getLanguageIsoCode() {
        return languageIsoCode;
    }

    public void setLanguageIsoCode(String languageIsoCode) {
        this.languageIsoCode = languageIsoCode;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public EmploymentDto getEmployment() {
        return employment;
    }

    public void setEmployment(EmploymentDto employment) {
        this.employment = employment;
    }

    public String getDrivingLicenseLevel() {
        return drivingLicenseLevel;
    }

    public void setDrivingLicenseLevel(String drivingLicenseLevel) {
        this.drivingLicenseLevel = drivingLicenseLevel;
    }

    public ApplyChannelDto getApplyChannel() {
        return applyChannel;
    }

    public void setApplyChannel(ApplyChannelDto applyChannel) {
        this.applyChannel = applyChannel;
    }

    public CompanyDto getCompany() {
        return company;
    }

    public void setCompany(CompanyDto company) {
        this.company = company;
    }

    public ContactDto getContact() {
        return contact;
    }

    public void setContact(ContactDto contact) {
        this.contact = contact;
    }

    public CreateLocationDto getLocation() {
        return location;
    }

    public void setLocation(CreateLocationDto location) {
        this.location = location;
    }

    public OccupationDto getOccupation() {
        return occupation;
    }

    public void setOccupation(OccupationDto occupation) {
        this.occupation = occupation;
    }

    public List<LanguageSkillDto> getLanguageSkills() {
        return languageSkills;
    }

    public void setLanguageSkills(List<LanguageSkillDto> languageSkills) {
        this.languageSkills = languageSkills;
    }

}
