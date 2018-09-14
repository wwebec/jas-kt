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

    protected CompanyDto() {
        // For reflection libs
    }

    public CompanyDto(String name, String street, String houseNumber, String postalCode, String city, String countryIsoCode, String postOfficeBoxNumber, String postOfficeBoxPostalCode, String postOfficeBoxCity, String phone, String email, String website, boolean surrogate) {
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
