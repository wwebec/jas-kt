package ch.admin.seco.jobs.services.jobadservice.domain.profession;

import ch.admin.seco.jobs.services.jobadservice.core.conditions.Condition;
import ch.admin.seco.jobs.services.jobadservice.core.domain.AggregateId;
import ch.admin.seco.jobs.services.jobadservice.core.domain.IdGenerator;

import java.util.Objects;

public class ProfessionId implements AggregateId<ProfessionId> {

    private final String value;

    public ProfessionId() {
        this(IdGenerator.timeBasedUUID().toString());
    }

    public ProfessionId(String value) {
        this.value = Condition.notBlank(value);
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean sameValueObjectAs(ProfessionId other) {
        return (other != null) && this.value.equals(other.value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProfessionId that = (ProfessionId) o;
        return sameValueObjectAs(that);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

}
