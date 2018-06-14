package ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.create.legacy;

public class LegacyCancellationDto {

    private String reasonCode;

    protected LegacyCancellationDto() {
        // For reflection libs
    }

    public LegacyCancellationDto(String reasonCode) {
        this.reasonCode = reasonCode;
    }

    public String getReasonCode() {
        return reasonCode;
    }

    public void setReasonCode(String reasonCode) {
        this.reasonCode = reasonCode;
    }
}
