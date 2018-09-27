package ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.constraints.NotEmpty;

import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.Occupation;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.WorkExperience;

public class OccupationDto {

    @NotEmpty
    private String avamOccupationCode;

    private WorkExperience workExperience;

    private String educationCode;

    public String getAvamOccupationCode() {
        return avamOccupationCode;
    }

    public OccupationDto setAvamOccupationCode(String avamOccupationCode) {
        this.avamOccupationCode = avamOccupationCode;
        return this;
    }

    public WorkExperience getWorkExperience() {
        return workExperience;
    }

    public OccupationDto setWorkExperience(WorkExperience workExperience) {
        this.workExperience = workExperience;
        return this;
    }

    public String getEducationCode() {
        return educationCode;
    }

    public OccupationDto setEducationCode(String educationCode) {
        this.educationCode = educationCode;
        return this;
    }

    public static OccupationDto toDto(Occupation occupation) {
        return new OccupationDto()
                .setAvamOccupationCode(occupation.getAvamOccupationCode())
                .setWorkExperience(occupation.getWorkExperience())
                .setEducationCode(occupation.getEducationCode());
    }

    public static List<OccupationDto> toDto(List<Occupation> occupations) {
        if (occupations != null) {
            return occupations.stream().map(OccupationDto::toDto).collect(Collectors.toList());
        }
        return null;
    }
}
