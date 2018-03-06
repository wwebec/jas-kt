package ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.api;

import java.time.LocalDate;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.OccupationDto;

public class CreateJobAdvertisementApiDto {
	@NotNull
	private LocalDate publicationStartDate;

	private LocalDate publicationEndDate;

	private String reference;

	private String url;

	private String applicationUrl;

	@Valid
	@NotNull
	private JobApiDto job;

	@NotNull
	private CompanyApiDto company;

	private ContactApiDto contact;

	@NotNull
	private OccupationDto occupation;

	protected CreateJobAdvertisementApiDto() {
		// For reflection libs
	}

	public CreateJobAdvertisementApiDto(LocalDate publicationStartDate,
			LocalDate publicationEndDate,
			String reference,
			String url,
			String applicationUrl,
			JobApiDto job,
			CompanyApiDto company,
			ContactApiDto contact,
			OccupationDto occupation) {
		this.publicationStartDate = publicationStartDate;
		this.publicationEndDate = publicationEndDate;
		this.reference = reference;
		this.url = url;
		this.applicationUrl = applicationUrl;
		this.job = job;
		this.company = company;
		this.contact = contact;
		this.occupation = occupation;
	}

	public OccupationDto getOccupation() {
		return occupation;
	}

	public void setOccupation(OccupationDto occupation) {
		this.occupation = occupation;
	}

	public LocalDate getPublicationStartDate() {
		return publicationStartDate;
	}

	public void setPublicationStartDate(LocalDate publicationStartDate) {
		this.publicationStartDate = publicationStartDate;
	}

	public LocalDate getPublicationEndDate() {
		return publicationEndDate;
	}

	public void setPublicationEndDate(LocalDate publicationEndDate) {
		this.publicationEndDate = publicationEndDate;
	}

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getApplicationUrl() {
		return applicationUrl;
	}

	public void setApplicationUrl(String applicationUrl) {
		this.applicationUrl = applicationUrl;
	}

	public JobApiDto getJob() {
		return job;
	}

	public void setJob(JobApiDto job) {
		this.job = job;
	}

	public CompanyApiDto getCompany() {
		return company;
	}

	public void setCompany(CompanyApiDto company) {
		this.company = company;
	}

	public ContactApiDto getContact() {
		return contact;
	}

	public void setContact(ContactApiDto contact) {
		this.contact = contact;
	}
}
