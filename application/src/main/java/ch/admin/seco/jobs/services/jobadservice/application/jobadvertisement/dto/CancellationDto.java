package ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto;

import java.time.LocalDate;

import javax.validation.constraints.NotNull;

public class CancellationDto {

    private String jobAdvertisementId;

    private String stellennummerEgov;

    @NotNull
    private String stellennummerAvam;

    private LocalDate date;

    private String code;

    protected CancellationDto() {
        // For reflection libs
    }

    public CancellationDto(String jobAdvertisementId, String stellennummerEgov, String stellennummerAvam, LocalDate date, String code) {
        this.jobAdvertisementId = jobAdvertisementId;
        this.stellennummerEgov = stellennummerEgov;
        this.stellennummerAvam = stellennummerAvam;
        this.date = date;
        this.code = code;
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
}
