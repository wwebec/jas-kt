package ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.update;

import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.CancellationCode;

import java.time.LocalDate;

import javax.validation.constraints.NotNull;

public class CancellationDto {

    private String stellennummerEgov;

    @NotNull
    private String stellennummerAvam;

    private LocalDate date;

    private CancellationCode code;

    protected CancellationDto() {
        // For reflection libs
    }

    public CancellationDto(String stellennummerEgov, String stellennummerAvam, LocalDate date, CancellationCode code) {
        this.stellennummerEgov = stellennummerEgov;
        this.stellennummerAvam = stellennummerAvam;
        this.date = date;
        this.code = code;
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

    public CancellationCode getCode() {
        return code;
    }

    public void setCode(CancellationCode code) {
        this.code = code;
    }
}
