package ch.admin.seco.jobs.services.jobadservice.infrastructure.messagebroker.messages;

import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisement;

public class DeregisterJobAdvertisementMessage {

    private final JobAdvertisement jobAdvertisement;

    public DeregisterJobAdvertisementMessage(JobAdvertisement jobAdvertisement) {
        this.jobAdvertisement = jobAdvertisement;
    }

    public JobAdvertisement getJobAdvertisement() {
        return jobAdvertisement;
    }
}
