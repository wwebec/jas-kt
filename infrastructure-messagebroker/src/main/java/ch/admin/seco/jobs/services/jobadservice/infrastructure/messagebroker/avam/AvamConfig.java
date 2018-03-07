package ch.admin.seco.jobs.services.jobadservice.infrastructure.messagebroker.avam;

import ch.admin.seco.jobs.services.jobadservice.application.RavRegistrationService;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.ProfileRegistry;
import ch.admin.seco.jobs.services.jobadservice.core.domain.events.DomainEventPublisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Processor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.messaging.MessageChannel;

@Configuration
public class AvamConfig {

    @Configuration
    @Profile(ProfileRegistry.AVAM_MOCK)
    static class MockedAvamConfig {

        private final DomainEventPublisher domainEventPublisher;

        @Autowired
        public MockedAvamConfig(DomainEventPublisher domainEventPublisher) {
            this.domainEventPublisher = domainEventPublisher;
        }

        @Bean
        public RavRegistrationService ravRegistrationService() {
            return new MockedAvamService(domainEventPublisher);
        }

    }

    @Configuration
    @Profile('!' + ProfileRegistry.AVAM_MOCK)
    @EnableBinding(Processor.class)
    static class DefaultAvamConfig {

        private final DomainEventPublisher domainEventPublisher;

        private final MessageChannel output;

        @Autowired
        public DefaultAvamConfig(DomainEventPublisher domainEventPublisher, MessageChannel output) {
            this.domainEventPublisher = domainEventPublisher;
            this.output = output;
        }

        @Bean
        public RavRegistrationService ravRegistrationService() {
            return new DefaultAvamService(domainEventPublisher, output);
        }

    }

}
