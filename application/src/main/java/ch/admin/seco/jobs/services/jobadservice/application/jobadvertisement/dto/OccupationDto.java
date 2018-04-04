package ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto;

import java.util.List;
import java.util.stream.Collectors;

import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.Occupation;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.WorkExperience;

import javax.validation.constraints.NotEmpty;

public class OccupationDto {

    @NotEmpty
    private String avamOccupationCode;
    private WorkExperience workExperience;
    private String educationCode;

    protected OccupationDto() {
        // For reflection libs
    }

    public OccupationDto(String avamOccupationCode, WorkExperience workExperience, String educationCode) {
        this.avamOccupationCode = avamOccupationCode;
        this.workExperience = workExperience;
        this.educationCode = educationCode;
    }

    public static OccupationDto toDto(Occupation occupation) {
        OccupationDto occupationDto = new OccupationDto();
        occupationDto.setAvamOccupationCode(occupation.getAvamOccupationCode());
        occupationDto.setWorkExperience(occupation.getWorkExperience());
        occupationDto.setEducationCode(occupation.getEducationCode());
        return occupationDto;
    }

    public String getAvamOccupationCode() {
        return avamOccupationCode;
    }

    public WorkExperience getWorkExperience() {
        return workExperience;
    }

    public void setWorkExperience(WorkExperience workExperience) {
        this.workExperience = workExperience;
    }

    public void setAvamOccupationCode(String avamOccupationCode) {
        this.avamOccupationCode = avamOccupationCode;
    }

    public String getEducationCode() {
        return educationCode;
    }

    public void setEducationCode(String educationCode) {
        this.educationCode = educationCode;
    }

    public static List<OccupationDto> toDto(List<Occupation> occupations) {
        if(occupations != null) {
            return occupations.stream().map(OccupationDto::toDto).collect(Collectors.toList());
        }
        return null;
    }
}
