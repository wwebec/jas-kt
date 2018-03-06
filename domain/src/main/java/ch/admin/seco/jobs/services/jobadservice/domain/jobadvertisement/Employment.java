package ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement;

import java.time.LocalDate;
import java.util.Objects;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Embeddable;

import ch.admin.seco.jobs.services.jobadservice.core.domain.ValueObject;

@Embeddable
@Access(AccessType.FIELD)
public class Employment implements ValueObject<Employment> {

    private LocalDate startDate;
    private LocalDate endDate;
    private Integer durationInDays;
    private Boolean immediately;
    private Boolean permanent;
    private int workloadPercentageMin;
    private int workloadPercentageMax;

    protected Employment() {
        // For reflection libs
    }

    public Employment(LocalDate startDate, LocalDate endDate, Integer durationInDays, Boolean immediately, Boolean permanent, int workloadPercentageMin, int workloadPercentageMax) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.durationInDays = durationInDays;
        this.immediately = immediately;
        this.permanent = permanent;
        this.workloadPercentageMin = workloadPercentageMin;
        this.workloadPercentageMax = workloadPercentageMax;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public Integer getDurationInDays() {
        return durationInDays;
    }

    public Boolean getImmediately() {
        return immediately;
    }

    public Boolean getPermanent() {
        return permanent;
    }

    public int getWorkloadPercentageMin() {
        return workloadPercentageMin;
    }

    public int getWorkloadPercentageMax() {
        return workloadPercentageMax;
    }

    @Override
    public boolean sameValueObjectAs(Employment other) {
        return equals(other);
    }

    @Override
    public int hashCode() {
        return Objects.hash(startDate, endDate, durationInDays, immediately, permanent, workloadPercentageMin, workloadPercentageMax);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (o == null || getClass() != o.getClass()) { return false; }
        Employment that = (Employment) o;
        return workloadPercentageMin == that.workloadPercentageMin &&
                workloadPercentageMax == that.workloadPercentageMax &&
                Objects.equals(startDate, that.startDate) &&
                Objects.equals(endDate, that.endDate) &&
                Objects.equals(durationInDays, that.durationInDays) &&
                Objects.equals(immediately, that.immediately) &&
                Objects.equals(permanent, that.permanent);
    }

    @Override
    public String toString() {
        return "Employment{" +
                "startDate=" + startDate +
                ", endDate=" + endDate +
                ", durationInDays=" + durationInDays +
                ", immediately=" + immediately +
                ", permanent=" + permanent +
                ", workloadPercentageMin=" + workloadPercentageMin +
                ", workloadPercentageMax=" + workloadPercentageMax +
                '}';
    }
}
