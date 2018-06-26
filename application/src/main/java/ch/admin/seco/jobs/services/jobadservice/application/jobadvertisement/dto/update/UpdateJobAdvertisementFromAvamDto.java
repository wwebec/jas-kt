package ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.update;

import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.*;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.create.CreateLocationDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;
import java.util.List;

public class UpdateJobAdvertisementFromAvamDto {

    @NotEmpty
    private String stellennummerAvam;

    @NotEmpty
    private String title;

    @NotEmpty
    private String description;

    @NotBlank
    @Pattern(regexp = "[a-z]{2}")
    private String languageIsoCode;

    private boolean reportingObligation;

    private LocalDate reportingObligationEndDate;

    private String jobCenterCode;

    @NotNull
    private LocalDate approvalDate;

    @NotNull
    private EmploymentDto employment;

    private ApplyChannelDto applyChannel;

    @NotNull
    private CompanyDto company;

    @NotNull
    private ContactDto contact;

    @NotNull
    private CreateLocationDto location;

    @NotEmpty
    private List<OccupationDto> occupations;

    private List<LanguageSkillDto> languageSkills;

    @NotNull
    private PublicationDto publication;

    protected UpdateJobAdvertisementFromAvamDto() {
        // For reflection libs
    }

    public UpdateJobAdvertisementFromAvamDto(String stellennummerAvam, String title,
                                             String description, String languageIsoCode, boolean reportingObligation, LocalDate reportingObligationEndDate, String jobCenterCode, LocalDate approvalDate, EmploymentDto employment,
                                             ApplyChannelDto applyChannel, CompanyDto company, ContactDto contact, CreateLocationDto location,
                                             List<OccupationDto> occupations, List<LanguageSkillDto> languageSkills, PublicationDto publication) {
        this.stellennummerAvam = stellennummerAvam;
        this.title = title;
        this.description = description;
        this.languageIsoCode = languageIsoCode;
        this.reportingObligation = reportingObligation;
        this.reportingObligationEndDate = reportingObligationEndDate;
        this.jobCenterCode = jobCenterCode;
        this.approvalDate = approvalDate;
        this.employment = employment;
        this.applyChannel = applyChannel;
        this.company = company;
        this.contact = contact;
        this.location = location;
        this.occupations = occupations;
        this.languageSkills = languageSkills;
        this.publication = publication;
    }

    public String getStellennummerAvam() {
        return stellennummerAvam;
    }

    public void setStellennummerAvam(String stellennummerAvam) {
        this.stellennummerAvam = stellennummerAvam;
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

    public String getLanguageIsoCode() {
        return languageIsoCode;
    }

    public void setLanguageIsoCode(String languageIsoCode) {
        this.languageIsoCode = languageIsoCode;
    }

    public boolean isReportingObligation() {
        return reportingObligation;
    }

    public void setReportingObligation(boolean reportingObligation) {
        this.reportingObligation = reportingObligation;
    }

    public LocalDate getReportingObligationEndDate() {
        return reportingObligationEndDate;
    }

    public void setReportingObligationEndDate(LocalDate reportingObligationEndDate) {
        this.reportingObligationEndDate = reportingObligationEndDate;
    }

    public String getJobCenterCode() {
        return jobCenterCode;
    }

    public void setJobCenterCode(String jobCenterCode) {
        this.jobCenterCode = jobCenterCode;
    }

    public LocalDate getApprovalDate() {
        return approvalDate;
    }

    public void setApprovalDate(LocalDate approvalDate) {
        this.approvalDate = approvalDate;
    }

    public EmploymentDto getEmployment() {
        return employment;
    }

    public void setEmployment(EmploymentDto employment) {
        this.employment = employment;
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

    public CreateLocationDto getLocation() {
        return location;
    }

    public void setLocation(CreateLocationDto location) {
        this.location = location;
    }

    public List<OccupationDto> getOccupations() {
        return occupations;
    }

    public void setOccupations(List<OccupationDto> occupations) {
        this.occupations = occupations;
    }

    public List<LanguageSkillDto> getLanguageSkills() {
        return languageSkills;
    }

    public void setLanguageSkills(List<LanguageSkillDto> languageSkills) {
        this.languageSkills = languageSkills;
    }

    public PublicationDto getPublication() {
        return publication;
    }

    public void setPublication(PublicationDto publication) {
        this.publication = publication;
    }

}
