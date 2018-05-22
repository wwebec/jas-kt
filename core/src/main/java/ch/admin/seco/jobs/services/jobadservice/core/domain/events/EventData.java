package ch.admin.seco.jobs.services.jobadservice.core.domain.events;

import java.time.LocalDateTime;

public class EventData {

    private String id;

    private LocalDateTime registrationTime;

    private String domainEventType;

    private String aggregateType;

    private String aggregateId;

    private String userId;

    private String userExternalId;

    private String userDisplayName;

    private String userEmail;

    private String payload;

    EventData(Builder builder) {
        this.id = builder.id;
        this.registrationTime = builder.registrationTime;
        this.domainEventType = builder.domainEventType;
        this.aggregateType = builder.aggregateType;
        this.aggregateId = builder.aggregateId;
        this.userId = builder.userId;
        this.userExternalId = builder.userExternalId;
        this.userDisplayName = builder.displayName;
        this.userEmail = builder.userEmail;
        this.payload = builder.payload;
    }

    public String getId() {
        return id;
    }

    public LocalDateTime getRegistrationTime() {
        return registrationTime;
    }

    public String getDomainEventType() {
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

    public static class Builder {

        private String id;

        private LocalDateTime registrationTime;

        private String domainEventType;

        private String aggregateType;

        private String aggregateId;

        private String userId;

        private String userExternalId;

        private String displayName;

        private String userEmail;

        private String payload;

        public EventData build() {
            return new EventData(this);
        }

        public Builder setId(String id) {
            this.id = id;
            return this;
        }

        public Builder setRegistrationTime(LocalDateTime registrationTime) {
            this.registrationTime = registrationTime;
            return this;
        }

        public Builder setDomainEventType(String domainEventType) {
            this.domainEventType = domainEventType;
            return this;
        }

        public Builder setAggregateType(String aggregateType) {
            this.aggregateType = aggregateType;
            return this;
        }

        public Builder setAggregateId(String aggregateId) {
            this.aggregateId = aggregateId;
            return this;
        }

        public Builder setUserId(String userId) {
            this.userId = userId;
            return this;
        }

        public Builder setUserExternalId(String userExternalId) {
            this.userExternalId = userExternalId;
            return this;
        }

        public Builder setDisplayName(String displayName) {
            this.displayName = displayName;
            return this;
        }

        public Builder setUserEmail(String userEmail) {
            this.userEmail = userEmail;
            return this;
        }

        public Builder setPayload(String payload) {
            this.payload = payload;
            return this;
        }
    }
}
