package ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto;

import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobContent;

import java.util.List;

public class JobContentDto {

    private String externalUrl;
    private List<JobDescriptionDto> jobDescriptions;
    private CompanyDto company;
    private EmployerDto employer;
    private EmploymentDto employment;
    private LocationDto location;
    private List<OccupationDto> occupations;
    private List<LanguageSkillDto> languageSkills;
    private ApplyChannelDto applyChannel;
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

    public EmployerDto getEmployer() {
        return employer;
    }

    public void setEmployer(EmployerDto employer) {
        this.employer = employer;
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
        jobContentDto.setJobDescriptions(JobDescriptionDto.toDto(jobContent.getJobDescriptions()));
        jobContentDto.setCompany(CompanyDto.toDto(jobContent.getCompany()));
        jobContentDto.setEmployer(EmployerDto.toDto(jobContent.getEmployer()));
        jobContentDto.setEmployment(EmploymentDto.toDto(jobContent.getEmployment()));
        jobContentDto.setLocation(LocationDto.toDto(jobContent.getLocation()));
        jobContentDto.setOccupations(OccupationDto.toDto(jobContent.getOccupations()));
        jobContentDto.setLanguageSkills(LanguageSkillDto.toDto(jobContent.getLanguageSkills()));
        jobContentDto.setApplyChannel(ApplyChannelDto.toDto(jobContent.getApplyChannel()));
        jobContentDto.setPublicContact(PublicContactDto.toDto(jobContent.getPublicContact()));
        return jobContentDto;
    }
}
