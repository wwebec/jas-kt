package ch.admin.seco.jobs.services.jobadservice.infrastructure.database.eventstore;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import ch.admin.seco.jobs.services.jobadservice.core.domain.events.DomainEventType;
import ch.admin.seco.jobs.services.jobadservice.core.domain.events.EventData;
import ch.admin.seco.jobs.services.jobadservice.core.domain.events.EventStore;

@Transactional(readOnly = true)
class JpaBasedEventStore implements EventStore {

    private final StoredEventRepository storedEventRepository;

    JpaBasedEventStore(StoredEventRepository storedEventRepository) {
        this.storedEventRepository = storedEventRepository;
    }

    private static List<EventData> toEventDtos(List<StoredEvent> storedEvents) {
        return storedEvents.stream()
            .map(JpaBasedEventStore::toEventDto)
            .collect(Collectors.toList());
    }

    private static EventData toEventDto(StoredEvent storedEvent) {
        return new EventData.Builder()
            .setAggregateId(storedEvent.getAggregateId())
            .setAggregateType(storedEvent.getAggregateType())
            .setId(storedEvent.getId())
            .setDomainEventType(storedEvent.getDomainEventType().getValue())
            .setUserEmail(storedEvent.getUserEmail())
            .setUserId(storedEvent.getUserId())
            .setDisplayName(storedEvent.getUserDisplayName())
            .setRegistrationTime(storedEvent.getRegistrationTime())
            .setPayload(storedEvent.getPayload()).build();
    }

    @Override
    public Page<EventData> findByAggregateId(String aggregateId, String aggregateType, int page, int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize);
        final Page<StoredEvent> storedEvents = storedEventRepository.findByAggregateId(aggregateId, aggregateType, pageable);
        return new PageImpl<>(toEventDtos(storedEvents.getContent()), pageable, storedEvents.getTotalElements());
    }

    @Override
    public EventData findLatestByType(String aggregateId, String aggregateType, DomainEventType domainEventType) {
        StoredEvent storedEvent = storedEventRepository.findLatest(aggregateId, aggregateType, domainEventType);
        if (storedEvent == null) {
            return null;
        }
        return toEventDto(storedEvent);
    }
}
