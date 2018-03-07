package ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.api;

import javax.validation.constraints.NotEmpty;

public class LocationApiDto {

    private String remarks;
    private String city;
    @NotEmpty
    private String postalCode;
    private String cantonCode;
    private String countryIsoCode;

    protected LocationApiDto() {
        // For reflection libs
    }

    public LocationApiDto(String remarks, String city, String postalCode, String cantonCode, String countryIsoCode) {
        this.remarks = remarks;
        this.city = city;
        this.postalCode = postalCode;
        this.cantonCode = cantonCode;
        this.countryIsoCode = countryIsoCode;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCantonCode() {
        return cantonCode;
    }

    public void setCantonCode(String cantonCode) {
        this.cantonCode = cantonCode;
    }

    public String getCountryIsoCode() {
        return countryIsoCode;
    }

    public void setCountryIsoCode(String countryIsoCode) {
        this.countryIsoCode = countryIsoCode;
    }
}
