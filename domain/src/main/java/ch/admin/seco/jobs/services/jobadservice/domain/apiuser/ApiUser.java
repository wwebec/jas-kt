package ch.admin.seco.jobs.services.jobadservice.domain.apiuser;

import java.time.LocalDate;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import ch.admin.seco.jobs.services.jobadservice.core.conditions.Condition;
import ch.admin.seco.jobs.services.jobadservice.core.domain.Aggregate;

@Entity
public class ApiUser implements Aggregate<ApiUser, ApiUserId> {

	@EmbeddedId
	@AttributeOverride(name = "value", column = @Column(name = "ID"))
	@Valid
	private ApiUserId id;

	@NotEmpty
	private String username;

	@NotEmpty
	private String password;

	@NotEmpty
	private String email;

	@NotNull
	private Boolean active;

	@NotEmpty
	private String companyName;

	@NotEmpty
	private String contactName;

	@NotEmpty
	private String contactEmail;

	@NotNull
	private LocalDate createDate;

	private LocalDate lastAccessDate;

	protected ApiUser() {
		// For reflection libs
	}

	public ApiUser(Builder builder) {
		this.id = Condition.notNull(builder.id);
		this.username = Condition.notBlank(builder.username);
		this.password = Condition.notBlank(builder.password);
		this.email = Condition.notBlank(builder.email);
		this.active = Condition.notNull(builder.active);
		this.companyName = Condition.notBlank(builder.companyName);
		this.contactName = Condition.notBlank(builder.contactName);
		this.contactEmail = Condition.notBlank(builder.contactEmail);
		this.createDate = Condition.notNull(builder.createDate);
		this.lastAccessDate = builder.lastAccessDate;
	}

	public static final class Builder {
		private ApiUserId id;
		private String username;
		private String password;
		private String email;
		private Boolean active;
		private String companyName;
		private String contactName;
		private String contactEmail;
		private LocalDate createDate;
		private LocalDate lastAccessDate;

		public Builder setId(ApiUserId id) {
			this.id = id;
			return this;
		}

		public Builder setUsername(String username) {
			this.username = username;
			return this;
		}

		public Builder setPassword(String password) {
			this.password = password;
			return this;
		}

		public Builder setEmail(String email) {
			this.email = email;
			return this;
		}

		public Builder setActive(Boolean active) {
			this.active = active;
			return this;
		}

		public Builder setCompanyName(String companyName) {
			this.companyName = companyName;
			return this;
		}

		public Builder setContactName(String contactName) {
			this.contactName = contactName;
			return this;
		}

		public Builder setContactEmail(String contactEmail) {
			this.contactEmail = contactEmail;
			return this;
		}

		public Builder setCreateDate(LocalDate createDate) {
			this.createDate = createDate;
			return this;
		}

		public Builder setLastAccessDate(LocalDate lastAccessDate) {
			this.lastAccessDate = lastAccessDate;
			return this;
		}
	}

	public ApiUserId getId() {
		return id;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public String getEmail() {
		return email;
	}

	public Boolean getActive() {
		return active;
	}

	public String getCompanyName() {
		return companyName;
	}

	public String getContactName() {
		return contactName;
	}

	public String getContactEmail() {
		return contactEmail;
	}

	public LocalDate getCreateDate() {
		return createDate;
	}

	public LocalDate getLastAccessDate() {
		return lastAccessDate;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public void update(Builder builder) {
		this.username = Condition.notBlank(builder.username);
		this.password = Condition.notBlank(builder.password);
		this.email = Condition.notBlank(builder.email);
		this.active = Condition.notNull(builder.active);
		this.companyName = Condition.notBlank(builder.companyName);
		this.contactName = Condition.notBlank(builder.contactName);
		this.contactEmail = Condition.notBlank(builder.contactEmail);
	}
}
