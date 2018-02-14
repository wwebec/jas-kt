package ch.admin.seco.jobs.services.jobadservice.application.dto;

import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.ApplyChannel;

public class ApplyChannelDto {

    private String mailAddress;
    private String emailAddress;
    private String phoneNumber;
    private String applicationUrl;
    private String additionalInfo;

    protected ApplyChannelDto() {
        // For reflection libs
    }

    public ApplyChannelDto(String mailAddress, String emailAddress, String phoneNumber, String applicationUrl, String additionalInfo) {
        this.mailAddress = mailAddress;
        this.emailAddress = emailAddress;
        this.phoneNumber = phoneNumber;
        this.applicationUrl = applicationUrl;
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

    public String getApplicationUrl() {
        return applicationUrl;
    }

    public void setApplicationUrl(String applicationUrl) {
        this.applicationUrl = applicationUrl;
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
        applyChannelDto.setApplicationUrl(applyChannel.getApplicationUrl());
        applyChannelDto.setAdditionalInfo(applyChannel.getAdditionalInfo());
        return applyChannelDto;
    }
}
