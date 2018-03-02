package ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto;

import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.Occupation;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.WorkExperience;

import java.util.List;
import java.util.stream.Collectors;

public class OccupationDto {

    private String avamCode;
    private WorkExperience workExperience;
    private String educationCode;

    protected OccupationDto() {
        // For reflection libs
    }

    public OccupationDto(String avamCode, WorkExperience workExperience, String educationCode) {
        this.avamCode = avamCode;
        this.workExperience = workExperience;
        this.educationCode = educationCode;
    }

    public String getAvamCode() {
        return avamCode;
    }

    public void setAvamCode(String avamCode) {
        this.avamCode = avamCode;
    }

    public WorkExperience getWorkExperience() {
        return workExperience;
    }

    public void setWorkExperience(WorkExperience workExperience) {
        this.workExperience = workExperience;
    }

    public String getEducationCode() {
        return educationCode;
    }

    public void setEducationCode(String educationCode) {
        this.educationCode = educationCode;
    }

    public static OccupationDto toDto(Occupation occupation) {
        OccupationDto occupationDto = new OccupationDto();
        occupationDto.setAvamCode(occupation.getAvamCode());
        occupationDto.setWorkExperience(occupation.getWorkExperience());
        occupationDto.setEducationCode(occupation.getEducationCode());
        return occupationDto;
    }

    public static List<OccupationDto> toDto(List<Occupation> occupations) {
        return occupations.stream().map(OccupationDto::toDto).collect(Collectors.toList());
    }
}
