package ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto;

import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.Owner;

public class OwnerDto {

    private String userId;

    private String avgId;

    private String accessToken;

    protected OwnerDto() {
        // For reflection libs
    }

    public OwnerDto(String userId, String avgId, String accessToken) {
        this.userId = userId;
        this.avgId = avgId;
        this.accessToken = accessToken;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAvgId() {
        return avgId;
    }

    public void setAvgId(String avgId) {
        this.avgId = avgId;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public static OwnerDto toDto(Owner owner) {
        OwnerDto ownerDto = new OwnerDto();
        ownerDto.setUserId(owner.getUserId());
        ownerDto.setAvgId(owner.getAvgId());
        ownerDto.setAccessToken(owner.getAccessToken());
        return ownerDto;
    }
}
