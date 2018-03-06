package ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement;

import ch.admin.seco.jobs.services.jobadservice.core.domain.events.DomainEvent;

public class JobAdvertisementEvent extends DomainEvent<JobAdvertisementId> {

    private JobAdvertisementId jobAdvertisementId;

    public JobAdvertisementEvent(JobAdvertisementEvents jobAdvertisementEventType, JobAdvertisement jobAdvertisement) {
        super(jobAdvertisementEventType.getDomainEventType(), JobAdvertisement.class.getSimpleName());
        this.jobAdvertisementId = jobAdvertisement.getId();
        additionalAttributes.put("jobAdvertisementId", jobAdvertisementId.getValue());
    }

    @Override
    public JobAdvertisementId getAggregateId() {
        return this.jobAdvertisementId;
    }
}
