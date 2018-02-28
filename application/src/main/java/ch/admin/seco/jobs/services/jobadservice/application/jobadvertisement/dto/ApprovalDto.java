package ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

public class ApprovalDto {

    @NotNull
    private String jobAdvertisementId;

    @NotNull
    private String stellennummerAvam;

    @NotNull
    private LocalDate date;

    private boolean reportingObligation;

    private LocalDate reportingObligationEndDate;

    protected ApprovalDto() {
        // For reflection libs
    }

    public ApprovalDto(String jobAdvertisementId, String stellennummerAvam, LocalDate date, boolean reportingObligation, LocalDate reportingObligationEndDate) {
        this.jobAdvertisementId = jobAdvertisementId;
        this.stellennummerAvam = stellennummerAvam;
        this.date = date;
        this.reportingObligation = reportingObligation;
        this.reportingObligationEndDate = reportingObligationEndDate;
    }

    public String getJobAdvertisementId() {
        return jobAdvertisementId;
    }

    public void setJobAdvertisementId(String jobAdvertisementId) {
        this.jobAdvertisementId = jobAdvertisementId;
    }

    public String getStellennummerAvam() {
        return stellennummerAvam;
    }

    public void setStellennummerAvam(String stellennummerAvam) {
        this.stellennummerAvam = stellennummerAvam;
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

    public LocalDate getReportingObligationEndDate() {
        return reportingObligationEndDate;
    }

    public void setReportingObligationEndDate(LocalDate reportingObligationEndDate) {
        this.reportingObligationEndDate = reportingObligationEndDate;
    }
}
