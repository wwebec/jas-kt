package ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto;

import java.time.LocalDate;

public class CancellationDto {

    private String jobAdvertisementId;
    private LocalDate date;
    private String code;

    protected CancellationDto() {
        // For reflection libs
    }

    public CancellationDto(String jobAdvertisementId, LocalDate date, String code) {
        this.jobAdvertisementId = jobAdvertisementId;
        this.date = date;
        this.code = code;
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
}
