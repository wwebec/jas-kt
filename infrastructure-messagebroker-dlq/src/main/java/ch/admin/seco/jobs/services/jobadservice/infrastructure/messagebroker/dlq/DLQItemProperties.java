package ch.admin.seco.jobs.services.jobadservice.infrastructure.messagebroker.dlq;

import java.util.List;

import javax.validation.constraints.NotEmpty;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@ConfigurationProperties("jobad.dlq")
@Validated
public class DLQItemProperties {

    @NotEmpty
    private List<String> receivers;

    public List<String> getReceivers() {
        return receivers;
    }

    public DLQItemProperties setReceivers(List<String> receivers) {
        this.receivers = receivers;
        return this;
    }
}
