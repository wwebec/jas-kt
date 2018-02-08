package ch.admin.seco.jobs.services.jobadservice.core.domain;


import java.util.List;

public interface TestDataProvider<T> {

    List<T> getTestdata();

}
