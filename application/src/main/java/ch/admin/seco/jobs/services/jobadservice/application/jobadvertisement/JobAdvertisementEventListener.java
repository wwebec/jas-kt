package ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement;

import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisement;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisementRepository;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.SourceSystem;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.events.JobAdvertisementBlackoutExpiredEvent;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.events.JobAdvertisementCreatedEvent;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.events.JobAdvertisementPublishExpiredEvent;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.events.JobAdvertisementRefinedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

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
    void onCreated(JobAdvertisementCreatedEvent event) {
        final JobAdvertisement jobAdvertisement = jobAdvertisementRepository.getOne(event.getAggregateId());
        if (jobAdvertisement.isReportingObligation() || jobAdvertisement.isReportToRav() || jobAdvertisement.getSourceSystem().equals(SourceSystem.JOBROOM)) {
            jobAdvertisementApplicationService.inspect(jobAdvertisement.getId());
        } else {
            jobAdvertisementApplicationService.refining(jobAdvertisement.getId());
        }
    }

    @EventListener
    void onRefined(JobAdvertisementRefinedEvent event) {
        jobAdvertisementApplicationService.publish(event.getAggregateId());
    }

    @EventListener
    void onBlackoutExpired(JobAdvertisementBlackoutExpiredEvent event) {
        jobAdvertisementApplicationService.publish(event.getAggregateId());
    }

    @EventListener
    void onPublishExpired(JobAdvertisementPublishExpiredEvent event) {
        jobAdvertisementApplicationService.archive(event.getAggregateId());
    }

}
