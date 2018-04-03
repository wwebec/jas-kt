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
    private int workingTimePercentageFrom;
    private int workingTimePercentageTo;
    private LocalDate startDate;
    private LocalDate endDate;
    private boolean startsImmediately;
    private boolean permanent;
    @Valid
    private LegacyLocationDto location;
    private List<LegacyLanguageSkillDto> languageSkills;

    protected LegacyJobDto() {
        // For reflection libs
    }

    public LegacyJobDto(String title, String description, int workingTimePercentageFrom, int workingTimePercentageTo, boolean startsImmediately, LocalDate startDate, boolean permanent, LocalDate endDate, List<LegacyLanguageSkillDto> languageSkills, LegacyLocationDto location) {
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

    public int getWorkingTimePercentageFrom() {
        return workingTimePercentageFrom;
    }

    public void setWorkingTimePercentageFrom(int workingTimePercentageFrom) {
        this.workingTimePercentageFrom = workingTimePercentageFrom;
    }

    public int getWorkingTimePercentageTo() {
        return workingTimePercentageTo;
    }

    public void setWorkingTimePercentageTo(int workingTimePercentageTo) {
        this.workingTimePercentageTo = workingTimePercentageTo;
    }

    public boolean isStartsImmediately() {
        return startsImmediately;
    }

    public void setStartsImmediately(boolean startsImmediately) {
        this.startsImmediately = startsImmediately;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public boolean isPermanent() {
        return permanent;
    }

    public void setPermanent(boolean permanent) {
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
