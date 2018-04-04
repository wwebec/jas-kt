package ch.admin.seco.jobs.services.jobadservice.infrastructure.messagebroker.avam;

import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisementId;

public class AvamException extends RuntimeException {

    public AvamException(JobAdvertisementId jobAdvertisementId, String action) {
        super("Couldn't register JobAdvertisement with id: " + jobAdvertisementId.getValue() + " -> " + action);
    }
}
