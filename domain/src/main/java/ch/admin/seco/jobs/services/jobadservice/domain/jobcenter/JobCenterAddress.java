package ch.admin.seco.jobs.services.jobadservice.domain.jobcenter;

public class JobCenterAddress {

    private String name;
    private String city;
    private String street;
    private String houseNumber;
    private String zipCode;

    protected JobCenterAddress() {
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

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
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
}
