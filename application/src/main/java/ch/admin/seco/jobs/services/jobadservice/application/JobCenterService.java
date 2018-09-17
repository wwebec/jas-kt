package ch.admin.seco.jobs.services.jobadservice.application;

import java.util.List;

import ch.admin.seco.jobs.services.jobadservice.domain.jobcenter.JobCenter;

public interface JobCenterService {

    String findJobCenterCode(String countryCode, String postalCode);

    JobCenter findJobCenterByCode(String code);

    List<JobCenter> findAllJobCenters();
}
