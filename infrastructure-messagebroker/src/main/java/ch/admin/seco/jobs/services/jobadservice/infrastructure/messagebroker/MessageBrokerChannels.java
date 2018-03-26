package ch.admin.seco.jobs.services.jobadservice.infrastructure.messagebroker;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

public interface MessageBrokerChannels {
    String CREATE_FROM_X28_CONDITION = "headers['action']=='CREATE_FROM_X28'";
    String UPDATE_FROM_X28_CONDITION = "headers['action']=='UPDATE_FROM_X28'";
    String APPROVE_CONDITION = "headers['action']=='APPROVE'";
    String REJECT_CONDITION = "headers['action']=='REJECT'";

    String JOB_AD_ACTION_CHANNEL = "job-ad-action";
    String JOB_AD_EVENT_CHANNEL = "job-ad-event";

    @Input(JOB_AD_ACTION_CHANNEL)
    SubscribableChannel jobAdActionChannel();

    @Output(JOB_AD_EVENT_CHANNEL)
    MessageChannel jobAdEventChannel();
}
