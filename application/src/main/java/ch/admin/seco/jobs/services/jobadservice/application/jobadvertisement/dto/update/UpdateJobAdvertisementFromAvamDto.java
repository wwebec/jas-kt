package ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.update;

import javax.validation.constraints.NotBlank;

public class UpdateJobAdvertisementFromAvamDto {

    @NotBlank
    private String stellennummerAvam;

    protected UpdateJobAdvertisementFromAvamDto() {
        // For reflection libs
    }

    public UpdateJobAdvertisementFromAvamDto(@NotBlank String stellennummerAvam) {
        this.stellennummerAvam = stellennummerAvam;
    }

    public String getStellennummerAvam() {
        return stellennummerAvam;
    }

    public void setStellennummerAvam(String stellennummerAvam) {
        this.stellennummerAvam = stellennummerAvam;
    }
}
