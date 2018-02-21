package ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto;

import java.time.LocalDate;

public class RejectionDto {

    private String jobAdvertisementId;
    private LocalDate date;
    private String code;
    private String reason;

    protected RejectionDto() {
        // For reflection libs
    }

    public RejectionDto(String jobAdvertisementId, LocalDate date, String code, String reason) {
        this.jobAdvertisementId = jobAdvertisementId;
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
