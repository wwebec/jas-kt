package ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto;

import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.Employer;

import javax.validation.constraints.NotBlank;

public class EmployerDto {

    @NotBlank
    private String name;
    private String postalCode;
    private String city;
    private String countryIsoCode;

    protected EmployerDto() {
        // For reflection libs
    }

    public EmployerDto(String name, String postalCode, String city, String countryIsoCode) {
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

    public static EmployerDto toDto(Employer employer) {
        if (employer == null) {
            return null;
        }
        EmployerDto employerDto = new EmployerDto();
        employerDto.setName(employer.getName());
        employerDto.setPostalCode(employer.getPostalCode());
        employerDto.setCity(employer.getCity());
        employerDto.setCountryIsoCode(employer.getCountryIsoCode());
        return employerDto;
    }
}
