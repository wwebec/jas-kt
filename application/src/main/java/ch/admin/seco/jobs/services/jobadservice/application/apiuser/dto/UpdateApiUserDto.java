package ch.admin.seco.jobs.services.jobadservice.application.apiuser.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class UpdateApiUserDto {

	@NotBlank
	private String id;

	@NotBlank
	private String username;

	@NotBlank
	private String password;

	@NotBlank
	private String companyName;

	@NotBlank
	@Email
	private String companyEmail;

	@NotBlank
	private String technicalContactName;

	@NotBlank
	@Email
	private String technicalContactEmail;

	private boolean active;

	protected UpdateApiUserDto() {
		// For reflection libs
	}

	public UpdateApiUserDto(String id, String username, String password, String companyName, String companyEmail, String technicalContactName, String technicalContactEmail, boolean active) {
		this.id = id;
		this.username = username;
		this.password = password;
		this.companyName = companyName;
		this.companyEmail = companyEmail;
		this.technicalContactName = technicalContactName;
		this.technicalContactEmail = technicalContactEmail;
		this.active = active;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getCompanyEmail() {
		return companyEmail;
	}

	public void setCompanyEmail(String companyEmail) {
		this.companyEmail = companyEmail;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getTechnicalContactName() {
		return technicalContactName;
	}

	public void setTechnicalContactName(String technicalContactName) {
		this.technicalContactName = technicalContactName;
	}

	public String getTechnicalContactEmail() {
		return technicalContactEmail;
	}

	public void setTechnicalContactEmail(String technicalContactEmail) {
		this.technicalContactEmail = technicalContactEmail;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

}
