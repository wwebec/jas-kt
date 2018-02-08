package ch.admin.seco.jobs.services.jobadservice.core.domain;

public interface AggregateId<T> extends ValueObject<T> {

    String getValue();

}
