package ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto;

import java.time.LocalDate;

public class ApprovalDto {

    private String jobAdvertisementId;
    private LocalDate date;
    private boolean reportingObligation;

    protected ApprovalDto() {
        // For reflection libs
    }

    public ApprovalDto(String jobAdvertisementId, LocalDate date, boolean reportingObligation) {
        this.jobAdvertisementId = jobAdvertisementId;
        this.date = date;
        this.reportingObligation = reportingObligation;
    }

    public String getJobAdvertisementId() {
        return jobAdvertisementId;
    }

    public void setJobAdvertisementId(String jobAdvertisementId) {
        this.jobAdvertisementId = jobAdvertisementId;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public boolean isReportingObligation() {
        return reportingObligation;
    }

    public void setReportingObligation(boolean reportingObligation) {
        this.reportingObligation = reportingObligation;
    }
}
