package ch.admin.seco.jobs.services.jobadservice.infrastructure.messagebroker.avam;

import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.SourceSystem;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.events.JobAdvertisementCancelledEvent;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.events.JobAdvertisementInspectingEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class AvamRegistrationEventListener {

    private static Logger LOG = LoggerFactory.getLogger(AvamRegistrationEventListener.class);

    private final AvamTaskRepository avamTaskRepository;

    public AvamRegistrationEventListener(AvamTaskRepository avamTaskRepository) {
        this.avamTaskRepository = avamTaskRepository;
    }

    @EventListener
    protected void handle(JobAdvertisementInspectingEvent event) {
        LOG.debug("EVENT caught for AVAM: JOB_ADVERTISEMENT_INSPECTING for JobAdvertisementId: '{}'", event.getAggregateId().getValue());
        this.avamTaskRepository.save(new AvamTask(new AvamTaskId(), event.getAggregateId(), AvamTaskType.REGISTER));
    }

    @EventListener
    protected void handle(JobAdvertisementCancelledEvent event) {
        if (shouldBeSend(event)) {
            LOG.debug("EVENT caught for AVAM: JOB_ADVERTISEMENT_CANCELLED for JobAdvertisementId: '{}'", event.getAggregateId().getValue());
            this.avamTaskRepository.save(new AvamTask(new AvamTaskId(), event.getAggregateId(), AvamTaskType.DEREGISTER));
        }
    }

    private boolean shouldBeSend(JobAdvertisementCancelledEvent event) {
        Object cancelledBy = event.getAttributesMap().get("cancelledBy");
        if (cancelledBy == null) {
            return true;
        }
        return !cancelledBy.equals(SourceSystem.RAV);
    }
}
