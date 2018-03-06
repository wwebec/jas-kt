package ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement;

import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisementEvents.JOB_ADVERTISEMENT_BLACKOUT_EXPIRED;
import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisementEvents.JOB_ADVERTISEMENT_CREATED;
import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisementEvents.JOB_ADVERTISEMENT_PUBLISH_EXPIRED;
import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisementEvents.JOB_ADVERTISEMENT_REFINED;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisement;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisementEvent;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisementRepository;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.SourceSystem;

@Component
public class JobAdvertisementEventListener {

    private final JobAdvertisementRepository jobAdvertisementRepository;
    private final JobAdvertisementApplicationService jobAdvertisementApplicationService;

    @Autowired
    public JobAdvertisementEventListener(JobAdvertisementRepository jobAdvertisementRepository, JobAdvertisementApplicationService jobAdvertisementApplicationService) {
        this.jobAdvertisementRepository = jobAdvertisementRepository;
        this.jobAdvertisementApplicationService = jobAdvertisementApplicationService;
    }

    @EventListener
    void onCreated(JobAdvertisementEvent jobAdvertisementEvent) {
        if (!JOB_ADVERTISEMENT_CREATED.getDomainEventType().equals(jobAdvertisementEvent.getDomainEventType())) {
            return;
        }
        final JobAdvertisement jobAdvertisement = jobAdvertisementRepository.getOne(jobAdvertisementEvent.getAggregateId());
        if (jobAdvertisement.isReportingObligation() || jobAdvertisement.getSourceSystem().equals(SourceSystem.JOBROOM)) {
            jobAdvertisementApplicationService.inspect(jobAdvertisement.getId());
        } else {
            jobAdvertisementApplicationService.refining(jobAdvertisement.getId());
        }
    }

    @EventListener
    void onRefined(JobAdvertisementEvent jobAdvertisementEvent) {
        if (!JOB_ADVERTISEMENT_REFINED.getDomainEventType().equals(jobAdvertisementEvent.getDomainEventType())) {
            return;
        }
        jobAdvertisementApplicationService.publish(jobAdvertisementEvent.getAggregateId());
    }

    @EventListener
    void onBlackoutExpired(JobAdvertisementEvent jobAdvertisementEvent) {
        if (!JOB_ADVERTISEMENT_BLACKOUT_EXPIRED.getDomainEventType().equals(jobAdvertisementEvent.getDomainEventType())) {
            return;
        }
        jobAdvertisementApplicationService.publish(jobAdvertisementEvent.getAggregateId());
    }

    @EventListener
    void onPublishExpired(JobAdvertisementEvent jobAdvertisementEvent) {
        if (!JOB_ADVERTISEMENT_PUBLISH_EXPIRED.getDomainEventType().equals(jobAdvertisementEvent.getDomainEventType())) {
            return;
        }
        jobAdvertisementApplicationService.archive(jobAdvertisementEvent.getAggregateId());
    }

}
