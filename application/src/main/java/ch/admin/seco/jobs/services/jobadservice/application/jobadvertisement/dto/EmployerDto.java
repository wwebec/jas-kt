package ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto;

import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.Company;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.Employer;

import javax.validation.constraints.NotNull;

public class EmployerDto {

    private String name;
    private String street;
    private String houseNumber;
    private String postalCode;
    private String city;
    private String countryIsoCode;

    protected EmployerDto() {
        // For reflection libs
    }

    public EmployerDto(String name, String street, String houseNumber, String postalCode, String city, String countryIsoCode) {
        this.name = name;
        this.street = street;
        this.houseNumber = houseNumber;
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

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getHouseNumber() {
        return houseNumber;
    }

    public void setHouseNumber(String houseNumber) {
        this.houseNumber = houseNumber;
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
        EmployerDto employerDto = new EmployerDto();
        employerDto.setName(employer.getName());
        employerDto.setStreet(employer.getStreet());
        employerDto.setHouseNumber(employer.getHouseNumber());
        employerDto.setPostalCode(employer.getPostalCode());
        employerDto.setCity(employer.getCity());
        employerDto.setCountryIsoCode(employer.getCountryIsoCode());
        return employerDto;
    }
}
