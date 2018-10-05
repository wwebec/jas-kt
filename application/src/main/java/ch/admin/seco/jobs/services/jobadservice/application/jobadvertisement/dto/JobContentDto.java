package ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto;

import java.util.List;

import javax.validation.Valid;

import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobContent;

public class JobContentDto {

    private String externalUrl;

    private String numberOfJobs;

    @Valid
    private List<JobDescriptionDto> jobDescriptions;

    @Valid
    private CompanyDto company;

    @Valid
    private EmploymentDto employment;

    @Valid
    private LocationDto location;

    @Valid
    private List<OccupationDto> occupations;

    @Valid
    private List<LanguageSkillDto> languageSkills;

    @Valid
    private ApplyChannelDto applyChannel;

    @Valid
    private PublicContactDto publicContact;

    protected JobContentDto() {
        // For reflection libs
    }

    public String getExternalUrl() {
        return externalUrl;
    }

    public void setExternalUrl(String externalUrl) {
        this.externalUrl = externalUrl;
    }

    public String getNumberOfJobs() {
        return numberOfJobs;
    }

    public void setNumberOfJobs(String numberOfJobs) {
        this.numberOfJobs = numberOfJobs;
    }

    public List<JobDescriptionDto> getJobDescriptions() {
        return jobDescriptions;
    }

    public void setJobDescriptions(List<JobDescriptionDto> jobDescriptions) {
        this.jobDescriptions = jobDescriptions;
    }

    public CompanyDto getCompany() {
        return company;
    }

    public void setCompany(CompanyDto company) {
        this.company = company;
    }

    public EmploymentDto getEmployment() {
        return employment;
    }

    public void setEmployment(EmploymentDto employment) {
        this.employment = employment;
    }

    public LocationDto getLocation() {
        return location;
    }

    public void setLocation(LocationDto location) {
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

    public ApplyChannelDto getApplyChannel() {
        return applyChannel;
    }

    public void setApplyChannel(ApplyChannelDto applyChannel) {
        this.applyChannel = applyChannel;
    }

    public PublicContactDto getPublicContact() {
        return publicContact;
    }

    public void setPublicContact(PublicContactDto publicContact) {
        this.publicContact = publicContact;
    }

    public static JobContentDto toDto(JobContent jobContent) {
        JobContentDto jobContentDto = new JobContentDto();
        jobContentDto.setExternalUrl(jobContent.getExternalUrl());
        jobContentDto.setNumberOfJobs(jobContent.getNumberOfJobs());
        jobContentDto.setJobDescriptions(JobDescriptionDto.toDto(jobContent.getJobDescriptions()));
        jobContentDto.setCompany(CompanyDto.toDto(jobContent.getDisplayCompany()));
        jobContentDto.setEmployment(EmploymentDto.toDto(jobContent.getEmployment()));
        jobContentDto.setLocation(LocationDto.toDto(jobContent.getLocation()));
        jobContentDto.setOccupations(OccupationDto.toDto(jobContent.getOccupations()));
        jobContentDto.setLanguageSkills(LanguageSkillDto.toDto(jobContent.getLanguageSkills()));
        jobContentDto.setApplyChannel(ApplyChannelDto.toDto(jobContent.getDisplayApplyChannel()));
        jobContentDto.setPublicContact(PublicContactDto.toDto(jobContent.getPublicContact()));
        return jobContentDto;
    }
}
