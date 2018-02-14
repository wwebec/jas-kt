package ch.admin.seco.jobs.services.jobadservice.core.domain;

import java.io.Serializable;

public interface AggregateId<T> extends ValueObject<T>, Serializable {

    static String getSafelyValue(AggregateId aggregateId) {
        return (aggregateId != null) ? aggregateId.getValue() : null;
    }

    String getValue();

}
