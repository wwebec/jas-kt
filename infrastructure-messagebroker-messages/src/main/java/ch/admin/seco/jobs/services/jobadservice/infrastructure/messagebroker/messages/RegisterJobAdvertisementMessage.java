package ch.admin.seco.jobs.services.jobadservice.infrastructure.messagebroker.messages;

import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisement;

public class RegisterJobAdvertisementMessage {

    private final JobAdvertisement jobAdvertisement;

    public RegisterJobAdvertisementMessage(JobAdvertisement jobAdvertisement) {
        this.jobAdvertisement = jobAdvertisement;
    }

    public JobAdvertisement getJobAdvertisement() {
        return jobAdvertisement;
    }
}
