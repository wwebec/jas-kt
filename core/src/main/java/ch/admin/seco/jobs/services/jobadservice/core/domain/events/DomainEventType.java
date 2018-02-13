package ch.admin.seco.jobs.services.jobadservice.core.domain.events;

import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class DomainEventType {

    private String value;

    public DomainEventType(String value) {
        this.value = value;
    }

    protected DomainEventType() {
        // FOR JPA
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
        if (!(o instanceof DomainEventType)) return false;
        DomainEventType that = (DomainEventType) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public String toString() {
        return "DomainEventType{" +
                "value=" + value +
                '}';
    }


}
