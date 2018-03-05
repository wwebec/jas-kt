package ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.api;

public class CompanyDto {

	private String name;
	private String countryCode;
	private String street;
	private String houseNumber;
	private String locality;
	private String postalCode;
	private String phoneNumber;
	private String email;
	private String website;
	private PostboxDto postbox;

	protected CompanyDto() {
		this.postbox = new PostboxDto();
	}

	public CompanyDto(String name,
			String countryCode,
			String street,
			String houseNumber,
			String locality,
			String postalCode,
			String phoneNumber,
			String email,
			String website,
			PostboxDto postbox) {
		this.name = name;
		this.countryCode = countryCode;
		this.street = street;
		this.houseNumber = houseNumber;
		this.locality = locality;
		this.postalCode = postalCode;
		this.phoneNumber = phoneNumber;
		this.email = email;
		this.website = website;
		this.postbox = postbox;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getHouseNumber() {
		return houseNumber;
	}

	public void setHouseNumber(String houseNumber) {
		this.houseNumber = houseNumber;
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

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public PostboxDto getPostbox() {
		return postbox;
	}

	public void setPostbox(PostboxDto postbox) {
		this.postbox = postbox;
	}
}
