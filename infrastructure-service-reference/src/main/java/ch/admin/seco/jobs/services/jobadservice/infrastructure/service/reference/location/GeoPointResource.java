package ch.admin.seco.jobs.services.jobadservice.infrastructure.service.reference.location;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GeoPointResource {

    @JsonProperty("lat")
    private Double latitude;

    @JsonProperty("lon")
    private Double longitude;

    protected GeoPointResource() {
        // For reflection libs
    }

    public GeoPointResource(Double latitude, Double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
}
