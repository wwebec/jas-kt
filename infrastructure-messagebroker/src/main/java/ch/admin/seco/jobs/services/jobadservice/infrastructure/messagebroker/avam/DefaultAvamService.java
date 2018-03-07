package ch.admin.seco.jobs.services.jobadservice.infrastructure.messagebroker.avam;

import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Processor;
import org.springframework.context.event.EventListener;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.MessageChannel;

import ch.admin.seco.jobs.services.jobadservice.application.RavRegistrationService;
import ch.admin.seco.jobs.services.jobadservice.core.domain.events.DomainEventPublisher;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisement;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisementEvent;

public class DefaultAvamService implements RavRegistrationService {


    private final DomainEventPublisher domainEventPublisher;

    private final MessageChannel output;

    public DefaultAvamService(DomainEventPublisher domainEventPublisher, MessageChannel output) {
        this.domainEventPublisher = domainEventPublisher;
        this.output = output;
    }

    @Override
    public void register(JobAdvertisement jobAdvertisement) {
        output.send(MessageBuilder
                .withPayload(jobAdvertisement)
                .setHeader("event", "JOB_ADVERTISEMENT_INSPECTING")
                .setHeader("source", "job-ad-service")
                .setHeader("target", "avam")
                .build());
    }

    @Override
    public void deregister(JobAdvertisement jobAdvertisement) {
        output.send(MessageBuilder
                .withPayload(jobAdvertisement)
                .setHeader("event", "JOB_ADVERTISEMENT_CANCELLED")
                .setHeader("source", "job-ad-service")
                .setHeader("target", "avam")
                .build());
    }

    @EventListener(condition = "#jobAdvertisementEvent.getDomainEventType().name()=='JOB_ADVERTISEMENT_INSPECTING'")
    public void register(JobAdvertisementEvent jobAdvertisementEvent) {
        // TODO extract JobAdvertisement and call register
    }

    @EventListener(condition = "#jobAdvertisementEvent.getDomainEventType().name()=='JOB_ADVERTISEMENT_CANCELLED'")
    public void deregister(JobAdvertisementEvent jobAdvertisementEvent) {
        // TODO extract JobAdvertisement and call register
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
