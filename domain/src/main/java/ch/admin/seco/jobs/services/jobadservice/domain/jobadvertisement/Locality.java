package ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement;

public class Locality {

    private String remarks;
    private String city;
    private String zipCode;
    private String communalCode;
    private String regionCode;
    private String cantonCode;
    private String countryCode;
    private GeoPoint location;

    protected Locality() {
    }

    public Locality(String remarks, String city, String zipCode, String communalCode, String regionCode, String cantonCode, String countryCode, GeoPoint location) {
        this.remarks = remarks;
        this.city = city;
        this.zipCode = zipCode;
        this.communalCode = communalCode;
        this.regionCode = regionCode;
        this.cantonCode = cantonCode;
        this.countryCode = countryCode;
        this.location = location;
    }

    public String getRemarks() {
        return remarks;
    }

    public String getCity() {
        return city;
    }

    public String getZipCode() {
        return zipCode;
    }

    public String getCommunalCode() {
        return communalCode;
    }

    public String getRegionCode() {
        return regionCode;
    }

    public String getCantonCode() {
        return cantonCode;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public GeoPoint getLocation() {
        return location;
    }
}
