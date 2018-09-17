package ch.admin.seco.jobs.services.jobadservice.infrastructure.messagebroker.dlq;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import ch.admin.seco.jobs.services.jobadservice.application.MailSenderService;
import ch.admin.seco.jobs.services.jobadservice.application.ProfileRegistry;

@Configuration
@EnableConfigurationProperties(DLQItemProperties.class)
public class DLQItemConfig {

    private final DLQItemProperties dlqItemProperties;

    private final DLQItemRepository dlqItemRepository;

    private final ObjectMapper objectMapper;

    private final MailSenderService mailSenderService;

    public DLQItemConfig(DLQItemProperties dlqItemProperties, DLQItemRepository dlqItemRepository, ObjectMapper objectMapper, MailSenderService mailSenderService) {
        this.dlqItemProperties = dlqItemProperties;
        this.dlqItemRepository = dlqItemRepository;
        this.objectMapper = objectMapper;
        this.mailSenderService = mailSenderService;
    }

    @Bean
    public DLQItemService dlqItemService() {
        return new DLQItemService(this.dlqItemRepository, this.objectMapper, this.mailSenderService, this.dlqItemProperties);
    }

    @Bean
    public DLQItemActuatorEndpoint dlqItemActuatorEndpoint() {
        return new DLQItemActuatorEndpoint(this.dlqItemService());
    }

    @Bean
    public DlqItemHealthIndicator dlqItemHealthIndicator() {
        return new DlqItemHealthIndicator(this.dlqItemRepository);
    }

    @Configuration
    @Profile('!' + ProfileRegistry.AVAM_MOCK)
    @EnableBinding(DLQChannels.class)
    static class DefaultMessageBroker {

    }

}
