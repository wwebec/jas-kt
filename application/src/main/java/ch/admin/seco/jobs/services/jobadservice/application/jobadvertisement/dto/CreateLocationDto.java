package ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto;

import javax.validation.constraints.NotEmpty;

public class CreateLocationDto {

    private String remarks;
    private String city;
    @NotEmpty
    private String postalCode;
    private String countryIsoCode;
    private String communalCode;

    protected CreateLocationDto() {
        // For reflection libs
    }

    public CreateLocationDto(String remarks, String city, String postalCode, String countryIsoCode,
            String communalCode) {
        this.remarks = remarks;
        this.city = city;
        this.postalCode = postalCode;
        this.countryIsoCode = countryIsoCode;
        this.communalCode = communalCode;
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

    public String getCountryIsoCode() {
        return countryIsoCode;
    }

    public void setCountryIsoCode(String countryIsoCode) {
        this.countryIsoCode = countryIsoCode;
    }

    public String getCommunalCode() {
        return communalCode;
    }

    public void setCommunalCode(String communalCode) {
        this.communalCode = communalCode;
    }
}
