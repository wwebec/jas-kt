package ch.admin.seco.jobs.services.jobadservice.core.domain.events;

import org.springframework.data.domain.Page;

public interface EventStore {

    Page<EventData> findByAggregateId(String aggregateId, String aggregateType, int page, int pageSize);

    EventData findLatestByType(String aggregateId, String aggregateType, DomainEventType domainEventType);

}
