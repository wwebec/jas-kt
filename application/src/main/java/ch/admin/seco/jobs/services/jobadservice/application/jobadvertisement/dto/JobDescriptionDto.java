package ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto;

import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobDescription;
import org.springframework.beans.factory.annotation.Value;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class JobDescriptionDto {

    private String languageIsoCode;

    @NotBlank
    private String title;

    @NotBlank
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
