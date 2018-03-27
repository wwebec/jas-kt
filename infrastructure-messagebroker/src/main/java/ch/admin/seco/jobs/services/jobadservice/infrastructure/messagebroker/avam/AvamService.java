package ch.admin.seco.jobs.services.jobadservice.infrastructure.messagebroker.avam;

import ch.admin.seco.jobs.services.jobadservice.core.domain.events.DomainEventPublisher;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisement;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.events.JobAdvertisementEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Processor;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.MessageChannel;

import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.events.JobAdvertisementEvents.JOB_ADVERTISEMENT_CANCELLED;
import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.events.JobAdvertisementEvents.JOB_ADVERTISEMENT_INSPECTING;
import static ch.admin.seco.jobs.services.jobadservice.infrastructure.messagebroker.avam.MessageHeaders.*;
import static ch.admin.seco.jobs.services.jobadservice.infrastructure.messagebroker.avam.MessageSystem.AVAM;
import static ch.admin.seco.jobs.services.jobadservice.infrastructure.messagebroker.avam.MessageSystem.JOB_AD_SERVICE;

public class AvamService {

    private final Logger LOG = LoggerFactory.getLogger(AvamService.class);

    private final DomainEventPublisher domainEventPublisher;

    private final MessageChannel output;

    public AvamService(DomainEventPublisher domainEventPublisher, MessageChannel output) {
        this.domainEventPublisher = domainEventPublisher;
        this.output = output;
    }

    public void register(JobAdvertisement jobAdvertisement) {
        LOG.debug("Send through the message broker for action: REGISTER, JobAdvertisementId: '{}'", jobAdvertisement.getId().getValue());
        output.send(MessageBuilder
                .withPayload(jobAdvertisement)
                .setHeader(EVENT, JOB_ADVERTISEMENT_INSPECTING)
                .setHeader(SOURCE_SYSTEM, JOB_AD_SERVICE)
                .setHeader(TARGET_SYSTEM, AVAM)
                .build());
    }

    public void deregister(JobAdvertisement jobAdvertisement) {
        LOG.debug("Send through the message broker for action: DEREGISTER, JobAdvertisementId: '{}'", jobAdvertisement.getId().getValue());
        output.send(MessageBuilder
                .withPayload(jobAdvertisement)
                .setHeader(EVENT, JOB_ADVERTISEMENT_CANCELLED)
                .setHeader(SOURCE_SYSTEM, JOB_AD_SERVICE)
                .setHeader(TARGET_SYSTEM, AVAM)
                .build());
    }

    @StreamListener(target = Processor.INPUT, condition = "headers['event']=='JOB_ADVERTISEMENT_APPROVED'")
    public void handleApprovedEvent(JobAdvertisementEvent jobAdvertisementEvent) {
        domainEventPublisher.publishEvent(jobAdvertisementEvent);
    }

    @StreamListener(target = Processor.INPUT, condition = "headers['event']=='JOB_ADVERTISEMENT_REJECTED'")
    public void handleRejectedEvent(JobAdvertisementEvent jobAdvertisementEvent) {
        domainEventPublisher.publishEvent(jobAdvertisementEvent);
    }
}
