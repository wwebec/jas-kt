package ch.admin.seco.jobs.services.jobadservice.infrastructure.service.reference.jobcenter;

import org.springframework.stereotype.Component;

@Component
public class JobCenterApiClientFallback implements JobCenterApiClient {

    @Override
    public JobCenterResource searchJobCenterByLocation(String countryCode, String postalCode) {
        return null;
    }

}
