package ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.api;

import javax.validation.constraints.NotNull;

public class LegacyCompanyDto {

    @NotNull
    private String name;
    @NotNull
    private String street;
    private String houseNumber;
    @NotNull
    private String zipCode;
    @NotNull
    private String city;
    private String postboxNumber;
    private String postboxZipCode;
    private String postboxCity;
    @NotNull
    private String countryCode;

    protected LegacyCompanyDto() {
        // For reflection libs
    }

    public LegacyCompanyDto(String name, String street, String houseNumber, String zipCode, String city, String postboxNumber, String postboxZipCode, String postboxCity, String countryCode) {
        this.name = name;
        this.street = street;
        this.houseNumber = houseNumber;
        this.zipCode = zipCode;
        this.city = city;
        this.postboxNumber = postboxNumber;
        this.postboxZipCode = postboxZipCode;
        this.postboxCity = postboxCity;
        this.countryCode = countryCode;
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

    public String getPostboxNumber() {
        return postboxNumber;
    }

    public void setPostboxNumber(String postboxNumber) {
        this.postboxNumber = postboxNumber;
    }

    public String getPostboxZipCode() {
        return postboxZipCode;
    }

    public void setPostboxZipCode(String postboxZipCode) {
        this.postboxZipCode = postboxZipCode;
    }

    public String getPostboxCity() {
        return postboxCity;
    }

    public void setPostboxCity(String postboxCity) {
        this.postboxCity = postboxCity;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

}
