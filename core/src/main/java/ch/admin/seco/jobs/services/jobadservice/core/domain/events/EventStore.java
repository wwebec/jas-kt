package ch.admin.seco.jobs.services.jobadservice.core.domain.events;

import org.springframework.data.domain.Page;

public interface EventStore {

    Page<EventData> findByAggregateId(String aggregateId, AggregateType aggregateType, int page, int pageSize);

    EventData findLatestByType(String aggregateId, AggregateType aggregateType, DomainEventType domainEventType);

}
