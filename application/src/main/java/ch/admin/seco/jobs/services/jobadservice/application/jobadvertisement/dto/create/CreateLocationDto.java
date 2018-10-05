package ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.create;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.utils.CountryIsoCode;

public class CreateLocationDto {

    @Size(max = 50)
    private String remarks;

    @Size(max = 50)
    private String city;

    private String postalCode;

    @NotBlank
    @CountryIsoCode
    private String countryIsoCode;

    public String getRemarks() {
        return remarks;
    }

    public CreateLocationDto setRemarks(String remarks) {
        this.remarks = remarks;
        return this;
    }

    public String getCity() {
        return city;
    }

    public CreateLocationDto setCity(String city) {
        this.city = city;
        return this;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public CreateLocationDto setPostalCode(String postalCode) {
        this.postalCode = postalCode;
        return this;
    }

    public String getCountryIsoCode() {
        return countryIsoCode;
    }

    public CreateLocationDto setCountryIsoCode(String countryIsoCode) {
        this.countryIsoCode = countryIsoCode;
        return this;
    }
}
