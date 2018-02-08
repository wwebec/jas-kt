package ch.admin.seco.jobs.services.jobadservice.core.domain.events;

import ch.admin.seco.jobs.services.jobadservice.core.conditions.Condition;

class DomainEventPublisherRegistry {

    private static DomainEventPublisher instance;

    private DomainEventPublisherRegistry() {
    }

    static DomainEventPublisher getInstance() {
        return Condition.notNull(DomainEventPublisherRegistry.instance, "DomainEventPublisher has not been initialized yet");
    }

    static void setInstance(DomainEventPublisher instance) {
        DomainEventPublisherRegistry.instance = instance;
    }

}
