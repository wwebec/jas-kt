package ch.admin.seco.jobs.services.jobadservice.infrastructure.mail;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.Resource;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Properties;

@Validated
@ConfigurationProperties(prefix = "mail.sender")
public class MailSenderProperties {

    @NotEmpty
    private String host;

    @Min(0)
    @Max(65535)
    private int port;

    private String username;

    private String password;

    @NotEmpty
    private String fromAddress;

    private String baseUrl;

    @NotNull
    private Resource templatesResource;

    public JavaMailSenderImpl getJavaMailSender() {
        final JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
        javaMailSender.setHost(this.getHost());
        javaMailSender.setPort(this.getPort());
        javaMailSender.setUsername(this.getUsername());
        javaMailSender.setPassword(this.getPassword());
        final Properties javaMailProperties = new Properties();
        javaMailProperties.setProperty("mail.smtp.auth", String.valueOf(Boolean.TRUE));
        javaMailProperties.setProperty("mail.smtp.starttls.enable", String.valueOf(Boolean.FALSE));
        javaMailSender.setJavaMailProperties(javaMailProperties);
        return javaMailSender;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
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

    public String getFromAddress() {
        return fromAddress;
    }

    public void setFromAddress(String fromAddress) {
        this.fromAddress = fromAddress;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public Resource getTemplatesResource() {
        return templatesResource;
    }

    public void setTemplatesResource(Resource templatesResource) {
        this.templatesResource = templatesResource;
    }
}
