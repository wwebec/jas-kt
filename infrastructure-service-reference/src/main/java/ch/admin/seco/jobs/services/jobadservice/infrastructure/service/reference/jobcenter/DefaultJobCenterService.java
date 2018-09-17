package ch.admin.seco.jobs.services.jobadservice.infrastructure.service.reference.jobcenter;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import ch.admin.seco.jobs.services.jobadservice.application.JobCenterService;
import ch.admin.seco.jobs.services.jobadservice.domain.jobcenter.JobCenter;
import ch.admin.seco.jobs.services.jobadservice.domain.jobcenter.JobCenterAddress;

@Service
class DefaultJobCenterService implements JobCenterService {

    private final JobCenterApiClient jobCenterApiClient;

    DefaultJobCenterService(JobCenterApiClient jobCenterApiClient) {
        this.jobCenterApiClient = jobCenterApiClient;
    }

    @Override
    public String findJobCenterCode(String countryCode, String postalCode) {
        JobCenterResource jobCenterResource = jobCenterApiClient.searchJobCenterByLocation(countryCode, postalCode);
        return (jobCenterResource != null) ? jobCenterResource.getCode() : null;
    }

    @Override
    public JobCenter findJobCenterByCode(String code) {
        JobCenterResource jobCenterResource = jobCenterApiClient.searchJobCenterByCode(code);
        if (jobCenterResource == null) {
            return null;
        }
        return toJobCenter(jobCenterResource);
    }

    @Override
    public List<JobCenter> findAllJobCenters() {
        return jobCenterApiClient.findAllJobCenters().stream()
                .map(this::toJobCenter)
                .collect(Collectors.toList());
    }

    private JobCenter toJobCenter(JobCenterResource jobCenterResource) {
        AddressResource jobCenterAddressResource = jobCenterResource.getAddress();
        return new JobCenter(
                jobCenterResource.getId(),
                jobCenterResource.getCode(),
                jobCenterResource.getEmail(),
                jobCenterResource.getPhone(),
                jobCenterResource.getFax(),
                jobCenterResource.isShowContactDetailsToPublic(),
                toJobCenterAddress(jobCenterAddressResource)
        );
    }

    private JobCenterAddress toJobCenterAddress(AddressResource jobCenterAddressResource) {
        return new JobCenterAddress(
                jobCenterAddressResource.getName(),
                jobCenterAddressResource.getCity(),
                jobCenterAddressResource.getStreet(),
                jobCenterAddressResource.getHouseNumber(),
                jobCenterAddressResource.getZipCode()
        );
    }
}
