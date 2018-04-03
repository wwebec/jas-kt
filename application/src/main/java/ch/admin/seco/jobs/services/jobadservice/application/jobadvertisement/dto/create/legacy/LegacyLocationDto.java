package ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.create.legacy;

import javax.validation.constraints.NotNull;

public class LegacyLocationDto {

    @NotNull
    private String postalCode;
    @NotNull
    private String locality;
    @NotNull
    private String countryCode;
    private String additionalDetails;

    protected LegacyLocationDto() {
        // For reflection libs
    }

    public LegacyLocationDto(String postalCode, String locality, String countryCode, String additionalDetails) {
        this.countryCode = countryCode;
        this.postalCode = postalCode;
        this.locality = locality;
        this.additionalDetails = additionalDetails;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getAdditionalDetails() {
        return additionalDetails;
    }

    public void setAdditionalDetails(String additionalDetails) {
        this.additionalDetails = additionalDetails;
    }
}
