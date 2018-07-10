package ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement;

import java.util.Locale;
import java.util.Objects;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Embeddable;

import ch.admin.seco.jobs.services.jobadservice.core.conditions.Condition;
import ch.admin.seco.jobs.services.jobadservice.core.domain.ValueObject;

@Embeddable
@Access(AccessType.FIELD)
public class JobDescription implements ValueObject<JobDescription> {

    private Locale language;

    private String title;

    private String description;

    protected JobDescription() {
        // For reflection libs
    }

    public JobDescription(Builder builder) {
        this.language = Condition.notNull(builder.language, "Language can't be null");
        this.title = Condition.notBlank(builder.title, "Title can't be blank");
        this.description = builder.description;
    }

    public Locale getLanguage() {
        return language;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    void updateTitleAndDescription(String title, String description) {
        this.title = Condition.notBlank(title, "Title can't be blank");
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JobDescription that = (JobDescription) o;
        return Objects.equals(language, that.language) &&
                Objects.equals(title, that.title) &&
                Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(language, title, description);
    }

    @Override
    public String toString() {
        return "JobDescription{" +
                "language=" + language +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

    public static final class Builder {
        private Locale language;
        private String title;
        private String description;

        public Builder() {
        }

        public JobDescription build() {
            return new JobDescription(this);
        }

        public Builder setLanguage(Locale language) {
            this.language = language;
            return this;
        }

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setDescription(String description) {
            this.description = description;
            return this;
        }
    }
}
