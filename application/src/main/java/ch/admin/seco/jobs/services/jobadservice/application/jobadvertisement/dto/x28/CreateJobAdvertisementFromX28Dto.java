package ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.x28;

import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.EmploymentDto;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.create.CreateLocationDto;

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
    private X28CompanyDto company;

    @Valid
    @NotNull
    private X28LocationDto location;

    @Valid
    @NotEmpty
    private List<X28OccupationDto> occupations;

    private String professionCodes;

    @Valid
    private List<X28LanguageSkillDto> languageSkills;

    private LocalDate publicationStartDate;

    private LocalDate publicationEndDate;

    private boolean companyAnonymous;

    public CreateJobAdvertisementFromX28Dto() {
        // For reflection libs
    }

    public CreateJobAdvertisementFromX28Dto(String stellennummerEgov, String stellennummerAvam, String title,
                                            String description, String numberOfJobs, String fingerprint,
                                            String externalUrl, String jobCenterCode, X28ContactDto contact,
                                            EmploymentDto employment, X28CompanyDto company, X28LocationDto location,
                                            List<X28OccupationDto> occupations, String professionCodes,
                                            List<X28LanguageSkillDto> languageSkills, LocalDate publicationStartDate,
                                            LocalDate publicationEndDate, boolean companyAnonymous) {

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

    public CreateJobAdvertisementFromX28Dto setStellennummerEgov(String stellennummerEgov) {
        this.stellennummerEgov = stellennummerEgov;
        return this;
    }

    public String getStellennummerAvam() {
        return stellennummerAvam;
    }

    public CreateJobAdvertisementFromX28Dto setStellennummerAvam(String stellennummerAvam) {
        this.stellennummerAvam = stellennummerAvam;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public CreateJobAdvertisementFromX28Dto setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public CreateJobAdvertisementFromX28Dto setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getNumberOfJobs() {
        return numberOfJobs;
    }

    public CreateJobAdvertisementFromX28Dto setNumberOfJobs(String numberOfJobs) {
        this.numberOfJobs = numberOfJobs;
        return this;
    }

    public String getFingerprint() {
        return fingerprint;
    }

    public CreateJobAdvertisementFromX28Dto setFingerprint(String fingerprint) {
        this.fingerprint = fingerprint;
        return this;
    }

    public String getExternalUrl() {
        return externalUrl;
    }

    public CreateJobAdvertisementFromX28Dto setExternalUrl(String externalUrl) {
        this.externalUrl = externalUrl;
        return this;
    }

    public String getJobCenterCode() {
        return jobCenterCode;
    }

    public CreateJobAdvertisementFromX28Dto setJobCenterCode(String jobCenterCode) {
        this.jobCenterCode = jobCenterCode;
        return this;
    }

    public X28ContactDto getContact() {
        return contact;
    }

    public CreateJobAdvertisementFromX28Dto setContact(X28ContactDto contact) {
        this.contact = contact;
        return this;
    }

    public EmploymentDto getEmployment() {
        return employment;
    }

    public CreateJobAdvertisementFromX28Dto setEmployment(EmploymentDto employment) {
        this.employment = employment;
        return this;
    }

    public X28CompanyDto getCompany() {
        return company;
    }

    public CreateJobAdvertisementFromX28Dto setCompany(X28CompanyDto company) {
        this.company = company;
        return this;
    }

    public X28LocationDto getLocation() {
        return location;
    }

    public CreateLocationDto getCreateLocationDto() {
        if(location == null) {
            return null;
        }

        return location.getCreateLocationDto();
    }


    public CreateJobAdvertisementFromX28Dto setLocation(X28LocationDto location) {
        this.location = location;
        return this;
    }

    public List<X28OccupationDto> getOccupations() {
        return occupations;
    }

    public CreateJobAdvertisementFromX28Dto setOccupations(List<X28OccupationDto> occupations) {
        this.occupations = occupations;
        return this;
    }

    public String getProfessionCodes() {
        return professionCodes;
    }

    public CreateJobAdvertisementFromX28Dto setProfessionCodes(String professionCodes) {
        this.professionCodes = professionCodes;
        return this;
    }

    public List<X28LanguageSkillDto> getLanguageSkills() {
        return languageSkills;
    }

    public CreateJobAdvertisementFromX28Dto setLanguageSkills(List<X28LanguageSkillDto> languageSkills) {
        this.languageSkills = languageSkills;
        return this;
    }

    public LocalDate getPublicationStartDate() {
        return publicationStartDate;
    }

    public CreateJobAdvertisementFromX28Dto setPublicationStartDate(LocalDate publicationStartDate) {
        this.publicationStartDate = publicationStartDate;
        return this;
    }

    public LocalDate getPublicationEndDate() {
        return publicationEndDate;
    }

    public CreateJobAdvertisementFromX28Dto setPublicationEndDate(LocalDate publicationEndDate) {
        this.publicationEndDate = publicationEndDate;
        return this;
    }

    public boolean isCompanyAnonymous() {
        return companyAnonymous;
    }

    public CreateJobAdvertisementFromX28Dto setCompanyAnonymous(boolean companyAnonymous) {
        this.companyAnonymous = companyAnonymous;
        return this;
    }
}
