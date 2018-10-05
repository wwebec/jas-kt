package ch.admin.seco.jobs.services.jobadservice.infrastructure.web.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "jobadservice.security")
public class JobAdServiceSecurityProperties {

    private int apiUserMaxLoginAttempts = 5;

    public int getApiUserMaxLoginAttempts() {
        return apiUserMaxLoginAttempts;
    }

    public void setApiUserMaxLoginAttempts(int apiUserMaxLoginAttempts) {
        this.apiUserMaxLoginAttempts = apiUserMaxLoginAttempts;
    }
}
