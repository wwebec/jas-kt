package ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.create.legacy;

public class LegacyHref {
    private String href;

    protected LegacyHref() {
        // For reflection libs
    }

    public LegacyHref(String href) {
        this.href = href;
    }

    public String getHref() {
        return href;
    }
}
