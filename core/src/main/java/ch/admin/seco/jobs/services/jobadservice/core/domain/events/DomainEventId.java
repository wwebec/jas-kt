package ch.admin.seco.jobs.services.jobadservice.core.domain.events;

import ch.admin.estv.voe.core.domain.common.IdGenerator;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;


@Embeddable
@Access(AccessType.FIELD)
public class DomainEventId implements Serializable {

    @Column(name = "ID")
    @Length(max = 36)
    private final String value;

    public DomainEventId() {
        this(IdGenerator.timeBasedUUID().toString());
    }

    public DomainEventId(String value) {
        this.value = value;
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
        if (!(o instanceof DomainEventId)) return false;
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
