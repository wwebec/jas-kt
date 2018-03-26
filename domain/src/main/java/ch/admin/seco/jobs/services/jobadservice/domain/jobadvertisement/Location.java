package ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement;

import ch.admin.seco.jobs.services.jobadservice.core.domain.ValueObject;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.validation.Valid;
import java.util.Objects;

@Embeddable
@Access(AccessType.FIELD)
public class Location implements ValueObject<Location> {

    private String remarks;

    private String city;

    private String postalCode;

    private String communalCode;

    private String regionCode;

    private String cantonCode;

    private String countryIsoCode;

    @Embedded
    @Valid
    private GeoPoint coordinates;

    protected Location() {
        // For reflection libs
    }

    public Location(String remarks, String city, String postalCode, String communalCode, String regionCode, String cantonCode, String countryIsoCode, GeoPoint coordinates) {
        this.remarks = remarks;
        this.city = city;
        this.postalCode = postalCode;
        this.communalCode = communalCode;
        this.regionCode = regionCode;
        this.cantonCode = cantonCode;
        this.countryIsoCode = countryIsoCode;
        this.coordinates = coordinates;
    }

    public String getRemarks() {
        return remarks;
    }

    public String getCity() {
        return city;
    }

    public String getPostalCode() {
        return postalCode;
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

    public String getCountryIsoCode() {
        return countryIsoCode;
    }

    public GeoPoint getCoordinates() {
        return coordinates;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Location location = (Location) o;
        return Objects.equals(remarks, location.remarks) &&
                Objects.equals(city, location.city) &&
                Objects.equals(postalCode, location.postalCode) &&
                Objects.equals(communalCode, location.communalCode) &&
                Objects.equals(regionCode, location.regionCode) &&
                Objects.equals(cantonCode, location.cantonCode) &&
                Objects.equals(countryIsoCode, location.countryIsoCode) &&
                Objects.equals(coordinates, location.coordinates);
    }

    @Override
    public int hashCode() {
        return Objects.hash(remarks, city, postalCode, communalCode, regionCode, cantonCode, countryIsoCode, coordinates);
    }

    @Override
    public String toString() {
        return "Location{" +
                "remarks='" + remarks + '\'' +
                ", city='" + city + '\'' +
                ", postalCode='" + postalCode + '\'' +
                ", communalCode='" + communalCode + '\'' +
                ", regionCode='" + regionCode + '\'' +
                ", cantonCode='" + cantonCode + '\'' +
                ", countryIsoCode='" + countryIsoCode + '\'' +
                ", coordinates=" + coordinates +
                '}';
    }
}
