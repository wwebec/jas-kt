package ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement;

import ch.admin.seco.jobs.services.jobadservice.core.domain.ValueObject;

import javax.persistence.*;
import javax.validation.Valid;
import java.util.Objects;

@Embeddable
@Access(AccessType.FIELD)
public class Locality implements ValueObject<Locality> {

    private String remarks;

    private String city;

    private String zipCode;

    private String communalCode;

    private String regionCode; // TODO Still needed?

    private String cantonCode;

    private String countryIsoCode;

    @Embedded
    @Valid
    private GeoPoint location;

    protected Locality() {
        // For reflection libs
    }

    public Locality(String remarks, String city, String zipCode, String communalCode, String regionCode, String cantonCode, String countryIsoCode, GeoPoint location) {
        this.remarks = remarks;
        this.city = city;
        this.zipCode = zipCode;
        this.communalCode = communalCode;
        this.regionCode = regionCode;
        this.cantonCode = cantonCode;
        this.countryIsoCode = countryIsoCode;
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

    public String getCountryIsoCode() {
        return countryIsoCode;
    }

    public GeoPoint getLocation() {
        return location;
    }

    @Override
    public boolean sameValueObjectAs(Locality other) {
        return equals(other);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Locality locality = (Locality) o;
        return Objects.equals(remarks, locality.remarks) &&
                Objects.equals(city, locality.city) &&
                Objects.equals(zipCode, locality.zipCode) &&
                Objects.equals(communalCode, locality.communalCode) &&
                Objects.equals(regionCode, locality.regionCode) &&
                Objects.equals(cantonCode, locality.cantonCode) &&
                Objects.equals(countryIsoCode, locality.countryIsoCode) &&
                Objects.equals(location, locality.location);
    }

    @Override
    public int hashCode() {
        return Objects.hash(remarks, city, zipCode, communalCode, regionCode, cantonCode, countryIsoCode, location);
    }

    @Override
    public String toString() {
        return "Locality{" +
                "remarks='" + remarks + '\'' +
                ", city='" + city + '\'' +
                ", zipCode='" + zipCode + '\'' +
                ", communalCode='" + communalCode + '\'' +
                ", regionCode='" + regionCode + '\'' +
                ", cantonCode='" + cantonCode + '\'' +
                ", countryIsoCode='" + countryIsoCode + '\'' +
                ", location=" + location +
                '}';
    }
}
