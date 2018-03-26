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

    private boolean eures;

    private boolean euresAnonymous;

    private boolean publicDisplay;

    private boolean publicAnonynomous;

    private boolean restrictedDisplay;

    private boolean restrictedAnonymous;

    protected Publication() {
        // For reflection libs
    }

    public Publication(Builder builder) {
        this.startDate = builder.startDate;
        this.endDate = builder.endDate;
        this.eures = builder.eures;
        this.euresAnonymous = builder.euresAnonymous;
        this.publicDisplay = builder.publicDisplay;
        this.publicAnonynomous = builder.publicAnonynomous;
        this.restrictedDisplay = builder.restrictedDisplay;
        this.restrictedAnonymous = builder.restrictedAnonymous;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public boolean isEures() {
        return eures;
    }

    public boolean isEuresAnonymous() {
        return euresAnonymous;
    }

    public boolean isPublicDisplay() {
        return publicDisplay;
    }

    public boolean isPublicAnonynomous() {
        return publicAnonynomous;
    }

    public boolean isRestrictedDisplay() {
        return restrictedDisplay;
    }

    public boolean isRestrictedAnonymous() {
        return restrictedAnonymous;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Publication that = (Publication) o;
        return eures == that.eures &&
                euresAnonymous == that.euresAnonymous &&
                publicDisplay == that.publicDisplay &&
                publicAnonynomous == that.publicAnonynomous &&
                restrictedDisplay == that.restrictedDisplay &&
                restrictedAnonymous == that.restrictedAnonymous &&
                Objects.equals(startDate, that.startDate) &&
                Objects.equals(endDate, that.endDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(startDate, endDate, eures, euresAnonymous, publicDisplay, publicAnonynomous, restrictedDisplay, restrictedAnonymous);
    }

    @Override
    public String toString() {
        return "Publication{" +
                "startDate=" + startDate +
                ", endDate=" + endDate +
                ", eures=" + eures +
                ", euresAnonymous=" + euresAnonymous +
                ", publicDisplay=" + publicDisplay +
                ", publicAnonynomous=" + publicAnonynomous +
                ", restrictedDisplay=" + restrictedDisplay +
                ", restrictedAnonymous=" + restrictedAnonymous +
                '}';
    }

    public static final class Builder {
        private LocalDate startDate;
        private LocalDate endDate;
        private boolean eures;
        private boolean euresAnonymous;
        private boolean publicDisplay;
        private boolean publicAnonynomous;
        private boolean restrictedDisplay;
        private boolean restrictedAnonymous;

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

        public Builder setEures(boolean eures) {
            this.eures = eures;
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

        public Builder setPublicAnonynomous(boolean publicAnonynomous) {
            this.publicAnonynomous = publicAnonynomous;
            return this;
        }

        public Builder setRestrictedDisplay(boolean restrictedDisplay) {
            this.restrictedDisplay = restrictedDisplay;
            return this;
        }

        public Builder setRestrictedAnonymous(boolean restrictedAnonymous) {
            this.restrictedAnonymous = restrictedAnonymous;
            return this;
        }
    }
}