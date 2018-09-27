package ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto;

import javax.validation.constraints.NotBlank;

import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.Employer;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.utils.CountryIsoCode;

public class EmployerDto {

    @NotBlank
    private String name;

    private String postalCode;

    private String city;

    @NotBlank
    @CountryIsoCode
    private String countryIsoCode;

    public String getName() {
        return name;
    }

    public EmployerDto setName(String name) {
        this.name = name;
        return this;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public EmployerDto setPostalCode(String postalCode) {
        this.postalCode = postalCode;
        return this;
    }

    public String getCity() {
        return city;
    }

    public EmployerDto setCity(String city) {
        this.city = city;
        return this;
    }

    public String getCountryIsoCode() {
        return countryIsoCode;
    }

    public EmployerDto setCountryIsoCode(String countryIsoCode) {
        this.countryIsoCode = countryIsoCode;
        return this;
    }

    public static EmployerDto toDto(Employer employer) {
        if (employer == null) {
            return null;
        }
        return new EmployerDto()
        .setName(employer.getName())
        .setPostalCode(employer.getPostalCode())
        .setCity(employer.getCity())
        .setCountryIsoCode(employer.getCountryIsoCode());
    }
}
