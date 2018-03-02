package ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement;

public class CreateJobAdvertisementEvent {

    private JobAdvertisementEvents jobAdvertisementEvent;
    private JobAdvertisement jobAdvertisement;

    private CreateJobAdvertisementEvent() {
    }

    public CreateJobAdvertisementEvent(JobAdvertisement jobAdvertisement) {
        this.jobAdvertisementEvent = JobAdvertisementEvents.JOB_ADVERTISEMENT_CREATED;
        this.jobAdvertisement = jobAdvertisement;
    }

    public JobAdvertisement geJobAdvertisement() {
        return jobAdvertisement;
    }

    public JobAdvertisementEvents getDomainEventType() {
        return jobAdvertisementEvent;
    }
}
