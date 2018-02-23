package ch.admin.seco.jobs.services.jobadservice.application;

import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisementId;

public class RavRegistrationException extends RuntimeException {

    public RavRegistrationException(JobAdvertisementId jobAdvertisementId, String action) {
        super("Couldn't register JobAdvertisement with id: " + jobAdvertisementId.getValue() + " -> " + action);
    }
}
