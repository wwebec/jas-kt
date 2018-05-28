package ch.admin.seco.jobs.services.jobadservice.core.domain.events;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import ch.admin.seco.jobs.services.jobadservice.core.conditions.Condition;
import ch.admin.seco.jobs.services.jobadservice.core.domain.AggregateId;
import ch.admin.seco.jobs.services.jobadservice.core.time.TimeMachine;


public abstract class DomainEvent<T> {

    private final DomainEventId id;

    private final LocalDateTime registrationTime;

    private final DomainEventType domainEventType;

    private final String aggregateType;

    private String userId;

    private String userExternalId;

    private String userDisplayName;

    private String userEmail;

    protected transient Map<String, Object> additionalAttributes = new HashMap<>();

    public DomainEvent(DomainEventType domainEventType, String aggregateType) {
        this.id = new DomainEventId();
        this.registrationTime = TimeMachine.now();
        this.domainEventType = Condition.notNull(domainEventType);
        this.aggregateType = aggregateType;
    }

    public DomainEventId getId() {
        return id;
    }

    public LocalDateTime getRegistrationTime() {
        return registrationTime;
    }

    public DomainEventType getDomainEventType() {
        return domainEventType;
    }

    public abstract AggregateId<T> getAggregateId();

    public String getAggregateType() {
        return aggregateType;
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

    public Map<String, Object> getAttributesMap() {
        return Collections.unmodifiableMap(additionalAttributes);
    }

    public void setAuditUser(AuditUser auditUser) {
        Condition.notNull(auditUser);
        this.userId = auditUser.getUserId();
        this.userExternalId = auditUser.getExternalId();
        this.userDisplayName = auditUser.getDisplayName();
        this.userEmail = auditUser.getEmail();
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DomainEvent)) return false;
        DomainEvent that = (DomainEvent) o;
        return Objects.equals(id, that.id);
    }
}
