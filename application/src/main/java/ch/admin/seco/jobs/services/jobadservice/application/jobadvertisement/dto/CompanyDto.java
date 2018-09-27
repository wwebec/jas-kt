package ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto;

import javax.validation.constraints.NotBlank;

import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.Company;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.utils.CountryIsoCode;

public class CompanyDto {

    @NotBlank
    private String name;

    private String street;

    private String houseNumber;

    @NotBlank
    private String postalCode;

    @NotBlank
    private String city;

    @NotBlank
    @CountryIsoCode
    private String countryIsoCode;

    private String postOfficeBoxNumber;

    private String postOfficeBoxPostalCode;

    private String postOfficeBoxCity;

    private String phone;

    private String email;

    private String website;

    private boolean surrogate;

    public String getName() {
        return name;
    }

    public CompanyDto setName(String name) {
        this.name = name;
        return this;
    }

    public String getStreet() {
        return street;
    }

    public CompanyDto setStreet(String street) {
        this.street = street;
        return this;
    }

    public String getHouseNumber() {
        return houseNumber;
    }

    public CompanyDto setHouseNumber(String houseNumber) {
        this.houseNumber = houseNumber;
        return this;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public CompanyDto setPostalCode(String postalCode) {
        this.postalCode = postalCode;
        return this;
    }

    public String getCity() {
        return city;
    }

    public CompanyDto setCity(String city) {
        this.city = city;
        return this;
    }

    public String getCountryIsoCode() {
        return countryIsoCode;
    }

    public CompanyDto setCountryIsoCode(String countryIsoCode) {
        this.countryIsoCode = countryIsoCode;
        return this;
    }

    public String getPostOfficeBoxNumber() {
        return postOfficeBoxNumber;
    }

    public CompanyDto setPostOfficeBoxNumber(String postOfficeBoxNumber) {
        this.postOfficeBoxNumber = postOfficeBoxNumber;
        return this;
    }

    public String getPostOfficeBoxPostalCode() {
        return postOfficeBoxPostalCode;
    }

    public CompanyDto setPostOfficeBoxPostalCode(String postOfficeBoxPostalCode) {
        this.postOfficeBoxPostalCode = postOfficeBoxPostalCode;
        return this;
    }

    public String getPostOfficeBoxCity() {
        return postOfficeBoxCity;
    }

    public CompanyDto setPostOfficeBoxCity(String postOfficeBoxCity) {
        this.postOfficeBoxCity = postOfficeBoxCity;
        return this;
    }

    public String getPhone() {
        return phone;
    }

    public CompanyDto setPhone(String phone) {
        this.phone = phone;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public CompanyDto setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getWebsite() {
        return website;
    }

    public CompanyDto setWebsite(String website) {
        this.website = website;
        return this;
    }

    public boolean isSurrogate() {
        return surrogate;
    }

    public CompanyDto setSurrogate(boolean surrogate) {
        this.surrogate = surrogate;
        return this;
    }

    public static CompanyDto toDto(Company displayCompany) {
        if(displayCompany == null) {
            return null;
        }
        CompanyDto companyDto = new CompanyDto();
        companyDto.setName(displayCompany.getName());
        companyDto.setStreet(displayCompany.getStreet());
        companyDto.setHouseNumber(displayCompany.getHouseNumber());
        companyDto.setPostalCode(displayCompany.getPostalCode());
        companyDto.setCity(displayCompany.getCity());
        companyDto.setCountryIsoCode(displayCompany.getCountryIsoCode());
        companyDto.setPostOfficeBoxNumber(displayCompany.getPostOfficeBoxNumber());
        companyDto.setPostOfficeBoxPostalCode(displayCompany.getPostOfficeBoxPostalCode());
        companyDto.setPostOfficeBoxCity(displayCompany.getPostOfficeBoxCity());
        companyDto.setPhone(displayCompany.getPhone());
        companyDto.setEmail(displayCompany.getEmail());
        companyDto.setWebsite(displayCompany.getWebsite());
        companyDto.setSurrogate(displayCompany.isSurrogate());
        return companyDto;
    }
}
