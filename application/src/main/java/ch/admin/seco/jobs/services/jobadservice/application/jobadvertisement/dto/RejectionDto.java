package ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto;

import java.time.LocalDate;

import javax.validation.constraints.NotNull;

public class RejectionDto {

    private String jobAdvertisementId;

    @NotNull
    private String stellennummerEgov;

    private String stellennummerAvam;

    @NotNull
    private LocalDate date;

    @NotNull
    private String code;

    private String reason;

    protected RejectionDto() {
        // For reflection libs
    }

    public RejectionDto(String jobAdvertisementId, String stellennummerEgov, String stellennummerAvam, LocalDate date, String code, String reason) {
        this.jobAdvertisementId = jobAdvertisementId;
        this.stellennummerEgov = stellennummerEgov;
        this.stellennummerAvam = stellennummerAvam;
        this.date = date;
        this.code = code;
        this.reason = reason;
    }

    public String getJobAdvertisementId() {
        return jobAdvertisementId;
    }

    public void setJobAdvertisementId(String jobAdvertisementId) {
        this.jobAdvertisementId = jobAdvertisementId;
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
}
