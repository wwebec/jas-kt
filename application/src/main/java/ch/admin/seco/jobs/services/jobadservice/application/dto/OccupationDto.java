package ch.admin.seco.jobs.services.jobadservice.application.dto;

import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.Occupation;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.WorkExperience;

import java.util.Set;
import java.util.stream.Collectors;

public class OccupationDto {

    private String professionId;
    private WorkExperience workExperience;

    protected OccupationDto() {
        // For reflection libs
    }

    public OccupationDto(String professionId, WorkExperience workExperience) {
        this.professionId = professionId;
        this.workExperience = workExperience;
    }

    public String getProfessionId() {
        return professionId;
    }

    public void setProfessionId(String professionId) {
        this.professionId = professionId;
    }

    public WorkExperience getWorkExperience() {
        return workExperience;
    }

    public void setWorkExperience(WorkExperience workExperience) {
        this.workExperience = workExperience;
    }

    public static OccupationDto toDto(Occupation occupation) {
        OccupationDto occupationDto = new OccupationDto();
        occupationDto.setProfessionId(occupation.getProfessionId().getValue());
        occupationDto.setWorkExperience(occupation.getWorkExperience());
        return occupationDto;
    }

    public static Set<OccupationDto> toDto(Set<Occupation> occupations) {
        return occupations.stream().map(OccupationDto::toDto).collect(Collectors.toSet());
    }
}
