package ch.admin.seco.jobs.services.jobadservice.infrastructure.web.eureka;

import com.netflix.appinfo.HealthCheckHandler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.HealthAggregator;
import org.springframework.boot.actuate.health.OrderedHealthAggregator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(EurekaClientProperties.class)
@ConditionalOnProperty(value = "eureka.client.enabled", matchIfMissing = true)
class EurekaClientConfig {

    private HealthAggregator healthAggregator = new OrderedHealthAggregator();

    private final EurekaClientProperties eurekaClientProperties;

    EurekaClientConfig(EurekaClientProperties eurekaClientProperties) {
        this.eurekaClientProperties = eurekaClientProperties;
    }

    @Bean
    @ConditionalOnProperty(value = "eureka.client.healthcheck.enabled")
    HealthCheckHandler eurekaHealthCheckHandler() {
        return new IgnoreAwareEurekaHealthCheckHandler(this.healthAggregator, this.eurekaClientProperties.getIgnoredHealthIndicators());
    }

    @Autowired(required = false)
    void setHealthAggregator(HealthAggregator healthAggregator) {
        this.healthAggregator = healthAggregator;
    }
}
