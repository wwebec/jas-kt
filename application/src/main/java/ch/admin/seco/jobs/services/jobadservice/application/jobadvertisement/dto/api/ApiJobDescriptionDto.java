package ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.api;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobDescription;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.utils.LanguageIsoCode;

public class ApiJobDescriptionDto {

    @LanguageIsoCode
    private String languageIsoCode;

    @NotBlank
    @Size(max=255)
    private String title;

    @Size(max=12000)
    private String description;

    protected ApiJobDescriptionDto() {
        // For reflection libs
    }

    public ApiJobDescriptionDto(String languageIsoCode, String title, String description) {
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

    public static ApiJobDescriptionDto toDto(JobDescription jobDescription) {
        ApiJobDescriptionDto jobDescriptionDto = new ApiJobDescriptionDto();
        jobDescriptionDto.setLanguageIsoCode(jobDescription.getLanguage().getLanguage());
        jobDescriptionDto.setTitle(jobDescription.getTitle());
        jobDescriptionDto.setDescription(jobDescription.getDescription());
        return jobDescriptionDto;
    }

    public static List<ApiJobDescriptionDto> toDto(List<JobDescription> jobDescriptions) {
        return jobDescriptions.stream().map(ApiJobDescriptionDto::toDto).collect(Collectors.toList());
    }
}
