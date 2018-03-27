package ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto;

import javax.validation.constraints.NotBlank;

public class UpdateJobAdvertisementFromX28Dto {

    @NotBlank
    private String stellennummerEgov;

    @NotBlank
    private String fingerprint;

    private String x28OccupationCode;

    protected UpdateJobAdvertisementFromX28Dto() {
    }

    public UpdateJobAdvertisementFromX28Dto(@NotBlank String stellennummerEgov, @NotBlank String fingerprint, @NotBlank String x28OccupationCode) {
        this.stellennummerEgov = stellennummerEgov;
        this.fingerprint = fingerprint;
        this.x28OccupationCode = x28OccupationCode;
    }

    public String getStellennummerEgov() {
        return stellennummerEgov;
    }

    public String getFingerprint() {
        return fingerprint;
    }

    public String getX28OccupationCode() {
        return x28OccupationCode;
    }
}
