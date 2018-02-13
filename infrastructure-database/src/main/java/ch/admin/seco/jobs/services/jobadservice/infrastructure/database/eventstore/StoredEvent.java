package ch.admin.seco.jobs.services.jobadservice.infrastructure.database.eventstore;

import java.time.LocalDateTime;
import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.google.common.base.Preconditions;

import ch.admin.seco.jobs.services.jobadservice.core.domain.events.DomainEvent;
import ch.admin.seco.jobs.services.jobadservice.core.domain.events.DomainEventType;

@Entity
class StoredEvent {

    @Id
    private String id;

    @NotNull
    private String aggregateType;

    @NotEmpty
    private String aggregateId;

    @NotEmpty
    private String userId;

    @NotEmpty
    private String userEmail;

    @NotEmpty
    private String userDisplayName;

    @NotNull
    private DomainEventType domainEventType;

    @NotNull
    private LocalDateTime registrationTime;

    @Lob
    private String payload;

    StoredEvent(DomainEvent domainEvent, String payload) {
        Preconditions.checkNotNull(domainEvent);
        this.id = domainEvent.getId().getValue();
        this.aggregateId = domainEvent.getAggregateId();
        this.userId = Preconditions.checkNotNull(domainEvent.getUserExternalId());
        this.userDisplayName = Preconditions.checkNotNull(domainEvent.getUserDisplayName());
        this.userEmail = Preconditions.checkNotNull(domainEvent.getUserEmail());
        this.domainEventType = domainEvent.getDomainEventType();
        this.registrationTime = domainEvent.getRegistrationTime();
        this.aggregateType = domainEvent.getAggregateType();
        this.payload = payload;
    }

    StoredEvent(DomainEvent domainEvent) {
        this(domainEvent, null);
    }

    protected StoredEvent() {
        // FOR JPA
    }

    public String getId() {
        return id;
    }

    public String getAggregateId() {
        return aggregateId;
    }

    public String getUserId() {
        return userId;
    }

    public String getUserDisplayName() {
        return userDisplayName;
    }

    public DomainEventType getDomainEventType() {
        return domainEventType;
    }

    public String getPayload() {
        return payload;
    }

    public LocalDateTime getRegistrationTime() {
        return registrationTime;
    }

    public String getAggregateType() {
        return aggregateType;
    }

    public String getUserEmail() {
        return userEmail;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (!(o instanceof StoredEvent)) { return false; }
        StoredEvent that = (StoredEvent) o;
        return Objects.equals(id, that.id);
    }
}
