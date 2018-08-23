package ch.admin.seco.jobs.services.jobadservice.infrastructure.mail;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "mail.sender")
public class MailSenderProperties {

    @NotEmpty
    private String fromAddress;

    @NotEmpty
    private String[] bccAddress;

    private String baseUrl;

    private String linkToJobAdDetailPage;

    @NotNull
    private String templatesPath;

    @Min(0)
    private int mailQueueThreshold = 0;

    public String getFromAddress() {
        return fromAddress;
    }

    public String[] getBccAddress() {
        return bccAddress;
    }

    public void setBccAddress(String[] bccAddress) {
        this.bccAddress = bccAddress;
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

    public String getLinkToJobAdDetailPage() {
        return linkToJobAdDetailPage;
    }

    public void setLinkToJobAdDetailPage(String linkToJobAdDetailPage) {
        this.linkToJobAdDetailPage = linkToJobAdDetailPage;
    }

    public String getTemplatesPath() {
        return templatesPath;
    }

    public void setTemplatesPath(String templatesPath) {
        this.templatesPath = templatesPath;
    }

    public int getMailQueueThreshold() {
        return mailQueueThreshold;
    }

    public void setMailQueueThreshold(int mailQueueThreshold) {
        this.mailQueueThreshold = mailQueueThreshold;
    }
}
