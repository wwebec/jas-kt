package ch.admin.seco.jobs.services.jobadservice.domain.profession;

import java.util.Objects;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Embeddable;

import ch.admin.seco.jobs.services.jobadservice.core.conditions.Condition;
import ch.admin.seco.jobs.services.jobadservice.core.domain.AggregateId;
import ch.admin.seco.jobs.services.jobadservice.core.domain.IdGenerator;

@Embeddable
@Access(AccessType.FIELD)
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProfessionId that = (ProfessionId) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
