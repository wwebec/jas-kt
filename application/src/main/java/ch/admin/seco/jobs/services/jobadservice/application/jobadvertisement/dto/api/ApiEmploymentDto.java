package ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.api;

import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.Employment;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.WorkForm;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.Set;

public class ApiEmploymentDto {

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

    protected ApiEmploymentDto() {
        // For reflection libs
    }

    public ApiEmploymentDto(LocalDate startDate, LocalDate endDate, boolean shortEmployment, boolean immediately, boolean permanent, int workloadPercentageMin, int workloadPercentageMax, Set<WorkForm> workForms) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.shortEmployment = shortEmployment;
        this.immediately = immediately;
        this.permanent = permanent;
        this.workloadPercentageMin = workloadPercentageMin;
        this.workloadPercentageMax = workloadPercentageMax;
        this.workForms = workForms;
    }

    public static ApiEmploymentDto toDto(Employment employment) {
        ApiEmploymentDto employmentDto = new ApiEmploymentDto();
        employmentDto.setStartDate(employment.getStartDate());
        employmentDto.setEndDate(employment.getEndDate());
        employmentDto.setShortEmployment(employment.isShortEmployment());
        employmentDto.setImmediately(employment.isImmediately());
        employmentDto.setPermanent(employment.isPermanent());
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

    public boolean isShortEmployment() {
        return shortEmployment;
    }

    public void setShortEmployment(boolean shortEmployment) {
        this.shortEmployment = shortEmployment;
    }

    public boolean isImmediately() {
        return immediately;
    }

    public void setImmediately(boolean immediately) {
        this.immediately = immediately;
    }

    public boolean isPermanent() {
        return permanent;
    }

    public void setPermanent(boolean permanent) {
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
