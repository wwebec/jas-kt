package ch.admin.seco.jobs.services.jobadservice.infrastructure.events;

import ch.admin.seco.jobs.services.jobadservice.core.domain.events.AuditUser;
import ch.admin.seco.jobs.services.jobadservice.core.domain.events.DomainEvent;
import ch.admin.seco.jobs.services.jobadservice.core.domain.events.DomainEventPublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Component;

@Component("domainEventPublisher")
public class SpringDomainEventPublisher implements DomainEventPublisher, ApplicationEventPublisherAware {

    private static final Logger LOGGER = LoggerFactory.getLogger(SpringDomainEventPublisher.class);

    private ApplicationEventPublisher applicationEventPublisher;
/*
    private final UserContext userContext;

    @Autowired
    public SpringDomainEventPublisher(UserContext userContext) {
        this.userContext = userContext;
    }
*/

    @Override
    public void publishEvent(DomainEvent domainEvent) {
        LOGGER.debug("Starting Publishing Domain Event: ID '{}', Type '{}', AggregateId '{}'", domainEvent.getId().getValue(), domainEvent.getDomainEventType().getValue(), domainEvent.getAggregateId().getValue());
        // FIXME get the real user
        //domainEvent.setAuditUser(userContext.getAuditUser());
        domainEvent.setAuditUser(new AuditUser("intern-1", "extern-1", "My", "User", "my.user@example.org"));
        applicationEventPublisher.publishEvent(domainEvent);
    }

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

}
