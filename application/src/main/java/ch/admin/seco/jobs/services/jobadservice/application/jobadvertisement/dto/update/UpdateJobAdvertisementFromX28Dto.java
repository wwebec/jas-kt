package ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.update;

import javax.validation.constraints.NotBlank;

public class UpdateJobAdvertisementFromX28Dto {

    @NotBlank
    private String jobAdvertisementId;

    @NotBlank
    private String fingerprint;

    private String x28OccupationCodes;

    protected UpdateJobAdvertisementFromX28Dto() {
        // For reflection libs
    }

    public UpdateJobAdvertisementFromX28Dto(String jobAdvertisementId, String fingerprint, String x28OccupationCodes) {
        this.jobAdvertisementId = jobAdvertisementId;
        this.fingerprint = fingerprint;
        this.x28OccupationCodes = x28OccupationCodes;
    }

    public String getJobAdvertisementId() {
        return jobAdvertisementId;
    }

    public String getFingerprint() {
        return fingerprint;
    }

    public String getX28OccupationCodes() {
        return x28OccupationCodes;
    }
}
