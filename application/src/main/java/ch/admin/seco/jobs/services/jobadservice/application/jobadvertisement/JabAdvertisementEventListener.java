package ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement;

import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisement;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisementEvent;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisementRepository;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.SourceSystem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisementEvents.*;

@Component
public class JabAdvertisementEventListener {

    private final JobAdvertisementRepository jobAdvertisementRepository;
    private final JobAdvertisementApplicationService jobAdvertisementApplicationService;

    @Autowired
    public JabAdvertisementEventListener(JobAdvertisementRepository jobAdvertisementRepository, JobAdvertisementApplicationService jobAdvertisementApplicationService) {
        this.jobAdvertisementRepository = jobAdvertisementRepository;
        this.jobAdvertisementApplicationService = jobAdvertisementApplicationService;
    }

    @EventListener
    void sendToInspection(JobAdvertisementEvent jobAdvertisementEvent) {
        if (!JOB_ADVERTISEMENT_CREATED.getDomainEventType().equals(jobAdvertisementEvent.getDomainEventType())) {
            return;
        }
        final JobAdvertisement jobAdvertisement = jobAdvertisementRepository.getOne(jobAdvertisementEvent.getJobAdvertisementId());
        if (jobAdvertisement.isReportingObligation() || jobAdvertisement.getSourceSystem().equals(SourceSystem.JOBROOM)) {
            jobAdvertisementApplicationService.inspect(jobAdvertisement.getId());
        }
        jobAdvertisementApplicationService.refining(jobAdvertisement.getId());
    }

    @EventListener
    void publish(JobAdvertisementEvent jobAdvertisementEvent) {
        if (!JOB_ADVERTISEMENT_REFINED.getDomainEventType().equals(jobAdvertisementEvent.getDomainEventType())) {
            return;
        }
        jobAdvertisementApplicationService.publish(jobAdvertisementEvent.getJobAdvertisementId());
    }

    @EventListener
    void blackoutExpiring(JobAdvertisementEvent jobAdvertisementEvent) {
        if (!JOB_ADVERTISEMENT_BLACKOUT_EXPIRED.getDomainEventType().equals(jobAdvertisementEvent.getDomainEventType())) {
            return;
        }
        jobAdvertisementApplicationService.publish(jobAdvertisementEvent.getJobAdvertisementId());
    }

    @EventListener
    void publishExpiring(JobAdvertisementEvent jobAdvertisementEvent) {
        if (!JOB_ADVERTISEMENT_PUBLISH_EXPIRED.getDomainEventType().equals(jobAdvertisementEvent.getDomainEventType())) {
            return;
        }
        jobAdvertisementApplicationService.archive(jobAdvertisementEvent.getJobAdvertisementId());
    }

}
