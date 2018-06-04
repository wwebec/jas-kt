package ch.admin.seco.jobs.services.jobadservice.infrastructure.messagebroker.avam;

import static ch.admin.seco.jobs.services.jobadservice.infrastructure.messagebroker.JobAdvertisementAction.APPROVE;
import static ch.admin.seco.jobs.services.jobadservice.infrastructure.messagebroker.JobAdvertisementAction.CANCEL;
import static ch.admin.seco.jobs.services.jobadservice.infrastructure.messagebroker.JobAdvertisementAction.CREATE_FROM_AVAM;
import static ch.admin.seco.jobs.services.jobadservice.infrastructure.messagebroker.JobAdvertisementAction.REJECT;
import static ch.admin.seco.jobs.services.jobadservice.infrastructure.messagebroker.messages.MessageHeaders.ACTION;
import static ch.admin.seco.jobs.services.jobadservice.infrastructure.messagebroker.messages.MessageHeaders.PARTITION_KEY;
import static ch.admin.seco.jobs.services.jobadservice.infrastructure.messagebroker.messages.MessageHeaders.SOURCE_SYSTEM;
import static ch.admin.seco.jobs.services.jobadservice.infrastructure.messagebroker.messages.MessageHeaders.TARGET_SYSTEM;
import static ch.admin.seco.jobs.services.jobadservice.infrastructure.messagebroker.messages.MessageSystem.AVAM;
import static ch.admin.seco.jobs.services.jobadservice.infrastructure.messagebroker.messages.MessageSystem.JOB_AD_SERVICE;

import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.MessageChannel;

import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.create.CreateJobAdvertisementFromAvamDto;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.update.ApprovalDto;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.update.CancellationDto;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.update.RejectionDto;

@EnableBinding(Source.class)
public class AvamSource {

    private MessageChannel output;

    public AvamSource(MessageChannel output) {
        this.output = output;
    }

    public void approve(ApprovalDto approvalDto) {
        output.send(MessageBuilder
                .withPayload(approvalDto)
                .setHeader(PARTITION_KEY, approvalDto.getStellennummerEgov())
                .setHeader(ACTION, APPROVE.name())
                .setHeader(SOURCE_SYSTEM, AVAM.name())
                .setHeader(TARGET_SYSTEM, JOB_AD_SERVICE.name())
                .build());
    }

    public void reject(RejectionDto rejectionDto) {
        output.send(MessageBuilder
                .withPayload(rejectionDto)
                .setHeader(PARTITION_KEY, rejectionDto.getStellennummerEgov())
                .setHeader(ACTION, REJECT.name())
                .setHeader(SOURCE_SYSTEM, AVAM.name())
                .setHeader(TARGET_SYSTEM, JOB_AD_SERVICE.name())
                .build());
    }

    public void create(CreateJobAdvertisementFromAvamDto createJobAdvertisementFromAvamDto) {
        output.send(MessageBuilder
                .withPayload(createJobAdvertisementFromAvamDto)
                .setHeader(PARTITION_KEY, createJobAdvertisementFromAvamDto.getStellennummerAvam())
                .setHeader(ACTION, CREATE_FROM_AVAM.name())
                .setHeader(SOURCE_SYSTEM, AVAM.name())
                .setHeader(TARGET_SYSTEM, JOB_AD_SERVICE.name())
                .build());
    }

    public void cancel(CancellationDto cancellationDto) {
        output.send(MessageBuilder
                .withPayload(cancellationDto)
                .setHeader(PARTITION_KEY, cancellationDto.getStellennummerAvam())
                .setHeader(ACTION, CANCEL.name())
                .setHeader(SOURCE_SYSTEM, AVAM.name())
                .setHeader(TARGET_SYSTEM, JOB_AD_SERVICE.name())
                .build());
    }
}
