package ch.admin.seco.jobs.services.jobadservice.infrastructure.messagebroker;

import static ch.admin.seco.jobs.services.jobadservice.infrastructure.messagebroker.MessageBrokerChannels.JOB_AD_EVENT_CHANNEL;

import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.integration.channel.NullChannel;
import org.springframework.messaging.MessageChannel;

import ch.admin.seco.jobs.services.jobadservice.application.ProfileRegistry;

@Configuration
public class MessageBrokerConfig {

    @Configuration
    @Profile('!' + ProfileRegistry.AVAM_MOCK)
    @EnableBinding(MessageBrokerChannels.class)
    static class DefaultMessageBroker {}

    @Configuration
    @Profile(ProfileRegistry.AVAM_MOCK)
    static class MockedMessageBroker {

        @Bean(JOB_AD_EVENT_CHANNEL)
        MessageChannel jobAdEventChannel() {
            return new NullChannel();
            // use QueueChannel to catch messages
            // or trigger a mocked response, which will be received by the StreamListener
        }
    }
}
