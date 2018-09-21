package ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.x28;

import javax.validation.constraints.Size;

import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.create.CreateLocationDto;

public class X28LocationDto {

    @Size(max = 255)
    private String remarks;

    @Size(max = 50)
    private String city;

    @Size(max = 10)
    private String postalCode;

    @Size(max = 2)
    private String countryIsoCode;

    protected X28LocationDto() {
        // For reflection libs
    }

    public X28LocationDto(String remarks, String city, String postalCode, String countryIsoCode) {
        this.remarks = remarks;
        this.city = city;
        this.postalCode = postalCode;
        this.countryIsoCode = countryIsoCode;
    }

    CreateLocationDto toCreateLocationDto() {
        return new CreateLocationDto(remarks, city, postalCode, countryIsoCode);
    }

    public String getRemarks() {
        return remarks;
    }

    public X28LocationDto setRemarks(String remarks) {
        this.remarks = remarks;
        return this;
    }

    public String getCity() {
        return city;
    }

    public X28LocationDto setCity(String city) {
        this.city = city;
        return this;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public X28LocationDto setPostalCode(String postalCode) {
        this.postalCode = postalCode;
        return this;
    }

    public String getCountryIsoCode() {
        return countryIsoCode;
    }

    public X28LocationDto setCountryIsoCode(String countryIsoCode) {
        this.countryIsoCode = countryIsoCode;
        return this;
    }

}
