package ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.api;

import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.Employer;

import javax.validation.constraints.NotBlank;

public class ApiEmployerDto {

    @NotBlank
    private String name;
    private String postalCode;
    private String city;
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
