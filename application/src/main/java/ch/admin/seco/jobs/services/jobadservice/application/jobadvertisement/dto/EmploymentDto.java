package ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto;

import java.time.LocalDate;
import java.util.Set;

import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.Employment;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.WorkForm;

public class EmploymentDto {

    private LocalDate startDate;
    private LocalDate endDate;
    private boolean shortEmployment;
    private Boolean immediately;
    private Boolean permanent;
    private int workloadPercentageMin;
    private int workloadPercentageMax;
    private Set<WorkForm> workForms;

    protected EmploymentDto() {
        // For reflection libs
    }

    public EmploymentDto(LocalDate startDate, LocalDate endDate, boolean shortEmployment, Boolean immediately, Boolean permanent, int workloadPercentageMin, int workloadPercentageMax, Set<WorkForm> workForms) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.shortEmployment = shortEmployment;
        this.immediately = immediately;
        this.permanent = permanent;
        this.workloadPercentageMin = workloadPercentageMin;
        this.workloadPercentageMax = workloadPercentageMax;
        this.workForms = workForms;
    }

    public static EmploymentDto toDto(Employment employment) {
        EmploymentDto employmentDto = new EmploymentDto();
        employmentDto.setStartDate(employment.getStartDate());
        employmentDto.setEndDate(employment.getEndDate());
        employmentDto.setShortEmployment(employment.getShortEmployment());
        employmentDto.setImmediately(employment.getImmediately());
        employmentDto.setPermanent(employment.getPermanent());
        employmentDto.setWorkloadPercentageMin(employment.getWorkloadPercentageMin());
        employmentDto.setWorkloadPercentageMax(employment.getWorkloadPercentageMax());
        employmentDto.setWorkForms(employment.getWorkForms());
        return employmentDto;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public boolean getShortEmployment() {
        return shortEmployment;
    }

    public void setShortEmployment(boolean shortEmployment) {
        this.shortEmployment = shortEmployment;
    }

    public Boolean getImmediately() {
        return immediately;
    }

    public void setImmediately(Boolean immediately) {
        this.immediately = immediately;
    }

    public Boolean getPermanent() {
        return permanent;
    }

    public void setPermanent(Boolean permanent) {
        this.permanent = permanent;
    }

    public int getWorkloadPercentageMin() {
        return workloadPercentageMin;
    }

    public void setWorkloadPercentageMin(int workloadPercentageMin) {
        this.workloadPercentageMin = workloadPercentageMin;
    }

    public int getWorkloadPercentageMax() {
        return workloadPercentageMax;
    }

    public void setWorkloadPercentageMax(int workloadPercentageMax) {
        this.workloadPercentageMax = workloadPercentageMax;
    }

    public Set<WorkForm> getWorkForms() {
        return workForms;
    }

    public void setWorkForms(Set<WorkForm> workForms) {
        this.workForms = workForms;
    }
}
