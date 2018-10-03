package ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement;

import static org.springframework.util.StringUtils.hasText;

import java.util.Objects;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Embeddable;

import ch.admin.seco.jobs.services.jobadservice.core.domain.ValueObject;
import ch.admin.seco.jobs.services.jobadservice.domain.jobcenter.JobCenter;
import ch.admin.seco.jobs.services.jobadservice.domain.jobcenter.JobCenterAddress;

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

    private ApplyChannel(Builder builder) {
        this.mailAddress = builder.mailAddress;
        this.emailAddress = builder.emailAddress;
        this.phoneNumber = builder.phoneNumber;
        this.formUrl = builder.formUrl;
        this.additionalInfo = builder.additionalInfo;
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
        if (this == o) { return true; }
        if (o == null || getClass() != o.getClass()) { return false; }
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

    public static final class Builder {
        private String mailAddress;
        private String emailAddress;
        private String phoneNumber;
        private String formUrl;
        private String additionalInfo;

        public Builder() {
        }

        public Builder(JobCenter jobCenter) {
            JobCenterAddress jobCenterAddress = jobCenter.getAddress();
            this.mailAddress = createApplyMailAddress(jobCenterAddress);
            this.formUrl = null;
            this.additionalInfo = null;
            this.phoneNumber = null;
            this.emailAddress = null;
            if (jobCenter.isShowContactDetailsToPublic()) {
                this.phoneNumber = jobCenter.getPhone();
                this.emailAddress = jobCenter.getEmail();
            }
        }

        public ApplyChannel build() {
            return new ApplyChannel(this);
        }

        public Builder setMailAddress(String mailAddress) {
            this.mailAddress = mailAddress;
            return this;
        }

        public Builder setEmailAddress(String emailAddress) {
            this.emailAddress = emailAddress;
            return this;
        }

        public Builder setPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
            return this;
        }

        public Builder setFormUrl(String formUrl) {
            this.formUrl = formUrl;
            return this;
        }

        public Builder setAdditionalInfo(String additionalInfo) {
            this.additionalInfo = additionalInfo;
            return this;
        }

        private String createApplyMailAddress(JobCenterAddress jobCenterAddress) {
            StringBuilder sb = new StringBuilder();
            sb.append(jobCenterAddress.getName());
            if (hasText(jobCenterAddress.getStreet())) {
                sb.append(", ");
                sb.append(jobCenterAddress.getStreet());
                sb.append(' ');
                sb.append(jobCenterAddress.getHouseNumber());
            }
            if (hasText(jobCenterAddress.getZipCode())) {
                sb.append(", ");
                sb.append(jobCenterAddress.getZipCode());
                sb.append(' ');
                sb.append(jobCenterAddress.getCity());
            }
            return sb.toString();
        }
    }
}
