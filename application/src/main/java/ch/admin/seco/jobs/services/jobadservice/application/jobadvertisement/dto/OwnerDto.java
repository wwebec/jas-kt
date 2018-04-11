package ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto;

import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.Owner;

public class OwnerDto {

    private String userId;

    private String companyId;

    private String accessToken;

    protected OwnerDto() {
        // For reflection libs
    }

    public OwnerDto(String userId, String companyId, String accessToken) {
        this.userId = userId;
        this.companyId = companyId;
        this.accessToken = accessToken;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public static OwnerDto toDto(Owner owner) {
        if (owner == null) {
            return null;
        }
        OwnerDto ownerDto = new OwnerDto();
        ownerDto.setUserId(owner.getUserId());
        ownerDto.setCompanyId(owner.getCompanyId());
        ownerDto.setAccessToken(owner.getAccessToken());
        return ownerDto;
    }
}
