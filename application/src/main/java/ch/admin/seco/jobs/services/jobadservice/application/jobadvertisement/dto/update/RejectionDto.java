package ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.update;

import java.time.LocalDate;

import javax.validation.constraints.NotNull;

public class RejectionDto {

    @NotNull
    private String stellennummerEgov;

    private String stellennummerAvam;

    @NotNull
    private LocalDate date;

    @NotNull
    private String code;

    private String reason;

    private String jobCenterCode;

    protected RejectionDto() {
        // For reflection libs
    }

    public RejectionDto(String stellennummerEgov, String stellennummerAvam, LocalDate date, String code, String reason, String jobCenterCode) {
        this.stellennummerEgov = stellennummerEgov;
        this.stellennummerAvam = stellennummerAvam;
        this.date = date;
        this.code = code;
        this.reason = reason;
        this.jobCenterCode = jobCenterCode;
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getJobCenterCode() {
        return jobCenterCode;
    }

    public void setJobCenterCode(String jobCenterCode) {
        this.jobCenterCode = jobCenterCode;
    }
}
