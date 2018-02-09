package ch.admin.seco.jobs.services.jobadservice.core.domain.events;

import java.util.Map;

public interface AuditAttributeEnricher {

    boolean supports(DomainEvent domainEvent);

    Map<String, Object> enrichAttributes(DomainEvent domainEvent);

}
