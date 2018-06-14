package ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.create.legacy;

import java.util.List;

public class LegacyPageDto {

    private LegacyEmbedded _embedded;
    private LegacyPageLinks _links;
    private LegacyPage page;

    protected LegacyPageDto() {
        // For reflection libs
    }

    public LegacyPageDto(List<LegacyJobAdvertisementDto> jobOffers, String self, String profile, String search, int size, long totalElements, int totalPage, int number) {
        this._embedded = new LegacyEmbedded(jobOffers);
        this._links = new LegacyPageLinks(self, profile, search);
        this.page = new LegacyPage(size, totalElements, totalPage, number);
    }

    public LegacyEmbedded get_embedded() {
        return _embedded;
    }

    public LegacyPageLinks get_links() {
        return _links;
    }

    public LegacyPage getPage() {
        return page;
    }
}
