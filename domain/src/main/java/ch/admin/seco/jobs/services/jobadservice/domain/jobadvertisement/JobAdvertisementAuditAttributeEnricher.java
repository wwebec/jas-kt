package ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.events.JobAdvertisementEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ch.admin.seco.jobs.services.jobadservice.core.domain.events.AuditAttributeEnricher;
import ch.admin.seco.jobs.services.jobadservice.core.domain.events.DomainEvent;

@Component
public class JobAdvertisementAuditAttributeEnricher implements AuditAttributeEnricher {

    private static final Logger LOGGER = LoggerFactory.getLogger(JobAdvertisementAuditAttributeEnricher.class);

    private JobAdvertisementRepository jobAdvertisementRepository;

    @Autowired
    public JobAdvertisementAuditAttributeEnricher(JobAdvertisementRepository jobAdvertisementRepository) {
        this.jobAdvertisementRepository = jobAdvertisementRepository;
    }

    @Override
    public boolean supports(DomainEvent domainEvent) {
        return JobAdvertisementEvent.class.isInstance(domainEvent);
    }

    @Override
    public Map<String, Object> enrichAttributes(DomainEvent domainEvent) {
        JobAdvertisementEvent jobAdvertisementEvent = (JobAdvertisementEvent)domainEvent;
        final JobAdvertisementId jobAdvertisementId = jobAdvertisementEvent.getAggregateId();
        final Optional<JobAdvertisement> jobAdvertisement = jobAdvertisementRepository.findById(jobAdvertisementId);
        if(!jobAdvertisement.isPresent()) {
            LOGGER.info("JobAdvertisement with id {} was not found and can't be enrich Attributes with further information", jobAdvertisementId.getValue());
            return Collections.emptyMap();
        }
        // TODO enrich the attributes
        // return ImmutableMap.of(xxxx);
        return Collections.emptyMap();
    }

}
