package ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement;

public class GeoPoint {

    private double longitude;
    private double latitude;

    protected GeoPoint() {
        // For reflection libs
    }

    public GeoPoint(double longitude, double latitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }
}
