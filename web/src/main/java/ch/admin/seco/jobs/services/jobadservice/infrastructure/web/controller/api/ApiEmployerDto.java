package ch.admin.seco.jobs.services.jobadservice.infrastructure.web.controller.api;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.Employer;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.utils.CountryIsoCode;

public class ApiEmployerDto {

    @NotBlank
    @Size(max = 255)
    private String name;

    @NotBlank
    @Size(max = 10)
    private String postalCode;

    @NotBlank
    @Size(max = 100)
    private String city;

    @NotBlank
    @CountryIsoCode
    private String countryIsoCode;

    protected ApiEmployerDto() {
        // For reflection libs
    }

    public ApiEmployerDto(String name, String postalCode, String city, String countryIsoCode) {
        this.name = name;
        this.postalCode = postalCode;
        this.city = city;
        this.countryIsoCode = countryIsoCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountryIsoCode() {
        return countryIsoCode;
    }

    public void setCountryIsoCode(String countryIsoCode) {
        this.countryIsoCode = countryIsoCode;
    }

    public static ApiEmployerDto toDto(Employer employer) {
        if (employer == null) {
            return null;
        }
        ApiEmployerDto employerDto = new ApiEmployerDto();
        employerDto.setName(employer.getName());
        employerDto.setPostalCode(employer.getPostalCode());
        employerDto.setCity(employer.getCity());
        employerDto.setCountryIsoCode(employer.getCountryIsoCode());
        return employerDto;
    }
}
