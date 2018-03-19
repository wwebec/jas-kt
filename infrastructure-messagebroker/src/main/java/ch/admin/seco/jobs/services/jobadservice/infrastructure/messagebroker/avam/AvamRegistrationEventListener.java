package ch.admin.seco.jobs.services.jobadservice.infrastructure.messagebroker.avam;

import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.events.JobAdvertisementCancelledEvent;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.events.JobAdvertisementEvent;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.events.JobAdvertisementInspectingEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class AvamRegistrationEventListener {

    private final AvamTaskRepository avamTaskRepository;

    public AvamRegistrationEventListener(AvamTaskRepository avamTaskRepository) {
        this.avamTaskRepository = avamTaskRepository;
    }

    @EventListener
    protected void handle(JobAdvertisementInspectingEvent event) {
        this.avamTaskRepository.save(new AvamTask(new AvamTaskId(), event.getAggregateId(), AvamTaskType.REGISTER));
    }

    @EventListener
    protected void handle(JobAdvertisementCancelledEvent event) {
        this.avamTaskRepository.save(new AvamTask(new AvamTaskId(), event.getAggregateId(), AvamTaskType.DEREGISTER));
    }
}
