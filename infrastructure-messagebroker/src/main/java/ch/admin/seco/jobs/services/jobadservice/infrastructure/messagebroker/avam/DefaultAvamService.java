package ch.admin.seco.jobs.services.jobadservice.infrastructure.messagebroker.avam;

import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.events.JobAdvertisementEvents.JOB_ADVERTISEMENT_CANCELLED;
import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.events.JobAdvertisementEvents.JOB_ADVERTISEMENT_INSPECTING;
import static ch.admin.seco.jobs.services.jobadservice.domain.profession.ProfessionCodeType.AVAM;
import static ch.admin.seco.jobs.services.jobadservice.infrastructure.messagebroker.MessageHeaders.EVENT;
import static ch.admin.seco.jobs.services.jobadservice.infrastructure.messagebroker.MessageHeaders.SOURCE_SYSTEM;
import static ch.admin.seco.jobs.services.jobadservice.infrastructure.messagebroker.MessageHeaders.TARGET_SYSTEM;
import static ch.admin.seco.jobs.services.jobadservice.infrastructure.messagebroker.MessageSystem.JOB_AD_SERVICE;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Processor;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.MessageChannel;

import ch.admin.seco.jobs.services.jobadservice.application.RavRegistrationService;
import ch.admin.seco.jobs.services.jobadservice.core.domain.events.DomainEventPublisher;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisement;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.events.JobAdvertisementEvent;

public class DefaultAvamService implements RavRegistrationService {

    private final Logger LOG = LoggerFactory.getLogger(DefaultAvamService.class);

    private final DomainEventPublisher domainEventPublisher;

    private final MessageChannel output;

    public DefaultAvamService(DomainEventPublisher domainEventPublisher, MessageChannel output) {
        this.domainEventPublisher = domainEventPublisher;
        this.output = output;
    }

    @Override
    public void register(JobAdvertisement jobAdvertisement) {
        LOG.debug("Send through the message broker for action: REGISTER, JobAdvertisementId: '{}'", jobAdvertisement.getId().getValue());
        output.send(MessageBuilder
                .withPayload(jobAdvertisement)
                .setHeader(EVENT, JOB_ADVERTISEMENT_INSPECTING.getDomainEventType().getValue())
                .setHeader(SOURCE_SYSTEM, JOB_AD_SERVICE.name())
                .setHeader(TARGET_SYSTEM, AVAM.name())
                .build());
    }

    @Override
    public void deregister(JobAdvertisement jobAdvertisement) {
        LOG.debug("Send through the message broker for action: DEREGISTER, JobAdvertisementId: '{}'", jobAdvertisement.getId().getValue());
        output.send(MessageBuilder
                .withPayload(jobAdvertisement)
                .setHeader(EVENT, JOB_ADVERTISEMENT_CANCELLED.getDomainEventType().getValue())
                .setHeader(SOURCE_SYSTEM, JOB_AD_SERVICE.name())
                .setHeader(TARGET_SYSTEM, AVAM.name())
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
