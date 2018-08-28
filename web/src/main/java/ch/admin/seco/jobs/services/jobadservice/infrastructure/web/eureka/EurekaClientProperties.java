package ch.admin.seco.jobs.services.jobadservice.infrastructure.web.eureka;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "eureka.client.healthcheck")
public class EurekaClientProperties {

    @NotNull
    private List<String> ignoredHealthIndicators = new ArrayList<>();

    public List<String> getIgnoredHealthIndicators() {
        return ignoredHealthIndicators;
    }

    public void setIgnoredHealthIndicators(List<String> ignoredHealthIndicators) {
        this.ignoredHealthIndicators = ignoredHealthIndicators;
    }
}
