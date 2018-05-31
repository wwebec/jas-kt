package ch.admin.seco.jobs.services.jobadservice.infrastructure.web.controller.resources;

import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.CancellationCode;

public class CancellationResource {

    private CancellationCode code;

    protected CancellationResource() {
        // for reflection libs
    }

    public CancellationResource(CancellationCode code) {
        this.code = code;
    }

    public CancellationCode getCode() {
        return code;
    }

    public void setCode(CancellationCode code) {
        this.code = code;
    }
}
