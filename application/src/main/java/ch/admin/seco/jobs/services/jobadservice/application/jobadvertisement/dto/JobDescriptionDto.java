package ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.constraints.NotBlank;

import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobDescription;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.utils.LanguageIsoCode;

public class JobDescriptionDto {

    // can be blank
    @LanguageIsoCode
    private String languageIsoCode;

    @NotBlank
    private String title;

    private String description;

    protected JobDescriptionDto() {
        // For reflection libs
    }

    public JobDescriptionDto(String languageIsoCode, String title, String description) {
        this.languageIsoCode = languageIsoCode;
        this.title = title;
        this.description = description;
    }

    public String getLanguageIsoCode() {
        return languageIsoCode;
    }

    public void setLanguageIsoCode(String languageIsoCode) {
        this.languageIsoCode = languageIsoCode;
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

    public static JobDescriptionDto toDto(JobDescription jobDescription) {
        JobDescriptionDto jobDescriptionDto = new JobDescriptionDto();
        jobDescriptionDto.setLanguageIsoCode(jobDescription.getLanguage().getLanguage());
        jobDescriptionDto.setTitle(jobDescription.getTitle());
        jobDescriptionDto.setDescription(jobDescription.getDescription());
        return jobDescriptionDto;
    }

    public static List<JobDescriptionDto> toDto(List<JobDescription> jobDescriptions) {
        return jobDescriptions.stream().map(JobDescriptionDto::toDto).collect(Collectors.toList());
    }
}
