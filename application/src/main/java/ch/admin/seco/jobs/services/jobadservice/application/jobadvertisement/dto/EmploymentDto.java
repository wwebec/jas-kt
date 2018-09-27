package ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto;

import java.time.LocalDate;
import java.util.Set;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.Employment;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.WorkForm;

public class EmploymentDto {

    private LocalDate startDate;

    private LocalDate endDate;

    private boolean shortEmployment;

    private boolean immediately;

    private boolean permanent;

    @Min(10)
    @Max(100)
    private int workloadPercentageMin;

    @Min(10)
    @Max(100)
    private int workloadPercentageMax;

    private Set<WorkForm> workForms;


    public LocalDate getStartDate() {
        return startDate;
    }

    public EmploymentDto setStartDate(LocalDate startDate) {
        this.startDate = startDate;
        return this;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public EmploymentDto setEndDate(LocalDate endDate) {
        this.endDate = endDate;
        return this;
    }

    public boolean isShortEmployment() {
        return shortEmployment;
    }

    public EmploymentDto setShortEmployment(boolean shortEmployment) {
        this.shortEmployment = shortEmployment;
        return this;
    }

    public boolean isImmediately() {
        return immediately;
    }

    public EmploymentDto setImmediately(boolean immediately) {
        this.immediately = immediately;
        return this;
    }

    public boolean isPermanent() {
        return permanent;
    }

    public EmploymentDto setPermanent(boolean permanent) {
        this.permanent = permanent;
        return this;
    }

    public int getWorkloadPercentageMin() {
        return workloadPercentageMin;
    }

    public EmploymentDto setWorkloadPercentageMin(int workloadPercentageMin) {
        this.workloadPercentageMin = workloadPercentageMin;
        return this;
    }

    public int getWorkloadPercentageMax() {
        return workloadPercentageMax;
    }

    public EmploymentDto setWorkloadPercentageMax(int workloadPercentageMax) {
        this.workloadPercentageMax = workloadPercentageMax;
        return this;
    }

    public Set<WorkForm> getWorkForms() {
        return workForms;
    }

    public EmploymentDto setWorkForms(Set<WorkForm> workForms) {
        this.workForms = workForms;
        return this;
    }

    public static EmploymentDto toDto(Employment employment) {
        return new EmploymentDto()
                .setStartDate(employment.getStartDate())
                .setEndDate(employment.getEndDate())
                .setShortEmployment(employment.isShortEmployment())
                .setImmediately(employment.isImmediately())
                .setPermanent(employment.isPermanent())
                .setWorkloadPercentageMin(employment.getWorkloadPercentageMin())
                .setWorkloadPercentageMax(employment.getWorkloadPercentageMax())
                .setWorkForms(employment.getWorkForms());
    }
}
