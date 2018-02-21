package ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement;

import ch.admin.seco.jobs.services.jobadservice.core.domain.events.DomainEventType;

public enum JobAdvertisementEvents {

    JOB_ADVERTISEMENT_CREATED(new DomainEventType("JOB_ADVERTISEMENT_CREATED")),
    JOB_ADVERTISEMENT_UPDATED(new DomainEventType("JOB_ADVERTISEMENT_UPDATED")),
    JOB_ADVERTISEMENT_INSPECTING(new DomainEventType("JOB_ADVERTISEMENT_INSPECTING")),
    JOB_ADVERTISEMENT_APPROVED(new DomainEventType("JOB_ADVERTISEMENT_APPROVED")),
    JOB_ADVERTISEMENT_REJECTED(new DomainEventType("JOB_ADVERTISEMENT_REJECTED")),
    JOB_ADVERTISEMENT_CANCELLED(new DomainEventType("JOB_ADVERTISEMENT_CANCELLED")),
    JOB_ADVERTISEMENT_PUBLISHED_RESTRICTED(new DomainEventType("JOB_ADVERTISEMENT_PUBLISHED_RESTRICTED")),
    JOB_ADVERTISEMENT_PUBLISHED_PUBLIC(new DomainEventType("JOB_ADVERTISEMENT_PUBLISHED_PUBLIC")),
    JOB_ADVERTISEMENT_ARCHIVED(new DomainEventType("JOB_ADVERTISEMENT_ARCHIVED"));

    private DomainEventType domainEventType;

    JobAdvertisementEvents(DomainEventType domainEventType) {
        this.domainEventType = domainEventType;
    }

    public DomainEventType getDomainEventType() {
        return domainEventType;
    }

}
