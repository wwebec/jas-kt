package ch.admin.seco.jobs.services.jobadservice.infrastructure.service.reference.location;

import java.util.UUID;

public class LocationResource {

    private UUID id;
    private String city;
    private String zipCode;
    private String communalCode;
    private String cantonCode;
    private String regionCode;
    private GeoPointResource geoPoint;

    protected LocationResource() {
        // For reflection libs
    }

    public LocationResource(UUID id, String city, String zipCode, String communalCode, String cantonCode, String regionCode, GeoPointResource geoPoint) {
        this.id = id;
        this.city = city;
        this.zipCode = zipCode;
        this.communalCode = communalCode;
        this.cantonCode = cantonCode;
        this.regionCode = regionCode;
        this.geoPoint = geoPoint;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getCommunalCode() {
        return communalCode;
    }

    public void setCommunalCode(String communalCode) {
        this.communalCode = communalCode;
    }

    public String getCantonCode() {
        return cantonCode;
    }

    public void setCantonCode(String cantonCode) {
        this.cantonCode = cantonCode;
    }

    public String getRegionCode() {
        return regionCode;
    }

    public void setRegionCode(String regionCode) {
        this.regionCode = regionCode;
    }

    public GeoPointResource getGeoPoint() {
        return geoPoint;
    }

    public void setGeoPoint(GeoPointResource geoPoint) {
        this.geoPoint = geoPoint;
    }
}
