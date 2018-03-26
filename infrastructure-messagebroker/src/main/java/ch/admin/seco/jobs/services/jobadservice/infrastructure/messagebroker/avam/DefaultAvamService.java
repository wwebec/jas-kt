package ch.admin.seco.jobs.services.jobadservice.infrastructure.messagebroker.avam;

import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.events.JobAdvertisementEvents.JOB_ADVERTISEMENT_CANCELLED;
import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.events.JobAdvertisementEvents.JOB_ADVERTISEMENT_INSPECTING;
import static ch.admin.seco.jobs.services.jobadservice.infrastructure.messagebroker.MessageBrokerChannels.APPROVE_CONDITION;
import static ch.admin.seco.jobs.services.jobadservice.infrastructure.messagebroker.MessageBrokerChannels.JOB_AD_ACTION_CHANNEL;
import static ch.admin.seco.jobs.services.jobadservice.infrastructure.messagebroker.MessageBrokerChannels.REJECT_CONDITION;
import static ch.admin.seco.jobs.services.jobadservice.infrastructure.messagebroker.messages.MessageHeaders.EVENT;
import static ch.admin.seco.jobs.services.jobadservice.infrastructure.messagebroker.messages.MessageHeaders.SOURCE_SYSTEM;
import static ch.admin.seco.jobs.services.jobadservice.infrastructure.messagebroker.messages.MessageHeaders.TARGET_SYSTEM;
import static ch.admin.seco.jobs.services.jobadservice.infrastructure.messagebroker.messages.MessageSystem.AVAM;
import static ch.admin.seco.jobs.services.jobadservice.infrastructure.messagebroker.messages.MessageSystem.JOB_AD_SERVICE;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.MessageChannel;

import ch.admin.seco.jobs.services.jobadservice.application.RavRegistrationService;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.ApprovalDto;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.RejectionDto;
import ch.admin.seco.jobs.services.jobadservice.core.domain.events.DomainEventPublisher;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisement;

public class DefaultAvamService implements RavRegistrationService {

    private final Logger LOG = LoggerFactory.getLogger(DefaultAvamService.class);

    private final DomainEventPublisher domainEventPublisher;

    private final MessageChannel jobAdEventChannel;

    public DefaultAvamService(DomainEventPublisher domainEventPublisher, MessageChannel jobAdEventChannel) {
        this.domainEventPublisher = domainEventPublisher;
        this.jobAdEventChannel = jobAdEventChannel;
    }

    @Override
    public void register(JobAdvertisement jobAdvertisement) {
        LOG.debug("Send through the message broker for action: REGISTER, JobAdvertisementId: '{}'", jobAdvertisement.getId().getValue());
        jobAdEventChannel.send(MessageBuilder
                .withPayload(jobAdvertisement)
                .setHeader(EVENT, JOB_ADVERTISEMENT_INSPECTING.getDomainEventType().getValue())
                .setHeader(SOURCE_SYSTEM, JOB_AD_SERVICE.name())
                .setHeader(TARGET_SYSTEM, AVAM.name())
                .build());
    }

    @Override
    public void deregister(JobAdvertisement jobAdvertisement) {
        LOG.debug("Send through the message broker for action: DEREGISTER, JobAdvertisementId: '{}'", jobAdvertisement.getId().getValue());
        jobAdEventChannel.send(MessageBuilder
                .withPayload(jobAdvertisement)
                .setHeader(EVENT, JOB_ADVERTISEMENT_CANCELLED.getDomainEventType().getValue())
                .setHeader(SOURCE_SYSTEM, JOB_AD_SERVICE.name())
                .setHeader(TARGET_SYSTEM, AVAM.name())
                .build());
    }

    @StreamListener(target = JOB_AD_ACTION_CHANNEL, condition = APPROVE_CONDITION)
    public void handleApprovedAction(ApprovalDto approvalDto) {
    }

    @StreamListener(target = JOB_AD_ACTION_CHANNEL, condition = REJECT_CONDITION)
    public void handleRejectAction(RejectionDto rejectionDto) {
    }

}
