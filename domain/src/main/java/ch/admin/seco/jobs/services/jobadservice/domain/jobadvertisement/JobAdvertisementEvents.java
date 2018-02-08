package ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement;

import ch.admin.seco.jobs.services.jobadservice.core.domain.events.DomainEventType;

public enum JobAdvertisementEvents {

    JOB_ADVERTISEMENT_CREATED(new DomainEventType("JOB_ADVERTISEMENT_CREATED"));

    private DomainEventType domainEventType;

    JobAdvertisementEvents(DomainEventType domainEventType) {
        this.domainEventType = domainEventType;
    }

    public DomainEventType getDomainEventType() {
        return domainEventType;
    }

}
