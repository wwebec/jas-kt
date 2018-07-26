package ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.api;

import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.ApplyChannel;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class ApiApplyChannelDto {

    @Size(max=255)
    private String mailAddress;

    @Size(max=50)
    private String emailAddress;

    @Size(max=20)
    @Pattern(regexp = "[+][0-9]{11,}")
    private String phoneNumber;

    @Size(max=255)
    private String formUrl;

    @Size(max=255)
    private String additionalInfo;

    protected ApiApplyChannelDto() {
        // For reflection libs
    }

    public ApiApplyChannelDto(String mailAddress, String emailAddress, String phoneNumber, String formUrl, String additionalInfo) {
        this.mailAddress = mailAddress;
        this.emailAddress = emailAddress;
        this.phoneNumber = phoneNumber;
        this.formUrl = formUrl;
        this.additionalInfo = additionalInfo;
    }

    public String getMailAddress() {
        return mailAddress;
    }

    public void setMailAddress(String mailAddress) {
        this.mailAddress = mailAddress;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getFormUrl() {
        return formUrl;
    }

    public void setFormUrl(String formUrl) {
        this.formUrl = formUrl;
    }

    public String getAdditionalInfo() {
        return additionalInfo;
    }

    public void setAdditionalInfo(String additionalInfo) {
        this.additionalInfo = additionalInfo;
    }

    public static ApiApplyChannelDto toDto(ApplyChannel applyChannel) {
        if (applyChannel == null) {
            return null;
        }
        ApiApplyChannelDto applyChannelDto = new ApiApplyChannelDto();
        applyChannelDto.setMailAddress(applyChannel.getMailAddress());
        applyChannelDto.setEmailAddress(applyChannel.getEmailAddress());
        applyChannelDto.setPhoneNumber(applyChannel.getPhoneNumber());
        applyChannelDto.setFormUrl(applyChannel.getFormUrl());
        applyChannelDto.setAdditionalInfo(applyChannel.getAdditionalInfo());
        return applyChannelDto;
    }
}