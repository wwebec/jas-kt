package ch.admin.seco.jobs.services.jobadservice.infrastructure.database.eventstore;


import ch.admin.seco.jobs.services.jobadservice.core.domain.events.DomainEventType;
import org.springframework.data.jpa.repository.support.QueryDslRepositorySupport;

class StoredEventRepositoryImpl extends QueryDslRepositorySupport implements StoredEventRepositoryCustom {

    public StoredEventRepositoryImpl() {
        super(StoredEvent.class);
    }

    @Override
    public StoredEvent findLatest(String aggregateId, String aggregateType, DomainEventType domainEventType) {
        QStoredEvent storedEvent = QStoredEvent.storedEvent;
        return from(storedEvent)
                .select(storedEvent)
                .where(storedEvent.aggregateId.eq(aggregateId)
                        .and(storedEvent.aggregateType.eq(aggregateType))
                        .and(storedEvent.domainEventType.in(domainEventType)))
                .orderBy(storedEvent.registrationTime.desc())
                .fetchFirst();
    }

}
