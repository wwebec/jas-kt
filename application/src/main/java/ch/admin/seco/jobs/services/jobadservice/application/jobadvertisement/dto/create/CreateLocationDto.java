package ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.create;

import javax.validation.constraints.NotBlank;

import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.utils.CountryIsoCode;

public class CreateLocationDto {

    private String remarks;

    private String city;

    private String postalCode;

    @NotBlank
    @CountryIsoCode
    private String countryIsoCode;

    protected CreateLocationDto() {
        // For reflection libs
    }

    public CreateLocationDto(String remarks, String city, String postalCode, String countryIsoCode) {
        this.remarks = remarks;
        this.city = city;
        this.postalCode = postalCode;
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

    public String getCountryIsoCode() {
        return countryIsoCode;
    }

    public void setCountryIsoCode(String countryIsoCode) {
        this.countryIsoCode = countryIsoCode;
    }

}
