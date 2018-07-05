package ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.api;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

public class CreateApiJobAdvertisementDto {

    private boolean reportToAvam;

    private String externalUrl;

    private String externalReference;

    @NotNull
    private ApiContactDto contact;

    @NotNull
    private ApiPublicationDto publication;

    @NotNull
    @NotEmpty
    private List<ApiJobDescriptionDto> jobDescriptions;

    @NotNull
    private ApiCompanyDto company;

    private ApiEmployerDto employer;

    @NotNull
    private ApiEmploymentDto employment;

    @NotNull
    private ApiCreateLocationDto location;

    @NotNull
    private ApiOccupationDto occupation;

    private List<ApiLanguageSkillDto> languageSkills;

    private ApiApplyChannelDto applyChannel;

    private ApiPublicContactDto publicContact;

    protected CreateApiJobAdvertisementDto() {
        // For reflection libs
    }

    public CreateApiJobAdvertisementDto(boolean reportToAvam, String externalUrl, String externalReference, ApiContactDto contact, ApiPublicationDto publication, List<ApiJobDescriptionDto> jobDescriptions, ApiCompanyDto company, ApiEmployerDto employer, ApiEmploymentDto employment, ApiCreateLocationDto location, ApiOccupationDto occupation, List<ApiLanguageSkillDto> languageSkills, ApiApplyChannelDto applyChannel, ApiPublicContactDto publicContact) {
        this.reportToAvam = reportToAvam;
        this.externalUrl = externalUrl;
        this.externalReference = externalReference;
        this.contact = contact;
        this.publication = publication;
        this.jobDescriptions = jobDescriptions;
        this.company = company;
        this.employer = employer;
        this.employment = employment;
        this.location = location;
        this.occupation = occupation;
        this.languageSkills = languageSkills;
        this.applyChannel = applyChannel;
        this.publicContact = publicContact;
    }

    public boolean isReportToAvam() {
        return reportToAvam;
    }

    public void setReportToAvam(boolean reportToAvam) {
        this.reportToAvam = reportToAvam;
    }

    public String getExternalUrl() {
        return externalUrl;
    }

    public void setExternalUrl(String externalUrl) {
        this.externalUrl = externalUrl;
    }

    public String getExternalReference() {
        return externalReference;
    }

    public void setExternalReference(String externalReference) {
        this.externalReference = externalReference;
    }

    public ApiContactDto getContact() {
        return contact;
    }

    public void setContact(ApiContactDto contact) {
        this.contact = contact;
    }

    public ApiPublicationDto getPublication() {
        return publication;
    }

    public void setPublication(ApiPublicationDto publication) {
        this.publication = publication;
    }

    public List<ApiJobDescriptionDto> getJobDescriptions() {
        return jobDescriptions;
    }

    public void setJobDescriptions(List<ApiJobDescriptionDto> jobDescriptions) {
        this.jobDescriptions = jobDescriptions;
    }

    public ApiCompanyDto getCompany() {
        return company;
    }

    public void setCompany(ApiCompanyDto company) {
        this.company = company;
    }

    public ApiEmployerDto getEmployer() {
        return employer;
    }

    public void setEmployer(ApiEmployerDto employer) {
        this.employer = employer;
    }

    public ApiEmploymentDto getEmployment() {
        return employment;
    }

    public void setEmployment(ApiEmploymentDto employment) {
        this.employment = employment;
    }

    public ApiCreateLocationDto getLocation() {
        return location;
    }

    public void setLocation(ApiCreateLocationDto location) {
        this.location = location;
    }

    public ApiOccupationDto getOccupation() {
        return occupation;
    }

    public void setOccupation(ApiOccupationDto occupation) {
        this.occupation = occupation;
    }

    public List<ApiLanguageSkillDto> getLanguageSkills() {
        return languageSkills;
    }

    public void setLanguageSkills(List<ApiLanguageSkillDto> languageSkills) {
        this.languageSkills = languageSkills;
    }

    public ApiApplyChannelDto getApplyChannel() {
        return applyChannel;
    }

    public void setApplyChannel(ApiApplyChannelDto applyChannel) {
        this.applyChannel = applyChannel;
    }

    public ApiPublicContactDto getPublicContact() {
        return publicContact;
    }

    public void setPublicContact(ApiPublicContactDto publicContact) {
        this.publicContact = publicContact;
    }
}
