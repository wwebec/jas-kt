package ch.admin.seco.jobs.services.jobadservice.infrastructure.service.reference.jobcenter;

import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Component;

@Component
class JobCenterApiClientFallback implements JobCenterApiClient {

    @Override
    public JobCenterResource searchJobCenterByLocation(String countryCode, String postalCode) {
        return null;
    }

    @Override
    public JobCenterResource searchJobCenterByCode(String code) {
        return null;
    }

    @Override
    public List<JobCenterResource> findAllJobCenters() {
        return Collections.emptyList();
    }

}
