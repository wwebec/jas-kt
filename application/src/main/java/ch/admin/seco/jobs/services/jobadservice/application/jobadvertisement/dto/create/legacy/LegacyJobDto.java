package ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.create.legacy;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

public class LegacyJobDto {

    @NotNull
    private String title;
    @NotNull
    private String description;
    private Integer workingTimePercentageFrom;
    private Integer workingTimePercentageTo;
    private LocalDate startDate;
    private LocalDate endDate;
    private Boolean startsImmediately;
    private Boolean permanent;
    @Valid
    private LegacyLocationDto location;
    private List<LegacyLanguageSkillDto> languageSkills;

    protected LegacyJobDto() {
        // For reflection libs
    }

    public LegacyJobDto(String title, String description, Integer workingTimePercentageFrom, Integer workingTimePercentageTo, Boolean startsImmediately, LocalDate startDate, Boolean permanent, LocalDate endDate, List<LegacyLanguageSkillDto> languageSkills, LegacyLocationDto location) {
        this.title = title;
        this.description = description;
        this.workingTimePercentageFrom = workingTimePercentageFrom;
        this.workingTimePercentageTo = workingTimePercentageTo;
        this.startsImmediately = startsImmediately;
        this.startDate = startDate;
        this.permanent = permanent;
        this.endDate = endDate;
        this.languageSkills = languageSkills;
        this.location = location;
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

    public Boolean isStartsImmediately() {
        return startsImmediately;
    }

    public void setStartsImmediately(Boolean startsImmediately) {
        this.startsImmediately = startsImmediately;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public Boolean isPermanent() {
        return permanent;
    }

    public void setPermanent(Boolean permanent) {
        this.permanent = permanent;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public List<LegacyLanguageSkillDto> getLanguageSkills() {
        return languageSkills;
    }

    public void setLanguageSkills(List<LegacyLanguageSkillDto> languageSkills) {
        this.languageSkills = languageSkills;
    }

    public LegacyLocationDto getLocation() {
        return location;
    }

    public void setLocation(LegacyLocationDto location) {
        this.location = location;
    }
}
