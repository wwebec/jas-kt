package ch.admin.seco.jobs.services.jobadservice.domain.jobcenter;

public class JobCenterAddress {

    private String name;
    private String city;
    private String street;
    private String houseNumber;
    private String zipCode;

    public JobCenterAddress() {
        // For reflection libs
    }

    public JobCenterAddress(String name, String city, String street, String houseNumber, String zipCode) {
        this.name = name;
        this.city = city;
        this.street = street;
        this.houseNumber = houseNumber;
        this.zipCode = zipCode;
    }

    public String getName() {
        return name;
    }

    public JobCenterAddress setName(String name) {
        this.name = name;
        return this;
    }

    public String getCity() {
        return city;
    }

    public JobCenterAddress setCity(String city) {
        this.city = city;
        return this;
    }

    public String getStreet() {
        return street;
    }

    public JobCenterAddress setStreet(String street) {
        this.street = street;
        return this;
    }

    public String getHouseNumber() {
        return houseNumber;
    }

    public JobCenterAddress setHouseNumber(String houseNumber) {
        this.houseNumber = houseNumber;
        return this;
    }

    public String getZipCode() {
        return zipCode;
    }

    public JobCenterAddress setZipCode(String zipCode) {
        this.zipCode = zipCode;
        return this;
    }
}
