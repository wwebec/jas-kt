package ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.api;

import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.Occupation;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.WorkExperience;

import javax.validation.constraints.NotEmpty;
import java.util.List;
import java.util.stream.Collectors;

public class ApiOccupationDto {

    @NotEmpty
    private String avamOccupationCode;
    private WorkExperience workExperience;
    private String educationCode;

    protected ApiOccupationDto() {
        // For reflection libs
    }

    public ApiOccupationDto(String avamOccupationCode, WorkExperience workExperience, String educationCode) {
        this.avamOccupationCode = avamOccupationCode;
        this.workExperience = workExperience;
        this.educationCode = educationCode;
    }

    public static ApiOccupationDto toDto(Occupation occupation) {
        ApiOccupationDto occupationDto = new ApiOccupationDto();
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

    public static List<ApiOccupationDto> toDto(List<Occupation> occupations) {
        if(occupations != null) {
            return occupations.stream().map(ApiOccupationDto::toDto).collect(Collectors.toList());
        }
        return null;
    }
}
