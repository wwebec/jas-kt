package ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto;

import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.GeoPoint;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.Locality;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

public class LocalityDto {

    private String remarks;
    private String city;
    @NotNull
    private String zipCode;
    private String communalCode;
    private String regionCode;
    private String cantonCode;
    private String countryCode;
    private GeoPoint location;

    protected LocalityDto() {
        // For reflection libs
    }

    public LocalityDto(String remarks, String city, String zipCode, String communalCode, String regionCode, String cantonCode, String countryCode, GeoPoint location) {
        this.remarks = remarks;
        this.city = city;
        this.zipCode = zipCode;
        this.communalCode = communalCode;
        this.regionCode = regionCode;
        this.cantonCode = cantonCode;
        this.countryCode = countryCode;
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

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
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
        localityDto.setZipCode(locality.getZipCode());
        localityDto.setCommunalCode(locality.getCommunalCode());
        localityDto.setRegionCode(locality.getRegionCode());
        localityDto.setCantonCode(locality.getCantonCode());
        localityDto.setCountryCode(locality.getCountryCode());
        localityDto.setLocation(locality.getLocation());
        return localityDto;
    }

    public static List<LocalityDto> toDto(List<Locality> localities) {
        return localities.stream().map(LocalityDto::toDto).collect(Collectors.toList());
    }
}
