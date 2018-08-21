package ch.admin.seco.jobs.services.jobadservice.infrastructure.integration;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(IntegrationBasisProperties.class)
public class IntegrationBasisConfig {

    private final IntegrationBasisProperties integrationBasisProperties;

    public IntegrationBasisConfig(IntegrationBasisProperties integrationBasisProperties) {
        this.integrationBasisProperties = integrationBasisProperties;
    }

    @Bean
    RetryAdviceFactory retryAdviceFactory() {
        return new RetryAdviceFactory(this.integrationBasisProperties.getBackOffPeriod(), this.integrationBasisProperties.getMaxAttempts());
    }
}
