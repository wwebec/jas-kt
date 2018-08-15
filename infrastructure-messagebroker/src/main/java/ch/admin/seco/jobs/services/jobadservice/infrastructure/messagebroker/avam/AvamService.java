package ch.admin.seco.jobs.services.jobadservice.infrastructure.messagebroker.avam;

import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.events.JobAdvertisementEvents.JOB_ADVERTISEMENT_CANCELLED;
import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.events.JobAdvertisementEvents.JOB_ADVERTISEMENT_INSPECTING;
import static ch.admin.seco.jobs.services.jobadservice.infrastructure.messagebroker.MessageBrokerChannels.APPROVE_CONDITION;
import static ch.admin.seco.jobs.services.jobadservice.infrastructure.messagebroker.MessageBrokerChannels.CANCEL_CONDITION;
import static ch.admin.seco.jobs.services.jobadservice.infrastructure.messagebroker.MessageBrokerChannels.CREATE_FROM_AVAM_CONDITION;
import static ch.admin.seco.jobs.services.jobadservice.infrastructure.messagebroker.MessageBrokerChannels.JOB_AD_ACTION_CHANNEL;
import static ch.admin.seco.jobs.services.jobadservice.infrastructure.messagebroker.MessageBrokerChannels.REJECT_CONDITION;
import static ch.admin.seco.jobs.services.jobadservice.infrastructure.messagebroker.messages.MessageHeaders.EVENT;
import static ch.admin.seco.jobs.services.jobadservice.infrastructure.messagebroker.messages.MessageHeaders.PARTITION_KEY;
import static ch.admin.seco.jobs.services.jobadservice.infrastructure.messagebroker.messages.MessageHeaders.SOURCE_SYSTEM;
import static ch.admin.seco.jobs.services.jobadservice.infrastructure.messagebroker.messages.MessageHeaders.TARGET_SYSTEM;
import static ch.admin.seco.jobs.services.jobadservice.infrastructure.messagebroker.messages.MessageSystem.AVAM;
import static ch.admin.seco.jobs.services.jobadservice.infrastructure.messagebroker.messages.MessageSystem.JOB_AD_SERVICE;

import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.SourceSystem;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.MessageChannel;

import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.JobAdvertisementAlreadyExistsException;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.JobAdvertisementApplicationService;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.JobAdvertisementDto;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.create.CreateJobAdvertisementFromAvamDto;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.update.ApprovalDto;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.update.CancellationDto;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.update.RejectionDto;
import ch.admin.seco.jobs.services.jobadservice.core.conditions.Condition;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisement;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisementId;

public class AvamService {

    private final Logger LOG = LoggerFactory.getLogger(AvamService.class);

    private final MessageChannel jobAdEventChannel;

    private final JobAdvertisementApplicationService jobAdvertisementApplicationService;

    public AvamService(JobAdvertisementApplicationService jobAdvertisementApplicationService, MessageChannel jobAdEventChannel) {
        this.jobAdvertisementApplicationService = jobAdvertisementApplicationService;
        this.jobAdEventChannel = jobAdEventChannel;
    }

    public void register(JobAdvertisement jobAdvertisement) {
        LOG.debug("Send through the message broker for action: REGISTER, JobAdvertisementId: '{}'", jobAdvertisement.getId().getValue());
        jobAdEventChannel.send(MessageBuilder
                .withPayload(jobAdvertisement)
                .setHeader(PARTITION_KEY, jobAdvertisement.getId())
                .setHeader(EVENT, JOB_ADVERTISEMENT_INSPECTING.getDomainEventType().getValue())
                .setHeader(SOURCE_SYSTEM, JOB_AD_SERVICE.name())
                .setHeader(TARGET_SYSTEM, AVAM.name())
                .build());
    }

    public void deregister(JobAdvertisement jobAdvertisement) {
        LOG.debug("Send through the message broker for action: DEREGISTER, JobAdvertisementId: '{}'", jobAdvertisement.getId().getValue());
        jobAdEventChannel.send(MessageBuilder
                .withPayload(jobAdvertisement)
                .setHeader(PARTITION_KEY, jobAdvertisement.getId())
                .setHeader(EVENT, JOB_ADVERTISEMENT_CANCELLED.getDomainEventType().getValue())
                .setHeader(SOURCE_SYSTEM, JOB_AD_SERVICE.name())
                .setHeader(TARGET_SYSTEM, AVAM.name())
                .build());
    }

    @StreamListener(target = JOB_AD_ACTION_CHANNEL, condition = APPROVE_CONDITION)
    public void handleApprovedAction(ApprovalDto approvalDto) {
        jobAdvertisementApplicationService.approve(approvalDto);
    }

    @StreamListener(target = JOB_AD_ACTION_CHANNEL, condition = REJECT_CONDITION)
    public void handleRejectAction(RejectionDto rejectionDto) {
        jobAdvertisementApplicationService.reject(rejectionDto);
    }

    @StreamListener(target = JOB_AD_ACTION_CHANNEL, condition = CREATE_FROM_AVAM_CONDITION)
    public void handleCreateAction(CreateJobAdvertisementFromAvamDto createJobAdvertisementFromAvamDto) {
        try {
            jobAdvertisementApplicationService.createFromAvam(createJobAdvertisementFromAvamDto);
        } catch (JobAdvertisementAlreadyExistsException e) {
            LOG.debug(e.getMessage());
        }
    }

    @StreamListener(target = JOB_AD_ACTION_CHANNEL, condition = CANCEL_CONDITION)
    public void handleCancelAction(CancellationDto cancellationDto) {
        JobAdvertisementDto jobAdvertisementDto;
        if(StringUtils.isNotBlank(cancellationDto.getStellennummerEgov())) {
            jobAdvertisementDto = jobAdvertisementApplicationService.findByStellennummerEgov(cancellationDto.getStellennummerEgov());
        } else {
            jobAdvertisementDto = jobAdvertisementApplicationService.findByStellennummerAvam(cancellationDto.getStellennummerAvam());
        }
        Condition.notNull(jobAdvertisementDto, "Couldn't find the jobAdvertisement for stellennummerEgov %s nor stellennummerAvam %s", cancellationDto.getStellennummerEgov(), cancellationDto.getStellennummerAvam());
        jobAdvertisementApplicationService.cancel(new JobAdvertisementId(jobAdvertisementDto.getId()), cancellationDto.getDate(), cancellationDto.getCode(), SourceSystem.RAV, null);
    }
}
