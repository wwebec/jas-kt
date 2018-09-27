package ch.admin.seco.jobs.services.jobadservice.infrastructure.web.controller.api;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.utils.CountryIsoCode;

public class ApiCreateLocationDto {

    @Size(max=50)
    private String remarks;

    @NotBlank
    @Size(max=50)
    private String city;

    @NotBlank
    @Size(max=10)
    private String postalCode;

    @NotBlank
    @CountryIsoCode
    private String countryIsoCode;

    protected ApiCreateLocationDto() {
        // For reflection libs
    }

    public ApiCreateLocationDto(String remarks, String city, String postalCode, String countryIsoCode) {
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
