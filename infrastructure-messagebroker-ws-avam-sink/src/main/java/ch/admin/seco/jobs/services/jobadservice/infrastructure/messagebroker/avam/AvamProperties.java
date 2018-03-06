package ch.admin.seco.jobs.services.jobadservice.infrastructure.messagebroker.avam;

import javax.validation.constraints.NotBlank;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@ConfigurationProperties(prefix = "jobroom.ws.avam")
@Validated
public class AvamProperties {

    @NotBlank
    private String endPointUrl;

    @NotBlank
    private String username;

    @NotBlank
    private String password;

    public String getEndPointUrl() {
        return endPointUrl;
    }

    public void setEndPointUrl(String endPointUrl) {
        this.endPointUrl = endPointUrl;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
