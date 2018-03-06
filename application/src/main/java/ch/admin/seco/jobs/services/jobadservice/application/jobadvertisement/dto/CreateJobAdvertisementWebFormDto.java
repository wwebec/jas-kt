package ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto;

import java.util.List;

import javax.validation.constraints.NotNull;

public class CreateJobAdvertisementWebFormDto {

    private boolean eures;

    @NotNull
    private String title;

    @NotNull
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
    private LocalityDto locality;

    @NotNull
    private OccupationDto occupation;

    private List<LanguageSkillDto> languageSkills;

    protected CreateJobAdvertisementWebFormDto() {
        // For reflection libs
    }

    public CreateJobAdvertisementWebFormDto(boolean eures, String title, String description, EmploymentDto employment, String drivingLicenseLevel, ApplyChannelDto applyChannel, CompanyDto company, ContactDto contact, LocalityDto locality, OccupationDto occupation, List<LanguageSkillDto> languageSkills) {
        this.eures = eures;
        this.title = title;
        this.description = description;
        this.employment = employment;
        this.drivingLicenseLevel = drivingLicenseLevel;
        this.applyChannel = applyChannel;
        this.company = company;
        this.contact = contact;
        this.locality = locality;
        this.occupation = occupation;
        this.languageSkills = languageSkills;
    }

    public boolean isEures() {
        return eures;
    }

    public void setEures(boolean eures) {
        this.eures = eures;
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

    public LocalityDto getLocality() {
        return locality;
    }

    public void setLocality(LocalityDto locality) {
        this.locality = locality;
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
