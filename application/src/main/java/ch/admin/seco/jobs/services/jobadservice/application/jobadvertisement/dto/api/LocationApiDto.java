package ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.api;

import javax.validation.constraints.NotEmpty;

public class LocationApiDto {

	private String countryCode;
	private String locality;
	private String postalCode;
	@NotEmpty
	private String cantonCode;
	private String additionalDetails;

	protected LocationApiDto() {
		// For reflection libs
	}

	public LocationApiDto(String countryCode, String locality, String postalCode, String cantonCode, String additionalDetails) {
		this.countryCode = countryCode;
		this.locality = locality;
		this.postalCode = postalCode;
		this.cantonCode = cantonCode;
		this.additionalDetails = additionalDetails;
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

	public String getLocality() {
		return locality;
	}

	public void setLocality(String locality) {
		this.locality = locality;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public String getAdditionalDetails() {
		return additionalDetails;
	}

	public void setAdditionalDetails(String additionalDetails) {
		this.additionalDetails = additionalDetails;
	}
}
