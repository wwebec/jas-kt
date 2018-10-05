package ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto;

import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.ApplyChannel;

public class ApplyChannelDto {

    private String mailAddress;

    private String emailAddress;

    private String phoneNumber;

    private String formUrl;

    private String additionalInfo;

    public String getMailAddress() {
        return mailAddress;
    }

    public ApplyChannelDto setMailAddress(String mailAddress) {
        this.mailAddress = mailAddress;
        return this;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public ApplyChannelDto setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
        return this;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public ApplyChannelDto setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public String getFormUrl() {
        return formUrl;
    }

    public ApplyChannelDto setFormUrl(String formUrl) {
        this.formUrl = formUrl;
        return this;
    }

    public String getAdditionalInfo() {
        return additionalInfo;
    }

    public ApplyChannelDto setAdditionalInfo(String additionalInfo) {
        this.additionalInfo = additionalInfo;
        return this;
    }

    public static ApplyChannelDto toDto(ApplyChannel displayApplyChannel) {
        if (displayApplyChannel == null) {
            return null;
        }
        ApplyChannelDto applyChannelDto = new ApplyChannelDto();
        applyChannelDto.setMailAddress(displayApplyChannel.getMailAddress());
        applyChannelDto.setEmailAddress(displayApplyChannel.getEmailAddress());
        applyChannelDto.setPhoneNumber(displayApplyChannel.getPhoneNumber());
        applyChannelDto.setFormUrl(displayApplyChannel.getFormUrl());
        applyChannelDto.setAdditionalInfo(displayApplyChannel.getAdditionalInfo());
        return applyChannelDto;
    }
}
