package ch.admin.seco.jobs.services.jobadservice.core.domain.events;

public interface DomainEventPublisher {

    static void publish(DomainEvent domainEvent) {
        DomainEventPublisherRegistry.getInstance().publishEvent(domainEvent);
    }

    void publishEvent(DomainEvent domainEvent);

    static void set(DomainEventPublisher domainEventPublisher) {
        DomainEventPublisherRegistry.setInstance(domainEventPublisher);
    }

    static DomainEventPublisher get() {
        return DomainEventPublisherRegistry.getInstance();
    }

}
