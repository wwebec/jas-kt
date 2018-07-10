package ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.update;

import java.time.LocalDate;

import javax.validation.constraints.NotNull;

public class ApprovalDto {

    @NotNull
    private String stellennummerEgov;

    @NotNull
    private String stellennummerAvam;

    @NotNull
    private LocalDate date;

    private boolean reportingObligation;

    private LocalDate reportingObligationEndDate;

    private UpdateJobAdvertisementFromAvamDto updateJobAdvertisementDto;


    protected ApprovalDto() {
        // For reflection libs
    }

    public ApprovalDto(String stellennummerEgov, String stellennummerAvam, LocalDate date, boolean reportingObligation, LocalDate reportingObligationEndDate, UpdateJobAdvertisementFromAvamDto updateJobAdvertisementDto) {
        this.stellennummerEgov = stellennummerEgov;
        this.stellennummerAvam = stellennummerAvam;
        this.date = date;
        this.reportingObligation = reportingObligation;
        this.reportingObligationEndDate = reportingObligationEndDate;
        this.updateJobAdvertisementDto = updateJobAdvertisementDto;
    }

    public String getStellennummerEgov() {
        return stellennummerEgov;
    }

    public void setStellennummerEgov(String stellennummerEgov) {
        this.stellennummerEgov = stellennummerEgov;
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

    public UpdateJobAdvertisementFromAvamDto getUpdateJobAdvertisement() {
        return updateJobAdvertisementDto;
    }

    public void setUpdateJobAdvertisement(UpdateJobAdvertisementFromAvamDto updateJobAdvertisementDto) {
        this.updateJobAdvertisementDto = updateJobAdvertisementDto;
    }
}
