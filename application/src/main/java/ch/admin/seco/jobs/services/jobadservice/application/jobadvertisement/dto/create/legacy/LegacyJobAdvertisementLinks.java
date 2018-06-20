package ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.create.legacy;

public class LegacyJobAdvertisementLinks {
    private LegacyHref self;
    private LegacyHref jobOffer;

    protected LegacyJobAdvertisementLinks() {
        // For reflection libs
    }

    public LegacyJobAdvertisementLinks(String self, String jobOffer) {
        this.self = new LegacyHref(self);
        this.jobOffer = new LegacyHref(jobOffer);
    }

    public LegacyHref getSelf() {
        return self;
    }

    public LegacyHref getJobOffer() {
        return jobOffer;
    }
}
