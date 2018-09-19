package ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.create;

import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.CompanyDto;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.EmploymentDto;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.LanguageSkillDto;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.OccupationDto;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.x28.X28ContactDto;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

public class CreateJobAdvertisementFromX28Dto {

    private String stellennummerEgov;

    private String stellennummerAvam;

    @NotBlank
    private String title;

    @NotBlank
    private String description;

    private String numberOfJobs;

    @NotBlank
    private String fingerprint;

    private String externalUrl;

    private String jobCenterCode;

    @Valid
    private X28ContactDto contact;

    @Valid
    @NotNull
    private EmploymentDto employment;

    @Valid
    @NotNull
    private CompanyDto company;

    @Valid
    @NotNull
    private CreateLocationDto location;

    @Valid
    @NotEmpty
    private List<OccupationDto> occupations;

    private String professionCodes;

    @Valid
    private List<LanguageSkillDto> languageSkills;

    private LocalDate publicationStartDate;

    private LocalDate publicationEndDate;

    private boolean companyAnonymous;

    protected CreateJobAdvertisementFromX28Dto() {
        // For reflection libs
    }

    public CreateJobAdvertisementFromX28Dto(String stellennummerEgov, String stellennummerAvam, String title, String description, String numberOfJobs, String fingerprint, String externalUrl, String jobCenterCode, X28ContactDto contact, EmploymentDto employment, CompanyDto company, CreateLocationDto location, List<OccupationDto> occupations, String professionCodes, List<LanguageSkillDto> languageSkills, LocalDate publicationStartDate, LocalDate publicationEndDate, boolean companyAnonymous) {
        this.stellennummerEgov = stellennummerEgov;
        this.stellennummerAvam = stellennummerAvam;
        this.title = title;
        this.description = description;
        this.numberOfJobs = numberOfJobs;
        this.fingerprint = fingerprint;
        this.externalUrl = externalUrl;
        this.jobCenterCode = jobCenterCode;
        this.contact = contact;
        this.employment = employment;
        this.company = company;
        this.location = location;
        this.occupations = occupations;
        this.professionCodes = professionCodes;
        this.languageSkills = languageSkills;
        this.publicationStartDate = publicationStartDate;
        this.publicationEndDate = publicationEndDate;
        this.companyAnonymous = companyAnonymous;
    }

    public String getStellennummerEgov() {
        return stellennummerEgov;
    }

    public void setStellennummerEgov(String stellennummerEgov) {
        this.stellennummerEgov = stellennummerEgov;
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

    public String getNumberOfJobs() {
        return numberOfJobs;
    }

    public void setNumberOfJobs(String numberOfJobs) {
        this.numberOfJobs = numberOfJobs;
    }

    public String getFingerprint() {
        return fingerprint;
    }

    public void setFingerprint(String fingerprint) {
        this.fingerprint = fingerprint;
    }

    public String getExternalUrl() {
        return externalUrl;
    }

    public void setExternalUrl(String externalUrl) {
        this.externalUrl = externalUrl;
    }

    public String getJobCenterCode() {
        return jobCenterCode;
    }

    public void setJobCenterCode(String jobCenterCode) {
        this.jobCenterCode = jobCenterCode;
    }

    public X28ContactDto getContact() {
        return contact;
    }

    public void setContact(X28ContactDto contact) {
        this.contact = contact;
    }

    public EmploymentDto getEmployment() {
        return employment;
    }

    public void setEmployment(EmploymentDto employment) {
        this.employment = employment;
    }

    public CompanyDto getCompany() {
        return company;
    }

    public void setCompany(CompanyDto company) {
        this.company = company;
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

    public String getProfessionCodes() {
        return professionCodes;
    }

    public void setProfessionCodes(String professionCodes) {
        this.professionCodes = professionCodes;
    }

    public List<LanguageSkillDto> getLanguageSkills() {
        return languageSkills;
    }

    public void setLanguageSkills(List<LanguageSkillDto> languageSkills) {
        this.languageSkills = languageSkills;
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

    public boolean isCompanyAnonymous() {
        return companyAnonymous;
    }

    public void setCompanyAnonymous(boolean companyAnonymous) {
        this.companyAnonymous = companyAnonymous;
    }
}
