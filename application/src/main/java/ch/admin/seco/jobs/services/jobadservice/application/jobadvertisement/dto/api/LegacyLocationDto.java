package ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.api;

import javax.validation.constraints.NotNull;

public class LegacyLocationDto {

    @NotNull
    private String countryCode;
    @NotNull
    private String zipCode;
    private String communalCode;
    @NotNull
    private String city;
    private String additionalDetails;

    protected LegacyLocationDto() {
        // For reflection libs
    }

    public LegacyLocationDto(String countryCode, String zipCode, String communalCode, String city, String additionalDetails) {
        this.countryCode = countryCode;
        this.zipCode = zipCode;
        this.communalCode = communalCode;
        this.city = city;
        this.additionalDetails = additionalDetails;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getCommunalCode() {
        return communalCode;
    }

    public void setCommunalCode(String communalCode) {
        this.communalCode = communalCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAdditionalDetails() {
        return additionalDetails;
    }

    public void setAdditionalDetails(String additionalDetails) {
        this.additionalDetails = additionalDetails;
    }
}
