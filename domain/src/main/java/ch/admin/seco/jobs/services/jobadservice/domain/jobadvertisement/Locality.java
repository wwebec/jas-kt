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
public class Locality implements ValueObject<Locality> {

    private String remarks;

    private String city;

    private String postalCode;

    private String communalCode;

    private String regionCode;

    private String cantonCode;

    private String countryIsoCode;

    @Embedded
    @Valid
    private GeoPoint location;

    protected Locality() {
        // For reflection libs
    }

    public Locality(String remarks, String city, String postalCode, String communalCode, String regionCode, String cantonCode, String countryIsoCode, GeoPoint location) {
        this.remarks = remarks;
        this.city = city;
        this.postalCode = postalCode;
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
                Objects.equals(postalCode, locality.postalCode) &&
                Objects.equals(communalCode, locality.communalCode) &&
                Objects.equals(regionCode, locality.regionCode) &&
                Objects.equals(cantonCode, locality.cantonCode) &&
                Objects.equals(countryIsoCode, locality.countryIsoCode) &&
                Objects.equals(location, locality.location);
    }

    @Override
    public int hashCode() {
        return Objects.hash(remarks, city, postalCode, communalCode, regionCode, cantonCode, countryIsoCode, location);
    }

    @Override
    public String toString() {
        return "Locality{" +
                "remarks='" + remarks + '\'' +
                ", city='" + city + '\'' +
                ", postalCode='" + postalCode + '\'' +
                ", communalCode='" + communalCode + '\'' +
                ", regionCode='" + regionCode + '\'' +
                ", cantonCode='" + cantonCode + '\'' +
                ", countryIsoCode='" + countryIsoCode + '\'' +
                ", location=" + location +
                '}';
    }
}
