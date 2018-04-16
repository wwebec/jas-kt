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

    private boolean publicAnonymous;

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
        this.publicAnonymous = builder.publicAnonymous;
        this.restrictedDisplay = builder.restrictedDisplay;
        this.restrictedAnonymous = builder.restrictedAnonymous;
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

    public boolean isEures() {
        return eures;
    }

    void setEures(boolean eures) {
        this.eures = eures;
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

    public boolean isPublicAnonymous() {
        return publicAnonymous;
    }

    void setPublicAnonymous(boolean publicAnonymous) {
        this.publicAnonymous = publicAnonymous;
    }

    public boolean isRestrictedDisplay() {
        return restrictedDisplay;
    }

    void setRestrictedDisplay(boolean restrictedDisplay) {
        this.restrictedDisplay = restrictedDisplay;
    }

    public boolean isRestrictedAnonymous() {
        return restrictedAnonymous;
    }

    void setRestrictedAnonymous(boolean restrictedAnonymous) {
        this.restrictedAnonymous = restrictedAnonymous;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Publication that = (Publication) o;
        return eures == that.eures &&
                euresAnonymous == that.euresAnonymous &&
                publicDisplay == that.publicDisplay &&
                publicAnonymous == that.publicAnonymous &&
                restrictedDisplay == that.restrictedDisplay &&
                restrictedAnonymous == that.restrictedAnonymous &&
                Objects.equals(startDate, that.startDate) &&
                Objects.equals(endDate, that.endDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(startDate, endDate, eures, euresAnonymous, publicDisplay, publicAnonymous, restrictedDisplay, restrictedAnonymous);
    }

    @Override
    public String toString() {
        return "Publication{" +
                "startDate=" + startDate +
                ", endDate=" + endDate +
                ", eures=" + eures +
                ", euresAnonymous=" + euresAnonymous +
                ", publicDisplay=" + publicDisplay +
                ", publicAnonymous=" + publicAnonymous +
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
        private boolean publicAnonymous;
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

        public Builder setPublicAnonymous(boolean publicAnonymous) {
            this.publicAnonymous = publicAnonymous;
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