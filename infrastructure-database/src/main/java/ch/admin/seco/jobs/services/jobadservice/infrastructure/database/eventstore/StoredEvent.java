package ch.admin.seco.jobs.services.jobadservice.infrastructure.database.eventstore;

import ch.admin.seco.jobs.services.jobadservice.core.conditions.Condition;
import ch.admin.seco.jobs.services.jobadservice.core.domain.events.DomainEvent;
import ch.admin.seco.jobs.services.jobadservice.core.domain.events.DomainEventType;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
class StoredEvent {

    @Id
    private String id;

    @NotNull
    private LocalDateTime registrationTime;

    @NotNull
    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "domain_event_type"))
    @Valid
    private DomainEventType domainEventType;

    @NotNull
    private String aggregateType;

    @NotEmpty
    private String aggregateId;

    private String userId;

    private String userExternalId;

    private String userDisplayName;

    private String userEmail;

    @Lob
    private String payload;


    protected StoredEvent() {
        // For reflection libs
    }

    StoredEvent(DomainEvent domainEvent) {
        this(domainEvent, null);
    }

    StoredEvent(DomainEvent domainEvent, String payload) {
        Condition.notNull(domainEvent);
        this.id = Condition.notBlank(domainEvent.getId().getValue());
        this.registrationTime = Condition.notNull(domainEvent.getRegistrationTime());
        this.domainEventType = Condition.notNull(domainEvent.getDomainEventType());
        this.aggregateType = Condition.notBlank(domainEvent.getAggregateType());
        this.aggregateId = Condition.notBlank(domainEvent.getAggregateId().getValue());
        this.userId = domainEvent.getUserId();
        this.userExternalId = domainEvent.getUserExternalId();
        this.userDisplayName = domainEvent.getUserDisplayName();
        this.userEmail = domainEvent.getUserEmail();
        this.payload = payload;
    }

    public String getId() {
        return id;
    }

    public LocalDateTime getRegistrationTime() {
        return registrationTime;
    }

    public DomainEventType getDomainEventType() {
        return domainEventType;
    }

    public String getAggregateType() {
        return aggregateType;
    }

    public String getAggregateId() {
        return aggregateId;
    }

    public String getUserId() {
        return userId;
    }

    public String getUserExternalId() {
        return userExternalId;
    }

    public String getUserDisplayName() {
        return userDisplayName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public String getPayload() {
        return payload;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof StoredEvent)) {
            return false;
        }
        StoredEvent that = (StoredEvent) o;
        return Objects.equals(id, that.id);
    }
}
