package ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement;

import java.util.Objects;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Embeddable;

import ch.admin.seco.jobs.services.jobadservice.core.conditions.Condition;
import ch.admin.seco.jobs.services.jobadservice.core.domain.AggregateId;
import ch.admin.seco.jobs.services.jobadservice.core.domain.IdGenerator;

@Embeddable
@Access(AccessType.FIELD)
public class JobAdvertisementId implements AggregateId<JobAdvertisementId> {

    private final String value;

    public JobAdvertisementId() {
        this(IdGenerator.timeBasedUUID().toString());
    }

    public JobAdvertisementId(String value) {
        this.value = Condition.notBlank(value);
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JobAdvertisementId that = (JobAdvertisementId) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return "JobAdvertisementId{" +
                "value='" + value + '\'' +
                '}';
    }
}
