package ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement;

public class Company {

    private String name;
    private String street;
    private String houseNumber;
    private String zipCode;
    private String city;
    private String countryCode;
    private String postOfficeBoxNumber;
    private String postOfficeBoxZipCode;
    private String postOfficeBoxCity;
    private String phone;
    private String email;
    private String website;

    protected Company() {
    }

    public Company(String name, String street, String houseNumber, String zipCode, String city, String countryCode, String postOfficeBoxNumber, String postOfficeBoxZipCode, String postOfficeBoxCity, String phone, String email, String website) {
        this.name = name;
        this.street = street;
        this.houseNumber = houseNumber;
        this.zipCode = zipCode;
        this.city = city;
        this.countryCode = countryCode;
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

    public String getStreet() {
        return street;
    }

    public String getHouseNumber() {
        return houseNumber;
    }

    public String getZipCode() {
        return zipCode;
    }

    public String getCity() {
        return city;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public String getPostOfficeBoxNumber() {
        return postOfficeBoxNumber;
    }

    public String getPostOfficeBoxZipCode() {
        return postOfficeBoxZipCode;
    }

    public String getPostOfficeBoxCity() {
        return postOfficeBoxCity;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public String getWebsite() {
        return website;
    }

}
