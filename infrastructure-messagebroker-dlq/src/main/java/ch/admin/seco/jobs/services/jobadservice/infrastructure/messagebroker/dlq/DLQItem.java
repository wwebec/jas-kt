package ch.admin.seco.jobs.services.jobadservice.infrastructure.messagebroker.dlq;

import java.time.LocalDateTime;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonRawValue;

import ch.admin.seco.jobs.services.jobadservice.core.domain.IdGenerator;

@Entity
@Table(name = "DLQ_ITEM")
public class DLQItem {

    @Id
    private String id;

    @NotNull
    private LocalDateTime errorTime;

    @NotNull
    private LocalDateTime createdAt;

    @JsonRawValue
    @NotBlank
    private String headers;

    @JsonRawValue
    @NotBlank
    @Column(length = 4000)
    private String payload;

    @NotBlank
    private String originalTopic;

    @NotBlank
    private String relevantId;

    DLQItem(LocalDateTime errorTime, String headers, String payload, String originalTopic, String relevantId) {
        this.relevantId = relevantId;
        this.id = IdGenerator.timeBasedUUID().toString();
        this.errorTime = errorTime == null ? LocalDateTime.now() : errorTime;
        this.headers = headers;
        this.payload = payload;
        this.originalTopic = originalTopic;
        this.createdAt = LocalDateTime.now();
    }

    private DLQItem() {
        //for frameworks
    }

    public String getId() {
        return id;
    }

    public LocalDateTime getErrorTime() {
        return errorTime;
    }

    public String getHeaders() {
        return headers;
    }

    public String getPayload() {
        return payload;
    }

    public String getOriginalTopic() {
        return originalTopic;
    }

    public String getRelevantId() {
        return relevantId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public DLQItem setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (o == null || getClass() != o.getClass()) { return false; }
        DLQItem dlqItem = (DLQItem) o;
        return Objects.equals(id, dlqItem.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
