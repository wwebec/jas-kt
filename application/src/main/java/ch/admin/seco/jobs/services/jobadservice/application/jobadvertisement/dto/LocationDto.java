package ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.GeoPoint;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.Location;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.utils.CountryIsoCode;

public class LocationDto {

    private String remarks;

    private String city;

    @NotEmpty
    private String postalCode;

    private String communalCode;

    private String regionCode;

    private String cantonCode;

    @NotBlank
    @CountryIsoCode
    private String countryIsoCode;

    private GeoPoint coordinates;

    protected LocationDto() {
        // For reflection libs
    }

    public LocationDto(String remarks, String city, String postalCode, String communalCode, String regionCode, String cantonCode, String countryIsoCode, GeoPoint coordinates) {
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

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCommunalCode() {
        return communalCode;
    }

    public void setCommunalCode(String communalCode) {
        this.communalCode = communalCode;
    }

    public String getRegionCode() {
        return regionCode;
    }

    public void setRegionCode(String regionCode) {
        this.regionCode = regionCode;
    }

    public String getCantonCode() {
        return cantonCode;
    }

    public void setCantonCode(String cantonCode) {
        this.cantonCode = cantonCode;
    }

    public String getCountryIsoCode() {
        return countryIsoCode;
    }

    public void setCountryIsoCode(String countryIsoCode) {
        this.countryIsoCode = countryIsoCode;
    }

    public GeoPoint getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(GeoPoint coordinates) {
        this.coordinates = coordinates;
    }

    public static LocationDto toDto(Location location) {
        if(location == null) {
            return null;
        }

        LocationDto locationDto = new LocationDto();
        locationDto.setRemarks(location.getRemarks());
        locationDto.setCity(location.getCity());
        locationDto.setPostalCode(location.getPostalCode());
        locationDto.setCommunalCode(location.getCommunalCode());
        locationDto.setRegionCode(location.getRegionCode());
        locationDto.setCantonCode(location.getCantonCode());
        locationDto.setCountryIsoCode(location.getCountryIsoCode());
        locationDto.setCoordinates(location.getCoordinates());
        return locationDto;
    }
}
