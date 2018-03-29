package ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.create.legacy;

import javax.validation.constraints.NotNull;

public class LegacyApplicationDto {

    @NotNull
    private boolean written;
    @NotNull
    private boolean electronic;
    private String email;
    private String url;
    @NotNull
    private boolean phoneEnabled;
    private String phoneNumber;
    private String additionalInfo;

    protected LegacyApplicationDto() {
        // For reflection libs
    }

    public LegacyApplicationDto(boolean written, boolean electronic, String email, String url, boolean phoneEnabled, String phoneNumber, String additionalInfo) {
        this.written = written;
        this.electronic = electronic;
        this.email = email;
        this.url = url;
        this.phoneEnabled = phoneEnabled;
        this.phoneNumber = phoneNumber;
        this.additionalInfo = additionalInfo;
    }

    public boolean isWritten() {
        return written;
    }

    public void setWritten(boolean written) {
        this.written = written;
    }

    public boolean isElectronic() {
        return electronic;
    }

    public void setElectronic(boolean electronic) {
        this.electronic = electronic;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isPhoneEnabled() {
        return phoneEnabled;
    }

    public void setPhoneEnabled(boolean phoneEnabled) {
        this.phoneEnabled = phoneEnabled;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAdditionalInfo() {
        return additionalInfo;
    }

    public void setAdditionalInfo(String additionalInfo) {
        this.additionalInfo = additionalInfo;
    }

}
