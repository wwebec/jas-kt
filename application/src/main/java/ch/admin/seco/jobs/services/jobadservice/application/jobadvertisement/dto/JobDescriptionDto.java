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

    public String getLanguageIsoCode() {
        return languageIsoCode;
    }

    public JobDescriptionDto setLanguageIsoCode(String languageIsoCode) {
        this.languageIsoCode = languageIsoCode;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public JobDescriptionDto setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public JobDescriptionDto setDescription(String description) {
        this.description = description;
        return this;
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
