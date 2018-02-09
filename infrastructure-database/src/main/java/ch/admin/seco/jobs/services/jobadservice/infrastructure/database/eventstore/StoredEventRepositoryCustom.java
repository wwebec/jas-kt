package ch.admin.seco.jobs.services.jobadservice.infrastructure.database.eventstore;

import ch.admin.seco.jobs.services.jobadservice.core.domain.events.DomainEventType;

interface StoredEventRepositoryCustom {

    StoredEvent findLatest(String aggregateId, String aggregateType, DomainEventType domainEventType);

}
