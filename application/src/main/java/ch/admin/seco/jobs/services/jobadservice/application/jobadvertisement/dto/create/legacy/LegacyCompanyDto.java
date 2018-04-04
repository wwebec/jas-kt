package ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.create.legacy;

import javax.validation.constraints.NotNull;

public class LegacyCompanyDto {

    @NotNull
    private String name;
    private String street;
    private String houseNumber;
    private String postalCode;
    private String locality;
    private LegacyPostboxDto postbox;
    @NotNull
    private String countryCode;
    private String phoneNumber;
    private String email;
    private String website;

    protected LegacyCompanyDto() {
        // For reflection libs
    }

    public LegacyCompanyDto(String name, String street, String houseNumber, String postalCode, String locality, LegacyPostboxDto postbox, String countryCode, String phoneNumber, String email, String website) {
        this.name = name;
        this.street = street;
        this.houseNumber = houseNumber;
        this.postalCode = postalCode;
        this.locality = locality;
        this.postbox = postbox;
        this.countryCode = countryCode;
        this.phoneNumber = phoneNumber;
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

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public LegacyPostboxDto getPostbox() {
        return postbox;
    }

    public void setPostbox(LegacyPostboxDto postbox) {
        this.postbox = postbox;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
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
}
