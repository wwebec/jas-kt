package ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement;

public class IllegalJobAdvertisementStatusTransitionException extends RuntimeException {

    private final JobAdvertisementStatus source;

    private final JobAdvertisementStatus destination;

    public IllegalJobAdvertisementStatusTransitionException(JobAdvertisementStatus source,
                                                                  JobAdvertisementStatus destination) {
        super("Illegal JobAdvertisement status transition attempted ( " + source + " -> " + destination + " )");
        this.source = source;
        this.destination = destination;
    }

    public JobAdvertisementStatus getSource() {
        return source;
    }

    public JobAdvertisementStatus getDestination() {
        return destination;
    }

}
