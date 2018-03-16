package ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement;

import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisementEvents.JOB_ADVERTISEMENT_BLACKOUT_EXPIRED;
import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisementEvents.JOB_ADVERTISEMENT_CREATED;
import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisementEvents.JOB_ADVERTISEMENT_PUBLISH_EXPIRED;
import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisementEvents.JOB_ADVERTISEMENT_REFINED;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisement;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisementEvent;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisementRepository;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.SourceSystem;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class JobAdvertisementEventListener {

    private static Logger LOG = LoggerFactory.getLogger(JobAdvertisementMailEventListener.class);

    private final JobAdvertisementRepository jobAdvertisementRepository;
    private final JobAdvertisementApplicationService jobAdvertisementApplicationService;

    @Autowired
    public JobAdvertisementEventListener(JobAdvertisementRepository jobAdvertisementRepository, JobAdvertisementApplicationService jobAdvertisementApplicationService) {
        this.jobAdvertisementRepository = jobAdvertisementRepository;
        this.jobAdvertisementApplicationService = jobAdvertisementApplicationService;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    void onCreated(JobAdvertisementEvent jobAdvertisementEvent) {
        if (!JOB_ADVERTISEMENT_CREATED.getDomainEventType().equals(jobAdvertisementEvent.getDomainEventType())) {
            return;
        }
        LOG.debug("Listener catches event JOB_ADVERTISEMENT_CREATED for JobAdvertisementId: {}", jobAdvertisementEvent.getAggregateId());
        final JobAdvertisement jobAdvertisement = jobAdvertisementRepository.getOne(jobAdvertisementEvent.getAggregateId());
        if (jobAdvertisement.isReportingObligation() || jobAdvertisement.isReportToRav() || jobAdvertisement.getSourceSystem().equals(SourceSystem.JOBROOM)) {
            jobAdvertisementApplicationService.inspect(jobAdvertisement.getId());
        } else {
            jobAdvertisementApplicationService.refining(jobAdvertisement.getId());
        }
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    void onRefined(JobAdvertisementEvent jobAdvertisementEvent) {
        if (!JOB_ADVERTISEMENT_REFINED.getDomainEventType().equals(jobAdvertisementEvent.getDomainEventType())) {
            return;
        }
        LOG.debug("Listener catches event JOB_ADVERTISEMENT_REFINED for JobAdvertisementId: {}", jobAdvertisementEvent.getAggregateId());
        jobAdvertisementApplicationService.publish(jobAdvertisementEvent.getAggregateId());
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    void onBlackoutExpired(JobAdvertisementEvent jobAdvertisementEvent) {
        if (!JOB_ADVERTISEMENT_BLACKOUT_EXPIRED.getDomainEventType().equals(jobAdvertisementEvent.getDomainEventType())) {
            return;
        }
        LOG.debug("Listener catches event JOB_ADVERTISEMENT_BLACKOUT_EXPIRED for JobAdvertisementId: {}", jobAdvertisementEvent.getAggregateId());
        jobAdvertisementApplicationService.publish(jobAdvertisementEvent.getAggregateId());
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    void onPublishExpired(JobAdvertisementEvent jobAdvertisementEvent) {
        if (!JOB_ADVERTISEMENT_PUBLISH_EXPIRED.getDomainEventType().equals(jobAdvertisementEvent.getDomainEventType())) {
            return;
        }
        LOG.debug("Listener catches event JOB_ADVERTISEMENT_PUBLISH_EXPIRED for JobAdvertisementId: {}", jobAdvertisementEvent.getAggregateId());
        jobAdvertisementApplicationService.archive(jobAdvertisementEvent.getAggregateId());
    }

}
