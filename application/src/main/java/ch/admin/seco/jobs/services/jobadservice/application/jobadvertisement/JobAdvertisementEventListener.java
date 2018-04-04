package ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement;

import ch.admin.seco.jobs.services.jobadservice.core.domain.AggregateNotFoundException;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisementId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisement;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisementRepository;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.SourceSystem;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.events.JobAdvertisementApprovedEvent;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.events.JobAdvertisementBlackoutExpiredEvent;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.events.JobAdvertisementCreatedEvent;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.events.JobAdvertisementPublishExpiredEvent;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.events.JobAdvertisementRefinedEvent;

import java.util.Optional;

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

    @EventListener
    void onCreated(JobAdvertisementCreatedEvent event) {
        LOG.debug("EVENT caught for internal: JOB_ADVERTISEMENT_CREATED for JobAdvertisementId: '{}'", event.getAggregateId().getValue());
        final JobAdvertisement jobAdvertisement = getJobAdvertisement(event.getAggregateId());
        if (jobAdvertisement.isReportingObligation() || jobAdvertisement.isReportToAvam() || jobAdvertisement.getSourceSystem().equals(SourceSystem.JOBROOM)) {
            jobAdvertisementApplicationService.inspect(jobAdvertisement.getId());
        } else {
            jobAdvertisementApplicationService.refining(jobAdvertisement.getId());
        }
    }

    @EventListener
    void onApproved(JobAdvertisementApprovedEvent event) {
        LOG.debug("EVENT caught for internal: JOB_ADVERTISEMENT_APPROVED for JobAdvertisementId: '{}'", event.getAggregateId().getValue());
        jobAdvertisementApplicationService.refining(event.getAggregateId());
    }

    @EventListener
    void onRefined(JobAdvertisementRefinedEvent event) {
        LOG.debug("EVENT caught for internal: JOB_ADVERTISEMENT_REFINED for JobAdvertisementId: '{}'", event.getAggregateId().getValue());
        jobAdvertisementApplicationService.publish(event.getAggregateId());
    }

    @EventListener
    void onBlackoutExpired(JobAdvertisementBlackoutExpiredEvent event) {
        LOG.debug("EVENT caught for internal: JOB_ADVERTISEMENT_BLACKOUT_EXPIRED for JobAdvertisementId: '{}'", event.getAggregateId().getValue());
        jobAdvertisementApplicationService.publish(event.getAggregateId());
    }

    @EventListener
    void onPublishExpired(JobAdvertisementPublishExpiredEvent event) {
        LOG.debug("EVENT caught for internal: JOB_ADVERTISEMENT_PUBLISH_EXPIRED for JobAdvertisementId: '{}'", event.getAggregateId().getValue());
        jobAdvertisementApplicationService.archive(event.getAggregateId());
    }

    private JobAdvertisement getJobAdvertisement(JobAdvertisementId jobAdvertisementId) throws AggregateNotFoundException {
        Optional<JobAdvertisement> jobAdvertisement = jobAdvertisementRepository.findById(jobAdvertisementId);
        return jobAdvertisement.orElseThrow(() -> new AggregateNotFoundException(JobAdvertisement.class, jobAdvertisementId.getValue()));
    }

}
