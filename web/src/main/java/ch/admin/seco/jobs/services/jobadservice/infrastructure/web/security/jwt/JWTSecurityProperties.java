package ch.admin.seco.jobs.services.jobadservice.infrastructure.web.security.jwt;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "security.jwt", ignoreUnknownFields = false, ignoreInvalidFields = true)
public class JWTSecurityProperties {

    private String secret;

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }
}
