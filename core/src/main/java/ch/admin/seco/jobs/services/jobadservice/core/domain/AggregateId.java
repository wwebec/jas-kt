package ch.admin.seco.jobs.services.jobadservice.core.domain;

import java.io.Serializable;

public interface AggregateId<T> extends ValueObject<T>, Serializable {

    String getValue();

}
