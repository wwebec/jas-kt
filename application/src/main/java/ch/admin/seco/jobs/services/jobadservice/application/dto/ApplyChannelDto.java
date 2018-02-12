package ch.admin.seco.jobs.services.jobadservice.application.dto;

import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.ApplyChannel;

public class ApplyChannelDto {

    private String mailAddress;
    private String emailAddress;
    private String phoneNumber;
    private String onlineUrl;
    private String additionalInfo;

    protected ApplyChannelDto() {
        // For reflection libs
    }

    public ApplyChannelDto(String mailAddress, String emailAddress, String phoneNumber, String onlineUrl, String additionalInfo) {
        this.mailAddress = mailAddress;
        this.emailAddress = emailAddress;
        this.phoneNumber = phoneNumber;
        this.onlineUrl = onlineUrl;
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

    public String getOnlineUrl() {
        return onlineUrl;
    }

    public void setOnlineUrl(String onlineUrl) {
        this.onlineUrl = onlineUrl;
    }

    public String getAdditionalInfo() {
        return additionalInfo;
    }

    public void setAdditionalInfo(String additionalInfo) {
        this.additionalInfo = additionalInfo;
    }

    public static ApplyChannelDto toDto(ApplyChannel applyChannel) {
        ApplyChannelDto applyChannelDto = new ApplyChannelDto();
        applyChannelDto.setMailAddress(applyChannel.getMailAddress());
        applyChannelDto.setEmailAddress(applyChannel.getEmailAddress());
        applyChannelDto.setPhoneNumber(applyChannel.getPhoneNumber());
        applyChannelDto.setOnlineUrl(applyChannel.getOnlineUrl());
        applyChannelDto.setAdditionalInfo(applyChannel.getAdditionalInfo());
        return applyChannelDto;
    }
}
