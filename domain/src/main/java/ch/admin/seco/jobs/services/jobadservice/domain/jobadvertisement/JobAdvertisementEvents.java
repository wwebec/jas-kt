package ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement;

import ch.admin.seco.jobs.services.jobadservice.core.domain.events.DomainEventType;

public enum JobAdvertisementEvents {

    JOB_ADVERTISEMENT_CREATED(new DomainEventType("JOB_ADVERTISEMENT_CREATED")),
    JOB_ADVERTISEMENT_CANCELLED(new DomainEventType("JOB_ADVERTISEMENT_CANCELLED"));

    private DomainEventType domainEventType;

    JobAdvertisementEvents(DomainEventType domainEventType) {
        this.domainEventType = domainEventType;
    }

    public DomainEventType getDomainEventType() {
        return domainEventType;
    }

}
