package ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto;

import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.Company;

import javax.validation.constraints.NotNull;

public class CompanyDto {

    @NotNull
    private String name;
    @NotNull
    private String street;
    private String houseNumber;
    @NotNull
    private String zipCode;
    private String city;
    private String countryIsoCode;
    private String postOfficeBoxNumber;
    private String postOfficeBoxZipCode;
    private String postOfficeBoxCity;
    private String phone;
    private String email;
    private String website;

    protected CompanyDto() {
        // For reflection libs
    }

    public CompanyDto(String name, String street, String houseNumber, String zipCode, String city, String countryIsoCode, String postOfficeBoxNumber, String postOfficeBoxZipCode, String postOfficeBoxCity, String phone, String email, String website) {
        this.name = name;
        this.street = street;
        this.houseNumber = houseNumber;
        this.zipCode = zipCode;
        this.city = city;
        this.countryIsoCode = countryIsoCode;
        this.postOfficeBoxNumber = postOfficeBoxNumber;
        this.postOfficeBoxZipCode = postOfficeBoxZipCode;
        this.postOfficeBoxCity = postOfficeBoxCity;
        this.phone = phone;
        this.email = email;
        this.website = website;
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

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
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

    public String getPostOfficeBoxNumber() {
        return postOfficeBoxNumber;
    }

    public void setPostOfficeBoxNumber(String postOfficeBoxNumber) {
        this.postOfficeBoxNumber = postOfficeBoxNumber;
    }

    public String getPostOfficeBoxZipCode() {
        return postOfficeBoxZipCode;
    }

    public void setPostOfficeBoxZipCode(String postOfficeBoxZipCode) {
        this.postOfficeBoxZipCode = postOfficeBoxZipCode;
    }

    public String getPostOfficeBoxCity() {
        return postOfficeBoxCity;
    }

    public void setPostOfficeBoxCity(String postOfficeBoxCity) {
        this.postOfficeBoxCity = postOfficeBoxCity;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public static CompanyDto toDto(Company company) {
        CompanyDto companyDto = new CompanyDto();
        companyDto.setName(company.getName());
        companyDto.setStreet(company.getStreet());
        companyDto.setHouseNumber(company.getHouseNumber());
        companyDto.setZipCode(company.getZipCode());
        companyDto.setCity(company.getCity());
        companyDto.setCountryIsoCode(company.getCountryIsoCode());
        companyDto.setPostOfficeBoxNumber(company.getPostOfficeBoxNumber());
        companyDto.setPostOfficeBoxZipCode(company.getPostOfficeBoxZipCode());
        companyDto.setPostOfficeBoxCity(company.getPostOfficeBoxCity());
        companyDto.setPhone(company.getPhone());
        companyDto.setEmail(company.getEmail());
        companyDto.setWebsite(company.getWebsite());
        return companyDto;
    }
}
