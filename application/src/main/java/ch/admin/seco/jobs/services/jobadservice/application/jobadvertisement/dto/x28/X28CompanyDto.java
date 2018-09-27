package ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.x28;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.CompanyDto;

public class X28CompanyDto {

    @NotBlank
    @Size(max = 255)
    private String name;

    @Size(max = 50)
    private String street;

    @Size(max = 10)
    private String houseNumber;

    @Size(max = 10)
    private String postalCode;

    @Size(max = 100)
    private String city;

    @Size(max = 2)
    private String countryIsoCode;

    @Size(max = 10)
    private String postOfficeBoxNumber;

    @Size(max = 255)
    private String postOfficeBoxPostalCode;

    @Size(max = 255)
    private String postOfficeBoxCity;

    @Size(max = 20)
    private String phone;

    @Size(max = 255)
    private String email;

    @Size(max = 255)
    private String website;

    private boolean surrogate;

    public X28CompanyDto() {
        // For reflection libs
    }

    public X28CompanyDto(String name, String street, String houseNumber, String postalCode, String city, String countryIsoCode, String postOfficeBoxNumber, String postOfficeBoxPostalCode, String postOfficeBoxCity, String phone, String email, String website, boolean surrogate) {
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

    CompanyDto toCompanyDto() {
        return new CompanyDto()
                .setName(name)
                .setStreet(street)
                .setHouseNumber(houseNumber)
                .setPostalCode(postalCode)
                .setCity(city)
                .setCountryIsoCode(countryIsoCode)
                .setPostOfficeBoxNumber(postOfficeBoxNumber)
                .setPostOfficeBoxPostalCode(postOfficeBoxPostalCode)
                .setPostOfficeBoxCity(postOfficeBoxCity)
                .setPhone(phone)
                .setEmail(email)
                .setWebsite(website)
                .setSurrogate(surrogate);
    }

    public String getName() {
        return name;
    }

    public X28CompanyDto setName(String name) {
        this.name = name;
        return this;
    }

    public String getStreet() {
        return street;
    }

    public X28CompanyDto setStreet(String street) {
        this.street = street;
        return this;
    }

    public String getHouseNumber() {
        return houseNumber;
    }

    public X28CompanyDto setHouseNumber(String houseNumber) {
        this.houseNumber = houseNumber;
        return this;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public X28CompanyDto setPostalCode(String postalCode) {
        this.postalCode = postalCode;
        return this;
    }

    public String getCity() {
        return city;
    }

    public X28CompanyDto setCity(String city) {
        this.city = city;
        return this;
    }

    public String getCountryIsoCode() {
        return countryIsoCode;
    }

    public X28CompanyDto setCountryIsoCode(String countryIsoCode) {
        this.countryIsoCode = countryIsoCode;
        return this;
    }

    public String getPostOfficeBoxNumber() {
        return postOfficeBoxNumber;
    }

    public X28CompanyDto setPostOfficeBoxNumber(String postOfficeBoxNumber) {
        this.postOfficeBoxNumber = postOfficeBoxNumber;
        return this;
    }

    public String getPostOfficeBoxPostalCode() {
        return postOfficeBoxPostalCode;
    }

    public X28CompanyDto setPostOfficeBoxPostalCode(String postOfficeBoxPostalCode) {
        this.postOfficeBoxPostalCode = postOfficeBoxPostalCode;
        return this;
    }

    public String getPostOfficeBoxCity() {
        return postOfficeBoxCity;
    }

    public X28CompanyDto setPostOfficeBoxCity(String postOfficeBoxCity) {
        this.postOfficeBoxCity = postOfficeBoxCity;
        return this;
    }

    public String getPhone() {
        return phone;
    }

    public X28CompanyDto setPhone(String phone) {
        this.phone = phone;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public X28CompanyDto setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getWebsite() {
        return website;
    }

    public X28CompanyDto setWebsite(String website) {
        this.website = website;
        return this;
    }

    public boolean isSurrogate() {
        return surrogate;
    }

    public X28CompanyDto setSurrogate(boolean surrogate) {
        this.surrogate = surrogate;
        return this;
    }
}
