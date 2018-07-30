package ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement;

import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisementId;

public class JobAdvertisementAlreadyExistsException extends RuntimeException {
    private JobAdvertisementId jobAdvertisementId;

    public JobAdvertisementAlreadyExistsException(JobAdvertisementId id, String message) {
        super(message);
        this.jobAdvertisementId = id;
    }

    public JobAdvertisementId getJobAdvertisementId() {
        return jobAdvertisementId;
    }
}
