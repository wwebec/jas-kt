package ch.admin.seco.jobs.services.jobadservice.infrastructure.web.controller.webform;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.ApplyChannelDto;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.CompanyDto;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.ContactDto;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.EmployerDto;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.EmploymentDto;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.JobDescriptionDto;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.LanguageSkillDto;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.OccupationDto;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.PublicContactDto;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.PublicationDto;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.create.CreateLocationDto;

public class WebformCreateJobAdvertisementDto {

    private boolean reportToAvam;

    private String externalUrl;

    private String externalReference;

    @Valid
    @NotNull
    private ContactDto contact;

    @Valid
    @NotNull
    private PublicationDto publication;

    private String numberOfJobs;

    @Valid
    @NotNull
    @NotEmpty
    private List<JobDescriptionDto> jobDescriptions;

    @Valid
    @NotNull
    private CompanyDto company;

    @Valid
    private EmployerDto employer;

    @Valid
    @NotNull
    private EmploymentDto employment;

    @Valid
    @NotNull
    private CreateLocationDto location;

    @Valid
    @NotNull
    private OccupationDto occupation;

    @Valid
    private List<LanguageSkillDto> languageSkills;

    @Valid
    private ApplyChannelDto applyChannel;

    @Valid
    @NotNull
    private WebFormPublicContactDto publicContact;

    public boolean isReportToAvam() {
        return reportToAvam;
    }

    public WebformCreateJobAdvertisementDto setReportToAvam(boolean reportToAvam) {
        this.reportToAvam = reportToAvam;
        return this;
    }

    public String getExternalUrl() {
        return externalUrl;
    }

    public WebformCreateJobAdvertisementDto setExternalUrl(String externalUrl) {
        this.externalUrl = externalUrl;
        return this;
    }

    public String getExternalReference() {
        return externalReference;
    }

    public WebformCreateJobAdvertisementDto setExternalReference(String externalReference) {
        this.externalReference = externalReference;
        return this;
    }

    public ContactDto getContact() {
        return contact;
    }

    public WebformCreateJobAdvertisementDto setContact(ContactDto contact) {
        this.contact = contact;
        return this;
    }

    public PublicationDto getPublication() {
        return publication;
    }

    public WebformCreateJobAdvertisementDto setPublication(PublicationDto publication) {
        this.publication = publication;
        return this;
    }

    public String getNumberOfJobs() {
        return numberOfJobs;
    }

    public WebformCreateJobAdvertisementDto setNumberOfJobs(String numberOfJobs) {
        this.numberOfJobs = numberOfJobs;
        return this;
    }

    public List<JobDescriptionDto> getJobDescriptions() {
        return jobDescriptions;
    }

    public WebformCreateJobAdvertisementDto setJobDescriptions(List<JobDescriptionDto> jobDescriptions) {
        this.jobDescriptions = jobDescriptions;
        return this;
    }

    public CompanyDto getCompany() {
        return company;
    }

    public WebformCreateJobAdvertisementDto setCompany(CompanyDto company) {
        this.company = company;
        return this;
    }

    public EmployerDto getEmployer() {
        return employer;
    }

    public WebformCreateJobAdvertisementDto setEmployer(EmployerDto employer) {
        this.employer = employer;
        return this;
    }

    public EmploymentDto getEmployment() {
        return employment;
    }

    public WebformCreateJobAdvertisementDto setEmployment(EmploymentDto employment) {
        this.employment = employment;
        return this;
    }

    public CreateLocationDto getLocation() {
        return location;
    }

    public WebformCreateJobAdvertisementDto setLocation(CreateLocationDto location) {
        this.location = location;
        return this;
    }

    public OccupationDto getOccupation() {
        return occupation;
    }

    public WebformCreateJobAdvertisementDto setOccupation(OccupationDto occupation) {
        this.occupation = occupation;
        return this;
    }

    public List<LanguageSkillDto> getLanguageSkills() {
        return languageSkills;
    }

    public WebformCreateJobAdvertisementDto setLanguageSkills(List<LanguageSkillDto> languageSkills) {
        this.languageSkills = languageSkills;
        return this;
    }

    public ApplyChannelDto getApplyChannel() {
        return applyChannel;
    }

    public WebformCreateJobAdvertisementDto setApplyChannel(ApplyChannelDto applyChannel) {
        this.applyChannel = applyChannel;
        return this;
    }

    public WebFormPublicContactDto getPublicContact() {
        return publicContact;
    }

    public WebformCreateJobAdvertisementDto setPublicContact(WebFormPublicContactDto publicContact) {
        this.publicContact = publicContact;
        return this;
    }
}
