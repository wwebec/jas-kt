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

    public Location(Builder builder) {
        this.remarks = builder.remarks;
        this.city = builder.city;
        this.postalCode = builder.postalCode;
        this.communalCode = builder.communalCode;
        this.regionCode = builder.regionCode;
        this.cantonCode = builder.cantonCode;
        this.countryIsoCode = builder.countryIsoCode;
        this.coordinates = builder.coordinates;
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

    public static final class Builder {
        private String remarks;
        private String city;
        private String postalCode;
        private String communalCode;
        private String regionCode;
        private String cantonCode;
        private String countryIsoCode;
        private GeoPoint coordinates;

        public Builder() {
        }

        public Location build() {
            return new Location(this);
        }

        public Builder setRemarks(String remarks) {
            this.remarks = remarks;
            return this;
        }

        public Builder setCity(String city) {
            this.city = city;
            return this;
        }

        public Builder setPostalCode(String postalCode) {
            this.postalCode = postalCode;
            return this;
        }

        public Builder setCommunalCode(String communalCode) {
            this.communalCode = communalCode;
            return this;
        }

        public Builder setRegionCode(String regionCode) {
            this.regionCode = regionCode;
            return this;
        }

        public Builder setCantonCode(String cantonCode) {
            this.cantonCode = cantonCode;
            return this;
        }

        public Builder setCountryIsoCode(String countryIsoCode) {
            this.countryIsoCode = countryIsoCode;
            return this;
        }

        public Builder setCoordinates(GeoPoint coordinates) {
            this.coordinates = coordinates;
            return this;
        }
    }
}
