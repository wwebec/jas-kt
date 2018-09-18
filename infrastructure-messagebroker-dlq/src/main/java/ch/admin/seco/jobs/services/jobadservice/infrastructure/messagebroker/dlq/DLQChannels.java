package ch.admin.seco.jobs.services.jobadservice.infrastructure.messagebroker.dlq;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

public interface DLQChannels {

    String JOB_AD_EVENT_DLQ_CHANNEL = "job-ad-event-dlq";

    String JOB_AD_ACTION_DLQ_CHANNEL = "job-ad-action-dlq";

    @Input(JOB_AD_EVENT_DLQ_CHANNEL)
    SubscribableChannel jobAdEventDLQChannel();

    @Input(JOB_AD_ACTION_DLQ_CHANNEL)
    SubscribableChannel jobAdActionDLQChannel();
}
