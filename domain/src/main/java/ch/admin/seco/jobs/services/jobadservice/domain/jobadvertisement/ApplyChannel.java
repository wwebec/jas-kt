package ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement;

import ch.admin.seco.jobs.services.jobadservice.core.domain.ValueObject;
import org.apache.commons.lang3.builder.EqualsBuilder;

import java.util.Objects;

public class ApplyChannel implements ValueObject<ApplyChannel> {

    private String mailAddress;
    private String emailAddress;
    private String phoneNumber;
    private String onlineUrl;
    private String additionalInfo;

    protected ApplyChannel() {
    }

    public ApplyChannel(String mailAddress, String emailAddress, String phoneNumber, String onlineUrl, String additionalInfo) {
        this.mailAddress = mailAddress;
        this.emailAddress = emailAddress;
        this.phoneNumber = phoneNumber;
        this.onlineUrl = onlineUrl;
        this.additionalInfo = additionalInfo;
    }

    @Override
    public boolean isSameValueAs(ApplyChannel other) {
        return (other != null) && new EqualsBuilder()
                .append(this.mailAddress, other.mailAddress)
                .append(this.emailAddress, other.emailAddress)
                .append(this.phoneNumber, other.phoneNumber)
                .append(this.onlineUrl, other.onlineUrl)
                .append(this.additionalInfo, other.additionalInfo)
                .build();
    }

    public String getMailAddress() {
        return mailAddress;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getOnlineUrl() {
        return onlineUrl;
    }

    public String getAdditionalInfo() {
        return additionalInfo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ApplyChannel that = (ApplyChannel) o;
        return isSameValueAs(that);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mailAddress, emailAddress, phoneNumber, onlineUrl, additionalInfo);
    }
}
