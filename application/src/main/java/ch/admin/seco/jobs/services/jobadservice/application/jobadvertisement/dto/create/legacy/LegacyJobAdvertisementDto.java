package ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.create.legacy;

import java.time.LocalDate;

public class LegacyJobAdvertisementDto {

    private String id;
    private LocalDate publicationStartDate;
    private LocalDate publicationEndDate;
    private String reference;
    private String url;
    private String applicationUrl;
    private LegacyJobDto job;
    private LegacyCompanyDto company;
    private LegacyContactDto contact;
    private LegacyJobAdvertisementLinks _links;

    protected LegacyJobAdvertisementDto() {
        // For reflection libs
    }

    public LegacyJobAdvertisementDto(String id, LocalDate publicationStartDate, LocalDate publicationEndDate, String reference, String url, String applicationUrl, LegacyJobDto job, LegacyCompanyDto company, LegacyContactDto contact, String self, String jobOffer) {
        this.id = id;
        this.publicationStartDate = publicationStartDate;
        this.publicationEndDate = publicationEndDate;
        this.reference = reference;
        this.url = url;
        this.applicationUrl = applicationUrl;
        this.job = job;
        this.company = company;
        this.contact = contact;
        this._links = new LegacyJobAdvertisementLinks(self, jobOffer);
    }

    public String getId() {
        return id;
    }

    public LocalDate getPublicationStartDate() {
        return publicationStartDate;
    }

    public LocalDate getPublicationEndDate() {
        return publicationEndDate;
    }

    public String getReference() {
        return reference;
    }

    public String getUrl() {
        return url;
    }

    public String getApplicationUrl() {
        return applicationUrl;
    }

    public LegacyJobDto getJob() {
        return job;
    }

    public LegacyCompanyDto getCompany() {
        return company;
    }

    public LegacyContactDto getContact() {
        return contact;
    }

    public LegacyJobAdvertisementLinks get_links() {
        return _links;
    }
}
