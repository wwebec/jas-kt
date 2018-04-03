package ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.update;

import javax.validation.constraints.NotBlank;

public class UpdateJobAdvertisementFromX28Dto {

    @NotBlank
    private String stellennummerEgov;

    @NotBlank
    private String fingerprint;

    private String x28OccupationCodes;

    protected UpdateJobAdvertisementFromX28Dto() {
        // For reflection libs
    }

    public UpdateJobAdvertisementFromX28Dto(@NotBlank String stellennummerEgov, @NotBlank String fingerprint, @NotBlank String x28OccupationCodes) {
        this.stellennummerEgov = stellennummerEgov;
        this.fingerprint = fingerprint;
        this.x28OccupationCodes = x28OccupationCodes;
    }

    public String getStellennummerEgov() {
        return stellennummerEgov;
    }

    public String getFingerprint() {
        return fingerprint;
    }

    public String getX28OccupationCodes() {
        return x28OccupationCodes;
    }
}
