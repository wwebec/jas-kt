package ch.admin.seco.jobs.services.jobadservice.infrastructure.elasticsearch.write.jobadvertisement;

import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisement;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisementRepository;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.events.JobAdvertisementPublishExpiredEvent;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.events.JobAdvertisementPublishPublicEvent;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.events.JobAdvertisementPublishRestrictedEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.Optional;

@Component("job-advertisement-event-listener")
public class IndexerEventListener {
    private static Logger LOG = LoggerFactory.getLogger(IndexerEventListener.class);

    private JobAdvertisementElasticsearchRepository jobAdvertisementElasticsearchRepository;
    private JobAdvertisementRepository jobAdvertisementJpaRepository;

    public IndexerEventListener(JobAdvertisementElasticsearchRepository jobAdvertisementElasticsearchRepository,
                                JobAdvertisementRepository jobAdvertisementJpaRepository) {
        this.jobAdvertisementElasticsearchRepository = jobAdvertisementElasticsearchRepository;
        this.jobAdvertisementJpaRepository = jobAdvertisementJpaRepository;
    }

    @TransactionalEventListener
    public void handle(JobAdvertisementPublishRestrictedEvent event) {
        Optional<JobAdvertisement> jobAdvertisementOptional = this.jobAdvertisementJpaRepository.findById(event.getAggregateId());
        if (jobAdvertisementOptional.isPresent()) {
            this.jobAdvertisementElasticsearchRepository.save(new JobAdvertisementDocument(jobAdvertisementOptional.get()));
        } else {
            LOG.warn("JobAdvertisement not found for the given id: {}", event.getAggregateId());
        }

    }

    @TransactionalEventListener
    public void handle(JobAdvertisementPublishPublicEvent event) {
        Optional<JobAdvertisement> jobAdvertisementOptional = this.jobAdvertisementJpaRepository.findById(event.getAggregateId());
        if (jobAdvertisementOptional.isPresent()) {
            this.jobAdvertisementElasticsearchRepository.save(new JobAdvertisementDocument(jobAdvertisementOptional.get()));
        } else {
            this.jobAdvertisementElasticsearchRepository.deleteById(event.getAggregateId().getValue());
        }
    }

    @TransactionalEventListener
    public void handle(JobAdvertisementPublishExpiredEvent event) {
        final String id = event.getAggregateId().getValue();
        this.jobAdvertisementElasticsearchRepository.deleteById(id);
    }

}
