package ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement;

import ch.admin.seco.jobs.services.jobadservice.core.conditions.Condition;
import ch.admin.seco.jobs.services.jobadservice.core.domain.ValueObject;

import java.util.Locale;
import java.util.Objects;

public class JobDescription implements ValueObject<JobDescription> {

    private Locale language;

    private String title;

    private String description;

    protected JobDescription() {
        // For reflection libs
    }

    public JobDescription(Locale language, String title, String description) {
        this.language = Condition.notNull(language, "Language can't be null");
        this.title = Condition.notBlank(title, "Title can't be blank");
        this.description = Condition.notBlank(description, "Description can't be blank");
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
}
