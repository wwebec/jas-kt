package ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.create.legacy;

public class LegacyPageLinks {
    private LegacyHref self;
    private LegacyHref profile;
    private LegacyHref search;

    public LegacyPageLinks(String self, String profile, String search) {
        this.self = new LegacyHref(self);
        this.profile = new LegacyHref(profile);
        this.search = new LegacyHref(search);
    }

    public LegacyHref getSelf() {
        return self;
    }

    public LegacyHref getProfile() {
        return profile;
    }

    public LegacyHref getSearch() {
        return search;
    }
}
