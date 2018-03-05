package ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.api;

public class PostboxDto {

	private String number;
	private String locality;
	private String postalCode;

	protected PostboxDto() {
		// For reflection libs
	}

	public PostboxDto(String number, String locality, String postalCode) {
		this.number = number;
		this.locality = locality;
		this.postalCode = postalCode;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
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
}
