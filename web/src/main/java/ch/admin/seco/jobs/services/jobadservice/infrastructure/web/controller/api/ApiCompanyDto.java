package ch.admin.seco.jobs.services.jobadservice.infrastructure.web.controller.api;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.Company;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.utils.CountryIsoCode;

public class ApiCompanyDto {

    @NotBlank
    @Size(max=255)
    private String name;

    @Size(max=50)
    private String street;

    @Size(max=10)
    private String houseNumber;

    @NotBlank
    @Size(max=10)
    private String postalCode;

    @NotBlank
    @Size(max=100)
    private String city;

    @NotBlank
    @CountryIsoCode
    private String countryIsoCode;

    @Size(max=10)
    private String postOfficeBoxNumber;

    @Size(max=10)
    private String postOfficeBoxPostalCode;

    @Size(max=100)
    private String postOfficeBoxCity;

    @Size(max=20)
    private String phone;

    @Size(max=50)
    private String email;

    @Size(max=255)
    private String website;

    private boolean surrogate;

    protected ApiCompanyDto() {
        // For reflection libs
    }

    public ApiCompanyDto(String name, String street, String houseNumber, String postalCode, String city, String countryIsoCode, String postOfficeBoxNumber, String postOfficeBoxPostalCode, String postOfficeBoxCity, String phone, String email, String website, boolean surrogate) {
        this.name = name;
        this.street = street;
        this.houseNumber = houseNumber;
        this.postalCode = postalCode;
        this.city = city;
        this.countryIsoCode = countryIsoCode;
        this.postOfficeBoxNumber = postOfficeBoxNumber;
        this.postOfficeBoxPostalCode = postOfficeBoxPostalCode;
        this.postOfficeBoxCity = postOfficeBoxCity;
        this.phone = phone;
        this.email = email;
        this.website = website;
        this.surrogate = surrogate;
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

    public String getPostOfficeBoxNumber() {
        return postOfficeBoxNumber;
    }

    public void setPostOfficeBoxNumber(String postOfficeBoxNumber) {
        this.postOfficeBoxNumber = postOfficeBoxNumber;
    }

    public String getPostOfficeBoxPostalCode() {
        return postOfficeBoxPostalCode;
    }

    public void setPostOfficeBoxPostalCode(String postOfficeBoxPostalCode) {
        this.postOfficeBoxPostalCode = postOfficeBoxPostalCode;
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

    public boolean isSurrogate() {
        return surrogate;
    }

    public void setSurrogate(boolean surrogate) {
        this.surrogate = surrogate;
    }

    public static ApiCompanyDto toDto(Company company) {
        ApiCompanyDto companyDto = new ApiCompanyDto();
        companyDto.setName(company.getName());
        companyDto.setStreet(company.getStreet());
        companyDto.setHouseNumber(company.getHouseNumber());
        companyDto.setPostalCode(company.getPostalCode());
        companyDto.setCity(company.getCity());
        companyDto.setCountryIsoCode(company.getCountryIsoCode());
        companyDto.setPostOfficeBoxNumber(company.getPostOfficeBoxNumber());
        companyDto.setPostOfficeBoxPostalCode(company.getPostOfficeBoxPostalCode());
        companyDto.setPostOfficeBoxCity(company.getPostOfficeBoxCity());
        companyDto.setPhone(company.getPhone());
        companyDto.setEmail(company.getEmail());
        companyDto.setWebsite(company.getWebsite());
        companyDto.setSurrogate(company.isSurrogate());
        return companyDto;
    }
}
