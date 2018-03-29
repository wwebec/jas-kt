package ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.create.legacy;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

public class LegacyJobDto {

    @NotNull
    private String title;
    @Valid
    private LegacyOccupationDto occupation;
    @NotNull
    private String description;
    @NotNull
    private int workingTimePercentageMin;
    @NotNull
    private int workingTimePercentageMax;
    @NotNull
    private boolean startsImmediately;
    private LocalDate startDate;
    @NotNull
    private boolean permanent;
    private LocalDate endDate;
    private LegacyDrivingLicenseLevelEnum drivingLicenseLevel;
    private List<LegacyLanguageSkillDto> languageSkills;
    @Valid
    private LegacyLocationDto location;

    protected LegacyJobDto() {
        // For reflection libs
    }

    public LegacyJobDto(String title, LegacyOccupationDto occupation, String description, int workingTimePercentageMin, int workingTimePercentageMax, boolean startsImmediately, LocalDate startDate, boolean permanent, LocalDate endDate, LegacyDrivingLicenseLevelEnum drivingLicenseLevel, List<LegacyLanguageSkillDto> languageSkills, LegacyLocationDto location) {
        this.title = title;
        this.occupation = occupation;
        this.description = description;
        this.workingTimePercentageMin = workingTimePercentageMin;
        this.workingTimePercentageMax = workingTimePercentageMax;
        this.startsImmediately = startsImmediately;
        this.startDate = startDate;
        this.permanent = permanent;
        this.endDate = endDate;
        this.drivingLicenseLevel = drivingLicenseLevel;
        this.languageSkills = languageSkills;
        this.location = location;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LegacyOccupationDto getOccupation() {
        return occupation;
    }

    public void setOccupation(LegacyOccupationDto occupation) {
        this.occupation = occupation;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getWorkingTimePercentageMin() {
        return workingTimePercentageMin;
    }

    public void setWorkingTimePercentageMin(int workingTimePercentageMin) {
        this.workingTimePercentageMin = workingTimePercentageMin;
    }

    public int getWorkingTimePercentageMax() {
        return workingTimePercentageMax;
    }

    public void setWorkingTimePercentageMax(int workingTimePercentageMax) {
        this.workingTimePercentageMax = workingTimePercentageMax;
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

    public LegacyDrivingLicenseLevelEnum getDrivingLicenseLevel() {
        return drivingLicenseLevel;
    }

    public void setDrivingLicenseLevel(LegacyDrivingLicenseLevelEnum drivingLicenseLevel) {
        this.drivingLicenseLevel = drivingLicenseLevel;
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
