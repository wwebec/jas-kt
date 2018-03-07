package ch.admin.seco.jobs.services.jobadservice.infrastructure.messagebroker.avam;

import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Processor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.integration.channel.NullChannel;
import org.springframework.messaging.MessageChannel;

import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.ProfileRegistry;
import ch.admin.seco.jobs.services.jobadservice.core.domain.events.DomainEventPublisher;

@Configuration
public class AvamConfig {

    @Configuration
    @Profile(ProfileRegistry.AVAM_MOCK)
    static class MockedAvamConfig {

        @Bean
        public DefaultAvamService ravRegistrationService(DomainEventPublisher domainEventPublisher) {
            return new DefaultAvamService(domainEventPublisher, outputChannel());
        }

        @Bean
        MessageChannel outputChannel() {
            return new NullChannel();
            // use QueueChannel to catch messages
            // or trigger a mocked response, which will be received by the StreamListener
        }
    }

    @Configuration
    @Profile('!' + ProfileRegistry.AVAM_MOCK)
    @EnableBinding(Processor.class)
    static class DefaultAvamConfig {

        @Bean
        public DefaultAvamService ravRegistrationService(DomainEventPublisher domainEventPublisher, MessageChannel output) {
            return new DefaultAvamService(domainEventPublisher, output);
        }
    }
}
