package ch.admin.seco.jobs.services.jobadservice.infrastructure.database.eventstore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;

import ch.admin.seco.jobs.services.jobadservice.core.domain.events.AuditAttributeEnricher;
import ch.admin.seco.jobs.services.jobadservice.core.domain.events.DomainEvent;
import ch.admin.seco.jobs.services.jobadservice.core.domain.events.DomainEventException;

class DomainEventPersistenceListener {

    private final ObjectMapper objectMapper;

    private final List<AuditAttributeEnricher> auditAttributeEnricherList = new ArrayList<>();

    private final StoredEventRepository storedEventRepository;

    DomainEventPersistenceListener(ObjectMapper objectMapper, StoredEventRepository storedEventRepository) {
        this.objectMapper = objectMapper;
        this.storedEventRepository = storedEventRepository;
    }

    @Autowired(required = false)
    public void setAuditAttributeEnrichers(List<AuditAttributeEnricher> auditAttributeEnrichers) {
        this.auditAttributeEnricherList.addAll(auditAttributeEnrichers);
    }

    @EventListener
    void persist(DomainEvent domainEvent) {
        final Map<String, Object> payload = extractAuditAttributes(domainEvent);
        try {
            storedEventRepository.save(new StoredEvent(domainEvent, objectMapper.writeValueAsString(payload)));
        } catch (JsonProcessingException e) {
            throw new DomainEventException("Could not store the Domain Event " + domainEvent, e);
        }
    }

    private Map<String, Object> extractAuditAttributes(final DomainEvent domainEvent) {
        final Map<String, Object> attributesMap = new HashMap<>(domainEvent.getAttributesMap());
        this.auditAttributeEnricherList.stream()
            .filter(auditAttributeEnricher -> auditAttributeEnricher.supports(domainEvent))
            .forEach(auditAttributeEnricher -> attributesMap.putAll(auditAttributeEnricher.enrichAttributes(domainEvent)));
        return Collections.unmodifiableMap(attributesMap);
    }
}
