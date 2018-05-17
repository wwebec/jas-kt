package ch.admin.seco.jobs.services.jobadservice.application.apiuser.dto;

import java.time.LocalDate;
import ch.admin.seco.jobs.services.jobadservice.domain.apiuser.ApiUser;

public class ApiUserDto {
	private String id;
	private String username;
	private String password;
	private String email;
	private Boolean active;
	private String companyName;
	private String contactName;
	private String contactEmail;
	private LocalDate createDate;
	private LocalDate lastAccessDate;

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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getContactName() {
		return contactName;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	public String getContactEmail() {
		return contactEmail;
	}

	public void setContactEmail(String contactEmail) {
		this.contactEmail = contactEmail;
	}

	public LocalDate getCreateDate() {
		return createDate;
	}

	public void setCreateDate(LocalDate createDate) {
		this.createDate = createDate;
	}

	public LocalDate getLastAccessDate() {
		return lastAccessDate;
	}

	public void setLastAccessDate(LocalDate lastAccessDate) {
		this.lastAccessDate = lastAccessDate;
	}

	public static ApiUserDto toDto(ApiUser apiUser) {
		ApiUserDto dto = new ApiUserDto();
		dto.setId(apiUser.getId().getValue());
		dto.setUsername(apiUser.getUsername());
		dto.setPassword(apiUser.getPassword());
		dto.setEmail(apiUser.getEmail());
		dto.setActive(apiUser.getActive());
		dto.setCompanyName(apiUser.getCompanyName());
		dto.setContactName(apiUser.getContactName());
		dto.setContactEmail(apiUser.getContactEmail());
		dto.setCreateDate(apiUser.getCreateDate());
		dto.setLastAccessDate(apiUser.getLastAccessDate());
		return dto;
	}
}
