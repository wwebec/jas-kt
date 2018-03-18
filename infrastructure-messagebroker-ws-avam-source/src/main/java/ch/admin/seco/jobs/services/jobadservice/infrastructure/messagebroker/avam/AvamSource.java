package ch.admin.seco.jobs.services.jobadservice.infrastructure.messagebroker.avam;

import static ch.admin.seco.jobs.services.jobadservice.infrastructure.messagebroker.JobAdvertisementAction.APPROVE;
import static ch.admin.seco.jobs.services.jobadservice.infrastructure.messagebroker.JobAdvertisementAction.CANCEL;
import static ch.admin.seco.jobs.services.jobadservice.infrastructure.messagebroker.JobAdvertisementAction.CREATE_OR_UPDATE;
import static ch.admin.seco.jobs.services.jobadservice.infrastructure.messagebroker.JobAdvertisementAction.REJECT;
import static ch.admin.seco.jobs.services.jobadservice.infrastructure.messagebroker.MessageHeaders.ACTION;
import static ch.admin.seco.jobs.services.jobadservice.infrastructure.messagebroker.MessageHeaders.SOURCE_SYSTEM;
import static ch.admin.seco.jobs.services.jobadservice.infrastructure.messagebroker.MessageHeaders.TARGET_SYSTEM;
import static ch.admin.seco.jobs.services.jobadservice.infrastructure.messagebroker.MessageSystem.AVAM;
import static ch.admin.seco.jobs.services.jobadservice.infrastructure.messagebroker.MessageSystem.JOB_AD_SERVICE;

import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.MessageChannel;

import ch.admin.seco.jobs.services.jobadservice.infrastructure.messagebroker.messages.ApproveJobAdvertisementMessage;
import ch.admin.seco.jobs.services.jobadservice.infrastructure.messagebroker.messages.CancelJobAdvertisementMessage;
import ch.admin.seco.jobs.services.jobadservice.infrastructure.messagebroker.messages.CreateOrUpdateJobAdvertisementMessage;
import ch.admin.seco.jobs.services.jobadservice.infrastructure.messagebroker.messages.RejectJobAdvertisementMessage;

@EnableBinding(Source.class)
public class AvamSource {

    private MessageChannel output;

    public AvamSource(MessageChannel output) {
        this.output = output;
    }

    public void approve(ApproveJobAdvertisementMessage approveJobAdvertisement) {
        output.send(MessageBuilder
                .withPayload(approveJobAdvertisement)
                .setHeader(ACTION, APPROVE)
                .setHeader(SOURCE_SYSTEM, AVAM)
                .setHeader(TARGET_SYSTEM, JOB_AD_SERVICE)
                .build());
    }

    public void reject(RejectJobAdvertisementMessage rejectJobAdvertisement) {
        output.send(MessageBuilder
                .withPayload(rejectJobAdvertisement)
                .setHeader(ACTION, REJECT)
                .setHeader(SOURCE_SYSTEM, AVAM)
                .setHeader(TARGET_SYSTEM, JOB_AD_SERVICE)
                .build());
    }

    public void createOrUpdate(CreateOrUpdateJobAdvertisementMessage createOrUpdateJobAdvertisementMessage) {
        output.send(MessageBuilder
                .withPayload(createOrUpdateJobAdvertisementMessage)
                .setHeader(ACTION, CREATE_OR_UPDATE)
                .setHeader(SOURCE_SYSTEM, AVAM)
                .setHeader(TARGET_SYSTEM, JOB_AD_SERVICE)
                .build());
    }

    public void cancel(CancelJobAdvertisementMessage cancelJobAdvertisement) {
        output.send(MessageBuilder
                .withPayload(cancelJobAdvertisement)
                .setHeader(ACTION, CANCEL)
                .setHeader(SOURCE_SYSTEM, AVAM)
                .setHeader(TARGET_SYSTEM, JOB_AD_SERVICE)
                .build());
    }
}
