package ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement;

import ch.admin.seco.jobs.services.jobadservice.core.domain.ValueObject;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Embeddable;
import java.time.LocalDate;
import java.util.Objects;

@Embeddable
@Access(AccessType.FIELD)
public class Publication implements ValueObject<Publication> {

    private LocalDate startDate;

    private LocalDate endDate;

    private boolean euresDisplay;

    private boolean euresAnonymous;

    private boolean publicDisplay;

    private boolean restrictedDisplay;

    private boolean companyAnonymous;

    protected Publication() {
        // For reflection libs
    }

    public Publication(Builder builder) {
        this.startDate = builder.startDate;
        this.endDate = builder.endDate;
        this.euresDisplay = builder.euresDisplay;
        this.euresAnonymous = builder.euresAnonymous;
        this.publicDisplay = builder.publicDisplay;
        this.restrictedDisplay = builder.restrictedDisplay;
        this.companyAnonymous = builder.companyAnonymous;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public boolean isEuresDisplay() {
        return euresDisplay;
    }

    void setEuresDisplay(boolean euresDisplay) {
        this.euresDisplay = euresDisplay;
    }

    public boolean isEuresAnonymous() {
        return euresAnonymous;
    }

    void setEuresAnonymous(boolean euresAnonymous) {
        this.euresAnonymous = euresAnonymous;
    }

    public boolean isPublicDisplay() {
        return publicDisplay;
    }

    void setPublicDisplay(boolean publicDisplay) {
        this.publicDisplay = publicDisplay;
    }

    public boolean isRestrictedDisplay() {
        return restrictedDisplay;
    }

    void setRestrictedDisplay(boolean restrictedDisplay) {
        this.restrictedDisplay = restrictedDisplay;
    }

    public boolean isCompanyAnonymous() {
        return companyAnonymous;
    }

    void setCompanyAnonymous(boolean companyAnonymous) {
        this.companyAnonymous = companyAnonymous;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Publication that = (Publication) o;
        return euresDisplay == that.euresDisplay &&
                euresAnonymous == that.euresAnonymous &&
                publicDisplay == that.publicDisplay &&
                restrictedDisplay == that.restrictedDisplay &&
                companyAnonymous == that.companyAnonymous &&
                Objects.equals(startDate, that.startDate) &&
                Objects.equals(endDate, that.endDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(startDate, endDate, euresDisplay, euresAnonymous, publicDisplay, restrictedDisplay, companyAnonymous);
    }

    @Override
    public String toString() {
        return "Publication{" +
                "startDate=" + startDate +
                ", endDate=" + endDate +
                ", euresDisplay=" + euresDisplay +
                ", euresAnonymous=" + euresAnonymous +
                ", publicDisplay=" + publicDisplay +
                ", restrictedDisplay=" + restrictedDisplay +
                ", companyAnonymous=" + companyAnonymous +
                '}';
    }

    public static final class Builder {
        private LocalDate startDate;
        private LocalDate endDate;
        private boolean euresDisplay;
        private boolean euresAnonymous;
        private boolean publicDisplay;
        private boolean restrictedDisplay;
        private boolean companyAnonymous;

        public Builder() {
        }

        public Publication build() {
            return new Publication(this);
        }

        public Builder setStartDate(LocalDate startDate) {
            this.startDate = startDate;
            return this;
        }

        public Builder setEndDate(LocalDate endDate) {
            this.endDate = endDate;
            return this;
        }

        public Builder setEuresDisplay(boolean euresDisplay) {
            this.euresDisplay = euresDisplay;
            return this;
        }

        public Builder setEuresAnonymous(boolean euresAnonymous) {
            this.euresAnonymous = euresAnonymous;
            return this;
        }

        public Builder setPublicDisplay(boolean publicDisplay) {
            this.publicDisplay = publicDisplay;
            return this;
        }

        public Builder setRestrictedDisplay(boolean restrictedDisplay) {
            this.restrictedDisplay = restrictedDisplay;
            return this;
        }

        public Builder setCompanyAnonymous(boolean companyAnonymous) {
            this.companyAnonymous = companyAnonymous;
            return this;
        }
    }
}