package ch.admin.seco.jobs.services.jobadservice.infrastructure.integration;

import javax.validation.constraints.Min;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "spring-integration")
public class IntegrationBasisProperties {

    @Min(100)
    private int backOffPeriod = 10_000;

    @Min(0)
    private int maxAttempts = 10;

    public int getBackOffPeriod() {
        return backOffPeriod;
    }

    public void setBackOffPeriod(int backOffPeriod) {
        this.backOffPeriod = backOffPeriod;
    }

    public int getMaxAttempts() {
        return maxAttempts;
    }

    public void setMaxAttempts(int maxAttempts) {
        this.maxAttempts = maxAttempts;
    }
}
