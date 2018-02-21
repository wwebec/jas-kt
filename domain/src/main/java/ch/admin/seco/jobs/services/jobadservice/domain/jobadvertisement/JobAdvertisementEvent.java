package ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement;

import ch.admin.seco.jobs.services.jobadservice.core.domain.events.DomainEvent;
import ch.admin.seco.jobs.services.jobadservice.core.domain.events.DomainEventType;

public class JobAdvertisementEvent extends DomainEvent {

    private JobAdvertisementId jobAdvertisementId;

    public JobAdvertisementEvent(JobAdvertisementEvents jobAdvertisementEventType, JobAdvertisement jobAdvertisement) {
        super(jobAdvertisementEventType.getDomainEventType(), JobAdvertisement.class.getSimpleName());
        this.jobAdvertisementId = jobAdvertisement.getId();
        additionalAttributes.put("jobAdvertisementId", jobAdvertisementId.getValue());
    }

    @Override
    public String getAggregateId() {
        return this.jobAdvertisementId.getValue();
    }

    public JobAdvertisementId getJobAdvertisementId() {
        return jobAdvertisementId;
    }
}
