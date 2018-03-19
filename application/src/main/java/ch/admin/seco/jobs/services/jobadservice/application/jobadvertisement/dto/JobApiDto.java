package ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;

public class JobApiDto {

    @NotBlank
    @Pattern(regexp = "[a-z]{2}")
    private String languageIsoCode;

    @NotBlank
    private String title;

    @NotBlank
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
    private CreateLocationDto location;

    @Size(max = 5)
    private List<LanguageSkillDto> languageSkills;

    protected JobApiDto() {
        // For reflection libs
    }

    public JobApiDto(String languageIsoCode,
                     String title,
                     String description,
                     Integer workingTimePercentageFrom,
                     Integer workingTimePercentageTo,
                     LocalDate startDate,
                     LocalDate endDate,
                     Integer durationInDays,
                     Boolean startsImmediately,
                     Boolean permanent,
                     CreateLocationDto location,
                     List<LanguageSkillDto> languageSkills) {
        this.languageIsoCode = languageIsoCode;
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

    public String getLanguageIsoCode() {
        return languageIsoCode;
    }

    public void setLanguageIsoCode(String languageIsoCode) {
        this.languageIsoCode = languageIsoCode;
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

    public CreateLocationDto getLocation() {
        return location;
    }

    public void setLocation(CreateLocationDto location) {
        this.location = location;
    }

    public List<LanguageSkillDto> getLanguageSkills() {
        return languageSkills;
    }

    public void setLanguageSkills(List<LanguageSkillDto> languageSkills) {
        this.languageSkills = languageSkills;
    }

    public Integer getDurationInDays() {
        return durationInDays;
    }

    public void setDurationInDays(Integer durationInDays) {
        this.durationInDays = durationInDays;
    }
}
