package ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.x28;

import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.Occupation;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.WorkExperience;

import java.util.List;
import java.util.stream.Collectors;

public class X28OccupationDto {

    private String avamOccupationCode;

    private WorkExperience workExperience;

    private String educationCode;

    protected X28OccupationDto() {
        // For reflection libs
    }

    public X28OccupationDto(String avamOccupationCode, WorkExperience workExperience, String educationCode) {
        this.avamOccupationCode = avamOccupationCode;
        this.workExperience = workExperience;
        this.educationCode = educationCode;
    }

    public static X28OccupationDto toDto(Occupation occupation) {
        X28OccupationDto occupationDto = new X28OccupationDto();
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

    public static List<X28OccupationDto> toDto(List<Occupation> occupations) {
        if (occupations != null) {
            return occupations.stream().map(X28OccupationDto::toDto).collect(Collectors.toList());
        }
        return null;
    }
}
