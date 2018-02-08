package ch.admin.seco.jobs.services.jobadservice.core.domain.events;

import java.time.LocalDateTime;

public class EventData {

    private String id;

    private AggregateType aggregateType;

    private String aggregateId;

    private String domainEventType;

    private String userId;

    private String userEmail;

    private String userDisplayName;

    private LocalDateTime registrationTime;

    private String payload;

    EventData(Builder builder) {
        this.aggregateId = builder.aggregateId;
        this.aggregateType = builder.aggregateType;
        this.id = builder.id;
        this.domainEventType = builder.domainEventType;
        this.userId = builder.userId;
        this.userDisplayName = builder.displayName;
        this.registrationTime = builder.registrationTime;
        this.payload = builder.payload;
        this.userEmail = builder.userEmail;
    }

    public String getAggregateId() {
        return aggregateId;
    }

    public AggregateType getAggregateType() {
        return aggregateType;
    }

    public String getUserDisplayName() {
        return userDisplayName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setAggregateId(String aggregateId) {
        this.aggregateId = aggregateId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDomainEventType() {
        return domainEventType;
    }

    public void setDomainEventType(String domainEventType) {
        this.domainEventType = domainEventType;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public LocalDateTime getRegistrationTime() {
        return registrationTime;
    }

    public void setRegistrationTime(LocalDateTime registrationTime) {
        this.registrationTime = registrationTime;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public static class Builder {

        private String aggregateId;

        private AggregateType aggregateType;

        private String id;

        private String domainEventType;

        private String userId;

        private String displayName;

        private String userEmail;

        private LocalDateTime registrationTime;

        private String payload;

        public EventData build() {
            return new EventData(this);
        }

        public Builder setAggregateId(String aggregateId) {
            this.aggregateId = aggregateId;
            return this;
        }

        public Builder setAggregateType(AggregateType aggregateType) {
            this.aggregateType = aggregateType;
            return this;
        }

        public Builder setId(String id) {
            this.id = id;
            return this;
        }

        public Builder setDomainEventType(String domainEventType) {
            this.domainEventType = domainEventType;
            return this;
        }

        public Builder setUserId(String userId) {
            this.userId = userId;
            return this;
        }

        public Builder setDisplayName(String displayName) {
            this.displayName = displayName;
            return this;
        }

        public Builder setRegistrationTime(LocalDateTime registrationTime) {
            this.registrationTime = registrationTime;
            return this;
        }

        public Builder setPayload(String payload) {
            this.payload = payload;
            return this;
        }

        public Builder setUserEmail(String userEmail) {
            this.userEmail = userEmail;
            return this;
        }
    }
}
