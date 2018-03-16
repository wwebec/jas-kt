package ch.admin.seco.jobs.services.jobadservice.infrastructure.messagebroker.avam;

import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisementEvents.JOB_ADVERTISEMENT_APPROVED;
import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisementEvents.JOB_ADVERTISEMENT_CANCELLED;
import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisementEvents.JOB_ADVERTISEMENT_REFINING;
import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisementEvents.JOB_ADVERTISEMENT_REJECTED;
import static ch.admin.seco.jobs.services.jobadservice.infrastructure.messagebroker.MessageHeaders.EVENT;
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
import ch.admin.seco.jobs.services.jobadservice.infrastructure.messagebroker.messages.RejectJobAdvertisementMessage;
import ch.admin.seco.jobs.services.jobadservice.infrastructure.messagebroker.messages.UpdateJobAdvertisementMessage;

@EnableBinding(Source.class)
public class AvamSource {

    private MessageChannel output;

    public AvamSource(MessageChannel output) {
        this.output = output;
    }

    public void approve(ApproveJobAdvertisementMessage approveJobAdvertisement) {
        output.send(MessageBuilder
                .withPayload(approveJobAdvertisement)
                .setHeader(EVENT, JOB_ADVERTISEMENT_APPROVED)
                .setHeader(SOURCE_SYSTEM, AVAM)
                .setHeader(TARGET_SYSTEM, JOB_AD_SERVICE)
                .build());
    }

    public void reject(RejectJobAdvertisementMessage rejectJobAdvertisement) {
        output.send(MessageBuilder
                .withPayload(rejectJobAdvertisement)
                .setHeader(EVENT, JOB_ADVERTISEMENT_REJECTED)
                .setHeader(SOURCE_SYSTEM, AVAM)
                .setHeader(TARGET_SYSTEM, JOB_AD_SERVICE)
                .build());
    }

    public void update(UpdateJobAdvertisementMessage updateJobAdvertisement) {
        output.send(MessageBuilder
                .withPayload(updateJobAdvertisement)
                .setHeader(EVENT, JOB_ADVERTISEMENT_REFINING)
                .setHeader(SOURCE_SYSTEM, AVAM)
                .setHeader(TARGET_SYSTEM, JOB_AD_SERVICE)
                .build());
    }

    public void cancel(CancelJobAdvertisementMessage cancelJobAdvertisement) {
        output.send(MessageBuilder
                .withPayload(cancelJobAdvertisement)
                .setHeader(EVENT, JOB_ADVERTISEMENT_CANCELLED)
                .setHeader(SOURCE_SYSTEM, AVAM)
                .setHeader(TARGET_SYSTEM, JOB_AD_SERVICE)
                .build());
    }
}
