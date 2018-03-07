package ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto;

import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.GeoPoint;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.Locality;

import javax.validation.constraints.NotEmpty;

public class LocalityDto {

    private String remarks;
    private String city;
    @NotEmpty
    private String postalCode;
    private String communalCode;
    private String regionCode;
    private String cantonCode;
    private String countryIsoCode;
    private GeoPoint location;

    protected LocalityDto() {
        // For reflection libs
    }

    public LocalityDto(String remarks, String city, String postalCode, String communalCode, String regionCode, String cantonCode, String countryIsoCode, GeoPoint location) {
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

    public GeoPoint getLocation() {
        return location;
    }

    public void setLocation(GeoPoint location) {
        this.location = location;
    }

    public static LocalityDto toDto(Locality locality) {
        LocalityDto localityDto = new LocalityDto();
        localityDto.setRemarks(locality.getRemarks());
        localityDto.setCity(locality.getCity());
        localityDto.setPostalCode(locality.getPostalCode());
        localityDto.setCommunalCode(locality.getCommunalCode());
        localityDto.setRegionCode(locality.getRegionCode());
        localityDto.setCantonCode(locality.getCantonCode());
        localityDto.setCountryIsoCode(locality.getCountryIsoCode());
        localityDto.setLocation(locality.getLocation());
        return localityDto;
    }
}
