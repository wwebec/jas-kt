package ch.admin.seco.jobs.services.jobadservice.core.domain;

public interface Aggregate<T, I extends AggregateId> {

    boolean isSameAggregateAs(T other);

}
