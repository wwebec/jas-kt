package ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.x28;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.CompanyDto;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.ContactDto;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.EmploymentDto;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.LanguageSkillDto;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.OccupationDto;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.PublicContactDto;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.create.CreateLocationDto;

public class X28CreateJobAdvertisementDto {

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
    private X28ContactDto contact; // can be null in the domain

    @Valid
    @NotNull
    private EmploymentDto employment; // can not be null in the domain

    @Valid
    @NotNull
    private X28CompanyDto company; // can not be null in the domain

    @Valid
    private X28LocationDto location;  // can be null in the domain

    @Valid
    @NotEmpty
    private List<X28OccupationDto> occupations; // can not be null or empty in the domain

    private String professionCodes;

    @Valid
    private List<X28LanguageSkillDto> languageSkills; // can be null or empty in the domain

    private LocalDate publicationStartDate;

    private LocalDate publicationEndDate;

    private boolean companyAnonymous;

    public List<LanguageSkillDto> toLanguageSkillDtos() {
        if (this.languageSkills == null) {
            return Collections.emptyList();
        }
        return this.languageSkills.stream()
                .map(X28LanguageSkillDto::toLanguageSkillDto)
                .collect(Collectors.toList());
    }

    public ContactDto toContactDto() {
        if (contact == null) {
            return null;
        }
        return contact.toContactDto();
    }

    public PublicContactDto toPublicContactDto() {
        if (contact == null) {
            return null;
        }
        return contact.toPublicContactDto();
    }

    public CompanyDto toCompanyDto() {
        return company.toCompanyDto();
    }

    public CreateLocationDto toCreateLocationDto() {
        if (location == null) {
            return null;
        }

        return location.toCreateLocationDto();
    }

    public List<OccupationDto> toOccupationDtos() {
        if (occupations == null) {
            return Collections.emptyList();
        }

        return occupations
                .stream()
                .map(X28OccupationDto::toOccupationDto)
                .collect(Collectors.toList());
    }

    public String getStellennummerEgov() {
        return stellennummerEgov;
    }

    public X28CreateJobAdvertisementDto setStellennummerEgov(String stellennummerEgov) {
        this.stellennummerEgov = stellennummerEgov;
        return this;
    }

    public String getStellennummerAvam() {
        return stellennummerAvam;
    }

    public X28CreateJobAdvertisementDto setStellennummerAvam(String stellennummerAvam) {
        this.stellennummerAvam = stellennummerAvam;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public X28CreateJobAdvertisementDto setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public X28CreateJobAdvertisementDto setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getNumberOfJobs() {
        return numberOfJobs;
    }

    public X28CreateJobAdvertisementDto setNumberOfJobs(String numberOfJobs) {
        this.numberOfJobs = numberOfJobs;
        return this;
    }

    public String getFingerprint() {
        return fingerprint;
    }

    public X28CreateJobAdvertisementDto setFingerprint(String fingerprint) {
        this.fingerprint = fingerprint;
        return this;
    }

    public String getExternalUrl() {
        return externalUrl;
    }

    public X28CreateJobAdvertisementDto setExternalUrl(String externalUrl) {
        this.externalUrl = externalUrl;
        return this;
    }

    public String getJobCenterCode() {
        return jobCenterCode;
    }

    public X28CreateJobAdvertisementDto setJobCenterCode(String jobCenterCode) {
        this.jobCenterCode = jobCenterCode;
        return this;
    }

    public X28ContactDto getContact() {
        return contact;
    }

    public X28CreateJobAdvertisementDto setContact(X28ContactDto contact) {
        this.contact = contact;
        return this;
    }

    public EmploymentDto getEmployment() {
        return employment;
    }

    public X28CreateJobAdvertisementDto setEmployment(EmploymentDto employment) {
        this.employment = employment;
        return this;
    }

    public X28CompanyDto getCompany() {
        return company;
    }

    public X28CreateJobAdvertisementDto setCompany(X28CompanyDto company) {
        this.company = company;
        return this;
    }

    public X28LocationDto getLocation() {
        return location;
    }

    public X28CreateJobAdvertisementDto setLocation(X28LocationDto location) {
        this.location = location;
        return this;
    }

    public List<X28OccupationDto> getOccupations() {
        return occupations;
    }

    public X28CreateJobAdvertisementDto setOccupations(List<X28OccupationDto> occupations) {
        this.occupations = occupations;
        return this;
    }

    public String getProfessionCodes() {
        return professionCodes;
    }

    public X28CreateJobAdvertisementDto setProfessionCodes(String professionCodes) {
        this.professionCodes = professionCodes;
        return this;
    }

    public List<X28LanguageSkillDto> getLanguageSkills() {
        return languageSkills;
    }

    public X28CreateJobAdvertisementDto setLanguageSkills(List<X28LanguageSkillDto> languageSkills) {
        this.languageSkills = languageSkills;
        return this;
    }

    public LocalDate getPublicationStartDate() {
        return publicationStartDate;
    }

    public X28CreateJobAdvertisementDto setPublicationStartDate(LocalDate publicationStartDate) {
        this.publicationStartDate = publicationStartDate;
        return this;
    }

    public LocalDate getPublicationEndDate() {
        return publicationEndDate;
    }

    public X28CreateJobAdvertisementDto setPublicationEndDate(LocalDate publicationEndDate) {
        this.publicationEndDate = publicationEndDate;
        return this;
    }

    public boolean isCompanyAnonymous() {
        return companyAnonymous;
    }

    public X28CreateJobAdvertisementDto setCompanyAnonymous(boolean companyAnonymous) {
        this.companyAnonymous = companyAnonymous;
        return this;
    }
}
