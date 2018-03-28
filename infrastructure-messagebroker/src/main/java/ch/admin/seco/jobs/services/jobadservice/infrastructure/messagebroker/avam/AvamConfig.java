package ch.admin.seco.jobs.services.jobadservice.infrastructure.messagebroker.avam;

import static ch.admin.seco.jobs.services.jobadservice.infrastructure.messagebroker.MessageBrokerChannels.JOB_AD_EVENT_CHANNEL;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.MessageChannel;

import ch.admin.seco.jobs.services.jobadservice.core.domain.events.DomainEventPublisher;

@Configuration
public class AvamConfig {

    @Bean
    public AvamService avamService(
            DomainEventPublisher domainEventPublisher,
            @Qualifier(JOB_AD_EVENT_CHANNEL) MessageChannel jobAdEventChannel) {
        return new AvamService(domainEventPublisher, jobAdEventChannel);
    }
}
