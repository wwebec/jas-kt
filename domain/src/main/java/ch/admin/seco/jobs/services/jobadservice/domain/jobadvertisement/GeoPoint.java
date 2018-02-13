package ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Embeddable;

@Embeddable
@Access(AccessType.FIELD)
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
