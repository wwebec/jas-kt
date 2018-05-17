package ch.admin.seco.jobs.services.jobadservice.application.apiuser.dto;

import javax.validation.constraints.NotNull;

public class ChangeApiUserStatusDto {

	@NotNull
	private Boolean active;

	protected ChangeApiUserStatusDto() {
	}

	public ChangeApiUserStatusDto(Boolean active) {
		this.active = active;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}
}
