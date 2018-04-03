package ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.create.legacy;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

public class LegacyJobAdvertisementDto {

    @NotNull
    private LocalDate publicationStartDate;
    private LocalDate publicationEndDate;
    private String reference;
    private String url;
    private String applicationUrl;
    @Valid
    private LegacyJobDto job;
    @Valid
    private LegacyCompanyDto company;
    @Valid
    private LegacyContactDto contact;

    protected LegacyJobAdvertisementDto() {
        // For reflection libs
    }

    public LegacyJobAdvertisementDto(LocalDate publicationStartDate, LocalDate publicationEndDate, String reference, String url, String applicationUrl, LegacyJobDto job, LegacyCompanyDto company, LegacyContactDto contact) {
        this.publicationStartDate = publicationStartDate;
        this.publicationEndDate = publicationEndDate;
        this.reference = reference;
        this.url = url;
        this.applicationUrl = applicationUrl;
        this.job = job;
        this.company = company;
        this.contact = contact;
    }

    public LocalDate getPublicationStartDate() {
        return publicationStartDate;
    }

    public void setPublicationStartDate(LocalDate publicationStartDate) {
        this.publicationStartDate = publicationStartDate;
    }

    public LocalDate getPublicationEndDate() {
        return publicationEndDate;
    }

    public void setPublicationEndDate(LocalDate publicationEndDate) {
        this.publicationEndDate = publicationEndDate;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getApplicationUrl() {
        return applicationUrl;
    }

    public void setApplicationUrl(String applicationUrl) {
        this.applicationUrl = applicationUrl;
    }

    public LegacyJobDto getJob() {
        return job;
    }

    public void setJob(LegacyJobDto job) {
        this.job = job;
    }

    public LegacyCompanyDto getCompany() {
        return company;
    }

    public void setCompany(LegacyCompanyDto company) {
        this.company = company;
    }

    public LegacyContactDto getContact() {
        return contact;
    }

    public void setContact(LegacyContactDto contact) {
        this.contact = contact;
    }

}
