package ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement;

public class JobAdvertisementAlreadyExistsException extends RuntimeException {

    public JobAdvertisementAlreadyExistsException(String message) {
        super(message);
    }
}
