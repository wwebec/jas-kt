package ch.admin.seco.jobs.services.jobadservice.infrastructure.web.controller.resources;

public class CancellationResource {

    private String reasonCode;

    public CancellationResource(String reasonCode) {
        this.reasonCode = reasonCode;
    }

    public String getReasonCode() {
        return reasonCode;
    }

    public void setReasonCode(String reasonCode) {
        this.reasonCode = reasonCode;
    }
}
