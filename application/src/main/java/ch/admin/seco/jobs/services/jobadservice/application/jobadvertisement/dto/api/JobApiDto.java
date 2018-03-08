package ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.api;

import java.time.LocalDate;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class JobApiDto {
	// TODO Job-Language

	private String title;
	@Size(max = 10000)
	private String description;

	@NotNull
	private Integer workingTimePercentageFrom;
	@NotNull
	private Integer workingTimePercentageTo;
	private LocalDate startDate;
	private LocalDate endDate;
	private Integer durationInDays;
	@NotNull
	private Boolean startsImmediately;
	private Boolean permanent;
	@NotNull
	@Valid
	private LocationApiDto location;
	@Size(max = 5)
	private List<LanguageSkillApiDto> languageSkills;

	protected JobApiDto() {
		// For reflection libs
	}

	public JobApiDto(String title,
                     String description,
                     Integer workingTimePercentageFrom,
                     Integer workingTimePercentageTo,
                     LocalDate startDate,
                     LocalDate endDate,
                     Integer durationInDays,
                     Boolean startsImmediately,
                     Boolean permanent,
                     LocationApiDto location,
                     List<LanguageSkillApiDto> languageSkills) {
		this.title = title;
		this.description = description;
		this.workingTimePercentageFrom = workingTimePercentageFrom;
		this.workingTimePercentageTo = workingTimePercentageTo;
		this.startDate = startDate;
		this.endDate = endDate;
		this.durationInDays = durationInDays;
		this.startsImmediately = startsImmediately;
		this.permanent = permanent;
		this.location = location;
		this.languageSkills = languageSkills;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getWorkingTimePercentageFrom() {
		return workingTimePercentageFrom;
	}

	public void setWorkingTimePercentageFrom(Integer workingTimePercentageFrom) {
		this.workingTimePercentageFrom = workingTimePercentageFrom;
	}

	public Integer getWorkingTimePercentageTo() {
		return workingTimePercentageTo;
	}

	public void setWorkingTimePercentageTo(Integer workingTimePercentageTo) {
		this.workingTimePercentageTo = workingTimePercentageTo;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

	public LocalDate getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}

	public Boolean getStartsImmediately() {
		return startsImmediately;
	}

	public void setStartsImmediately(Boolean startsImmediately) {
		this.startsImmediately = startsImmediately;
	}

	public Boolean getPermanent() {
		return permanent;
	}

	public void setPermanent(Boolean permanent) {
		this.permanent = permanent;
	}

	public LocationApiDto getLocation() {
		return location;
	}

	public void setLocation(LocationApiDto location) {
		this.location = location;
	}

	public List<LanguageSkillApiDto> getLanguageSkills() {
		return languageSkills;
	}

	public void setLanguageSkills(List<LanguageSkillApiDto> languageSkills) {
		this.languageSkills = languageSkills;
	}

	public Integer getDurationInDays() {
		return durationInDays;
	}

	public void setDurationInDays(Integer durationInDays) {
		this.durationInDays = durationInDays;
	}
}
