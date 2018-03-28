package ch.admin.seco.jobs.services.jobadservice.core.domain.events;

import ch.admin.seco.jobs.services.jobadservice.core.conditions.Condition;
import ch.admin.seco.jobs.services.jobadservice.core.domain.AggregateId;
import ch.admin.seco.jobs.services.jobadservice.core.domain.IdGenerator;

import java.util.Objects;

public class DomainEventId implements AggregateId<DomainEventId> {

    private final String value;

    public DomainEventId() {
        this(IdGenerator.timeBasedUUID().toString());
    }

    public DomainEventId(String value) {
        this.value = Condition.notBlank(value);
    }

    public String getValue() {
        return value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DomainEventId that = (DomainEventId) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public String toString() {
        return "DomainEventId{" +
                "value=" + value +
                '}';
    }
}
