package ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

public class CreateJobAdvertisementWebFormDto {

    private boolean eures;

    @NotNull
    private String title;

    @NotNull
    private String description;

    private LocalDate employmentStartDate;

    private LocalDate employmentEndDate;

    private Integer durationInDays;

    private Boolean immediately;

    private Boolean permanent;

    private int workloadPercentageMin;

    private int workloadPercentageMax;

    private String drivingLicenseLevel;

    private ApplyChannelDto applyChannel;

    @NotNull
    private CompanyDto company;

    @NotNull
    private ContactDto contact;

    @NotNull
    private LocalityDto locality;

    @NotNull
    private OccupationDto occupation;

    private String educationCode;

    private List<LanguageSkillDto> languageSkills;

    protected CreateJobAdvertisementWebFormDto() {
        // For reflection libs
    }

    public CreateJobAdvertisementWebFormDto(boolean eures, String title, String description, LocalDate employmentStartDate, LocalDate employmentEndDate, Integer durationInDays, Boolean immediately, Boolean permanent, int workloadPercentageMin, int workloadPercentageMax, String drivingLicenseLevel, ApplyChannelDto applyChannel, CompanyDto company, ContactDto contact, LocalityDto locality, OccupationDto occupation, String educationCode, List<LanguageSkillDto> languageSkills) {
        this.eures = eures;
        this.title = title;
        this.description = description;
        this.employmentStartDate = employmentStartDate;
        this.employmentEndDate = employmentEndDate;
        this.durationInDays = durationInDays;
        this.immediately = immediately;
        this.permanent = permanent;
        this.workloadPercentageMin = workloadPercentageMin;
        this.workloadPercentageMax = workloadPercentageMax;
        this.drivingLicenseLevel = drivingLicenseLevel;
        this.applyChannel = applyChannel;
        this.company = company;
        this.contact = contact;
        this.locality = locality;
        this.occupation = occupation;
        this.educationCode = educationCode;
        this.languageSkills = languageSkills;
    }

    public boolean isEures() {
        return eures;
    }

    public void setEures(boolean eures) {
        this.eures = eures;
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

    public LocalDate getEmploymentStartDate() {
        return employmentStartDate;
    }

    public void setEmploymentStartDate(LocalDate employmentStartDate) {
        this.employmentStartDate = employmentStartDate;
    }

    public LocalDate getEmploymentEndDate() {
        return employmentEndDate;
    }

    public void setEmploymentEndDate(LocalDate employmentEndDate) {
        this.employmentEndDate = employmentEndDate;
    }

    public Integer getDurationInDays() {
        return durationInDays;
    }

    public void setDurationInDays(Integer durationInDays) {
        this.durationInDays = durationInDays;
    }

    public Boolean getImmediately() {
        return immediately;
    }

    public void setImmediately(Boolean immediately) {
        this.immediately = immediately;
    }

    public Boolean getPermanent() {
        return permanent;
    }

    public void setPermanent(Boolean permanent) {
        this.permanent = permanent;
    }

    public int getWorkloadPercentageMin() {
        return workloadPercentageMin;
    }

    public void setWorkloadPercentageMin(int workloadPercentageMin) {
        this.workloadPercentageMin = workloadPercentageMin;
    }

    public int getWorkloadPercentageMax() {
        return workloadPercentageMax;
    }

    public void setWorkloadPercentageMax(int workloadPercentageMax) {
        this.workloadPercentageMax = workloadPercentageMax;
    }

    public String getDrivingLicenseLevel() {
        return drivingLicenseLevel;
    }

    public void setDrivingLicenseLevel(String drivingLicenseLevel) {
        this.drivingLicenseLevel = drivingLicenseLevel;
    }

    public ApplyChannelDto getApplyChannel() {
        return applyChannel;
    }

    public void setApplyChannel(ApplyChannelDto applyChannel) {
        this.applyChannel = applyChannel;
    }

    public CompanyDto getCompany() {
        return company;
    }

    public void setCompany(CompanyDto company) {
        this.company = company;
    }

    public ContactDto getContact() {
        return contact;
    }

    public void setContact(ContactDto contact) {
        this.contact = contact;
    }

    public LocalityDto getLocality() {
        return locality;
    }

    public void setLocality(LocalityDto locality) {
        this.locality = locality;
    }

    public OccupationDto getOccupation() {
        return occupation;
    }

    public void setOccupation(OccupationDto occupation) {
        this.occupation = occupation;
    }

    public String getEducationCode() {
        return educationCode;
    }

    public void setEducationCode(String educationCode) {
        this.educationCode = educationCode;
    }

    public List<LanguageSkillDto> getLanguageSkills() {
        return languageSkills;
    }

    public void setLanguageSkills(List<LanguageSkillDto> languageSkills) {
        this.languageSkills = languageSkills;
    }

}
