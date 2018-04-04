package ch.admin.seco.jobs.services.jobadservice.infrastructure.service.reference.jobcenter;

import ch.admin.seco.jobs.services.jobadservice.application.JobCenterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DefaultJobCenterService implements JobCenterService {

    private final JobCenterApiClient jobCenterApiClient;

    @Autowired
    public DefaultJobCenterService(JobCenterApiClient jobCenterApiClient) {
        this.jobCenterApiClient = jobCenterApiClient;
    }

    @Override
    public String findJobCenterCode(String countryCode, String postalCode) {
        JobCenterResource jobCenterResource = jobCenterApiClient.searchJobCenterByLocation(countryCode, postalCode);
        return (jobCenterResource != null) ? jobCenterResource.getCode() : null;
    }

}
