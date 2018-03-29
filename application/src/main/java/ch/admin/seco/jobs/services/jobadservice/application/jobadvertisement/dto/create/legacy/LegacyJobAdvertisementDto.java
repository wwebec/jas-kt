package ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.create.legacy;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Locale;

public class LegacyJobAdvertisementDto {

    @Valid
    private LegacyJobDto job;
    @Valid
    private LegacyCompanyDto company;
    @Valid
    private LegacyContactDto contact;
    @Valid
    private LegacyApplicationDto application;
    @Valid
    private LegacyPublicationDto publication;
    @NotNull
    private Locale locale;

    protected LegacyJobAdvertisementDto() {
        // For reflection libs
    }

    public LegacyJobAdvertisementDto(
            LegacyJobDto job,
            LegacyCompanyDto company,
            LegacyContactDto contact,
            LegacyApplicationDto application,
            LegacyPublicationDto publication,
            Locale locale
    ) {
        this.job = job;
        this.company = company;
        this.contact = contact;
        this.application = application;
        this.publication = publication;
        this.locale = locale;
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

    public LegacyApplicationDto getApplication() {
        return application;
    }

    public void setApplication(LegacyApplicationDto application) {
        this.application = application;
    }

    public LegacyPublicationDto getPublication() {
        return publication;
    }

    public void setPublication(LegacyPublicationDto publication) {
        this.publication = publication;
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }
}
