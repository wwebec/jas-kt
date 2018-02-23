package ch.admin.seco.jobs.services.jobadservice.infrastructure.messagebroker.jms;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@ConfigurationProperties(prefix = "jobroom.jms")
@Validated
public class JmsProperties {

    @NotBlank
    private String hostname;

    @NotBlank
    @Max(65535)
    private Integer port;

    @NotBlank
    private String username;

    @NotBlank
    private String password;

    private String trustStorePath;

    private String trustStorePassword;

    private String keyStorePath;

    private String keyStorePassword;

    private int heartbeat = 10;

    private int connectionDelay = 100;

    @Min(1)
    private int errorCapturingLookbackMinutes = 15;

    @NotBlank
    private String inputQueueName;

    @NotBlank
    private String outputQueueName;

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
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

    public String getTrustStorePath() {
        return trustStorePath;
    }

    public void setTrustStorePath(String trustStorePath) {
        this.trustStorePath = trustStorePath;
    }

    public String getTrustStorePassword() {
        return trustStorePassword;
    }

    public void setTrustStorePassword(String trustStorePassword) {
        this.trustStorePassword = trustStorePassword;
    }

    public String getKeyStorePath() {
        return keyStorePath;
    }

    public void setKeyStorePath(String keyStorePath) {
        this.keyStorePath = keyStorePath;
    }

    public String getKeyStorePassword() {
        return keyStorePassword;
    }

    public void setKeyStorePassword(String keyStorePassword) {
        this.keyStorePassword = keyStorePassword;
    }

    public int getHeartbeat() {
        return heartbeat;
    }

    public void setHeartbeat(int heartbeat) {
        this.heartbeat = heartbeat;
    }

    public int getConnectionDelay() {
        return connectionDelay;
    }

    public void setConnectionDelay(int connectionDelay) {
        this.connectionDelay = connectionDelay;
    }

    public int getErrorCapturingLookbackMinutes() {
        return errorCapturingLookbackMinutes;
    }

    public void setErrorCapturingLookbackMinutes(int errorCapturingLookbackMinutes) {
        this.errorCapturingLookbackMinutes = errorCapturingLookbackMinutes;
    }

    public String getInputQueueName() {
        return inputQueueName;
    }

    public void setInputQueueName(String inputQueueName) {
        this.inputQueueName = inputQueueName;
    }

    public String getOutputQueueName() {
        return outputQueueName;
    }

    public void setOutputQueueName(String outputQueueName) {
        this.outputQueueName = outputQueueName;
    }
}
