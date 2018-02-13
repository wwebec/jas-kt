package ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement;

import ch.admin.seco.jobs.services.jobadservice.core.conditions.Condition;
import ch.admin.seco.jobs.services.jobadservice.core.domain.AggregateId;
import ch.admin.seco.jobs.services.jobadservice.core.domain.IdGenerator;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

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
    public boolean sameValueObjectAs(JobAdvertisementId other) {
        return (other != null) && this.value.equals(other.value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JobAdvertisementId that = (JobAdvertisementId) o;
        return sameValueObjectAs(that);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

}
