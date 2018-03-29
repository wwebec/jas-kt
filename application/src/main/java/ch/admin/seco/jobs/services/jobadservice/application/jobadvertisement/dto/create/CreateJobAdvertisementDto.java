package ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.create;

import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.*;

import javax.validation.constraints.NotNull;
import java.util.List;

public class CreateJobAdvertisementDto {

    private boolean reportToAvam;

    private String externalUrl;

    @NotNull
    private ContactDto contact;

    @NotNull
    private PublicationDto publication;

    @NotNull
    private List<JobDescriptionDto> jobDescriptions;

    @NotNull
    private CompanyDto company;

    private EmployerDto employer;

    @NotNull
    private EmploymentDto employment;

    @NotNull
    private CreateLocationDto location;

    @NotNull
    private OccupationDto occupation;

    @NotNull
    private List<LanguageSkillDto> languageSkills;

    private ApplyChannelDto applyChannel;

    @NotNull
    private PublicContactDto publicContact;

    protected CreateJobAdvertisementDto() {
        // For reflection libs
    }

    public CreateJobAdvertisementDto(boolean reportToAvam, String externalUrl, @NotNull ContactDto contact, @NotNull PublicationDto publication, @NotNull List<JobDescriptionDto> jobDescriptions, @NotNull CompanyDto company, EmployerDto employer, @NotNull EmploymentDto employment, @NotNull CreateLocationDto location, @NotNull OccupationDto occupation, @NotNull List<LanguageSkillDto> languageSkills, ApplyChannelDto applyChannel, @NotNull PublicContactDto publicContact) {
        this.reportToAvam = reportToAvam;
        this.externalUrl = externalUrl;
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

    public ContactDto getContact() {
        return contact;
    }

    public void setContact(ContactDto contact) {
        this.contact = contact;
    }

    public PublicationDto getPublication() {
        return publication;
    }

    public void setPublication(PublicationDto publication) {
        this.publication = publication;
    }

    public List<JobDescriptionDto> getJobDescriptions() {
        return jobDescriptions;
    }

    public void setJobDescriptions(List<JobDescriptionDto> jobDescriptions) {
        this.jobDescriptions = jobDescriptions;
    }

    public CompanyDto getCompany() {
        return company;
    }

    public void setCompany(CompanyDto company) {
        this.company = company;
    }

    public EmployerDto getEmployer() {
        return employer;
    }

    public void setEmployer(EmployerDto employer) {
        this.employer = employer;
    }

    public EmploymentDto getEmployment() {
        return employment;
    }

    public void setEmployment(EmploymentDto employment) {
        this.employment = employment;
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

    public ApplyChannelDto getApplyChannel() {
        return applyChannel;
    }

    public void setApplyChannel(ApplyChannelDto applyChannel) {
        this.applyChannel = applyChannel;
    }

    public PublicContactDto getPublicContact() {
        return publicContact;
    }

    public void setPublicContact(PublicContactDto publicContact) {
        this.publicContact = publicContact;
    }
}
