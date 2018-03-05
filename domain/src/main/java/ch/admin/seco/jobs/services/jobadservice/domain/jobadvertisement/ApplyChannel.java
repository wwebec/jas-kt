package ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement;

import ch.admin.seco.jobs.services.jobadservice.core.domain.ValueObject;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
@Access(AccessType.FIELD)
public class ApplyChannel implements ValueObject<ApplyChannel> {

    private String mailAddress;
    private String emailAddress;
    private String phoneNumber;
    private String formUrl;
    private String additionalInfo;

    protected ApplyChannel() {
        // For reflection libs
    }

    public ApplyChannel(String mailAddress, String emailAddress, String phoneNumber, String formUrl, String additionalInfo) {
        this.mailAddress = mailAddress;
        this.emailAddress = emailAddress;
        this.phoneNumber = phoneNumber;
        this.formUrl = formUrl;
        this.additionalInfo = additionalInfo;
    }

    @Override
    public boolean sameValueObjectAs(ApplyChannel other) {
        return equals(other);
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

    public String getFormUrl() {
        return formUrl;
    }

    public String getAdditionalInfo() {
        return additionalInfo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ApplyChannel that = (ApplyChannel) o;
        return Objects.equals(mailAddress, that.mailAddress) &&
                Objects.equals(emailAddress, that.emailAddress) &&
                Objects.equals(phoneNumber, that.phoneNumber) &&
                Objects.equals(formUrl, that.formUrl) &&
                Objects.equals(additionalInfo, that.additionalInfo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mailAddress, emailAddress, phoneNumber, formUrl, additionalInfo);
    }

    @Override
    public String toString() {
        return "ApplyChannel{" +
                "mailAddress='" + mailAddress + '\'' +
                ", emailAddress='" + emailAddress + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", formUrl='" + formUrl + '\'' +
                ", additionalInfo='" + additionalInfo + '\'' +
                '}';
    }
}
