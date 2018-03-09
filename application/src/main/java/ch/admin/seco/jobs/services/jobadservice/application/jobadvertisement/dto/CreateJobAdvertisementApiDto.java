package ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

public class CreateJobAdvertisementApiDto {

    private boolean reportToRav;

    @NotNull
    private LocalDate publicationStartDate;

    private LocalDate publicationEndDate;

    private String reference;

    private String url;

    private ApplyChannelDto applyChannel;

    @Valid
    @NotNull
    private JobApiDto job;

    @NotNull
    private CompanyDto company;

    private ContactDto contact;

    @NotNull
    private OccupationDto occupation;

    protected CreateJobAdvertisementApiDto() {
        // For reflection libs
    }

    public CreateJobAdvertisementApiDto(boolean reportToRav,
                                        LocalDate publicationStartDate,
                                        LocalDate publicationEndDate,
                                        String reference,
                                        String url,
                                        ApplyChannelDto applyChannel,
                                        JobApiDto job,
                                        CompanyDto company,
                                        ContactDto contact,
                                        OccupationDto occupation) {
        this.reportToRav = reportToRav;
        this.publicationStartDate = publicationStartDate;
        this.publicationEndDate = publicationEndDate;
        this.reference = reference;
        this.url = url;
        this.applyChannel = applyChannel;
        this.job = job;
        this.company = company;
        this.contact = contact;
        this.occupation = occupation;
    }

    public boolean isReportToRav() {
        return reportToRav;
    }

    public void setReportToRav(boolean reportToRav) {
        this.reportToRav = reportToRav;
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

    public ApplyChannelDto getApplyChannel() {
        return applyChannel;
    }

    public void setApplyChannel(ApplyChannelDto applyChannel) {
        this.applyChannel = applyChannel;
    }

    public JobApiDto getJob() {
        return job;
    }

    public void setJob(JobApiDto job) {
        this.job = job;
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

    public OccupationDto getOccupation() {
        return occupation;
    }

    public void setOccupation(OccupationDto occupation) {
        this.occupation = occupation;
    }
}
