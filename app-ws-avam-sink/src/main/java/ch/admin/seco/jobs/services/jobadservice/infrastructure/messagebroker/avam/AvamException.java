package ch.admin.seco.jobs.services.jobadservice.infrastructure.messagebroker.avam;

import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisementId;

public class AvamException extends RuntimeException {

    private final JobAdvertisementId jobAdvertisementId;
    private final String action;
    private final String avamResponse;

    public AvamException(JobAdvertisementId jobAdvertisementId, String action, String avamResponse) {
        super("Couldn't send JobAdvertisement with id: " + jobAdvertisementId.getValue() +
                " action: " + action + "\n" + "Details: " + avamResponse);
        this.action = action;
        this.avamResponse = avamResponse;
        this.jobAdvertisementId = jobAdvertisementId;
    }
}
