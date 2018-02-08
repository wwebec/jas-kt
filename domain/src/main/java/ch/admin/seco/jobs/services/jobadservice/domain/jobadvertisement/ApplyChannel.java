package ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement;

public class ApplyChannel {

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
}
