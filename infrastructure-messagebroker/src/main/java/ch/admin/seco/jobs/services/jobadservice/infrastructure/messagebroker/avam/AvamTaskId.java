package ch.admin.seco.jobs.services.jobadservice.infrastructure.messagebroker.avam;

import ch.admin.seco.jobs.services.jobadservice.core.conditions.Condition;
import ch.admin.seco.jobs.services.jobadservice.core.domain.AggregateId;
import ch.admin.seco.jobs.services.jobadservice.core.domain.IdGenerator;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
@Access(AccessType.FIELD)
public class AvamTaskId implements AggregateId<AvamTaskId> {

    private final String value;

    public AvamTaskId() {
        this(IdGenerator.timeBasedUUID().toString());
    }

    public AvamTaskId(String value) {
        this.value = Condition.notBlank(value);
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public boolean sameValueObjectAs(AvamTaskId other) {
        return (other != null) && this.value.equals(other.value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AvamTaskId that = (AvamTaskId) o;
        return sameValueObjectAs(that);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return "AvamTaskId{" +
                "value='" + value + '\'' +
                '}';
    }
}
