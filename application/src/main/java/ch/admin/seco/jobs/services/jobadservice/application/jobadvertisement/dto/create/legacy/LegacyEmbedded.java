package ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.create.legacy;

import java.util.List;

public class LegacyEmbedded {

    private List<LegacyJobAdvertisementDto> jobOffers;

    protected LegacyEmbedded() {
        // For reflection libs
    }

    public LegacyEmbedded(List<LegacyJobAdvertisementDto> jobOffers) {
        this.jobOffers = jobOffers;
    }

    public List<LegacyJobAdvertisementDto> getJobOffers() {
        return jobOffers;
    }

}
