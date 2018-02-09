package ch.admin.seco.jobs.services.jobadservice.core.domain;

public interface ValueObject<T> {

    boolean sameValueObjectAs(T other);

}
