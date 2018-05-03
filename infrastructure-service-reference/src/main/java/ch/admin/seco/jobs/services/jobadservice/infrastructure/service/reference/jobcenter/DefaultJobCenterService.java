package ch.admin.seco.jobs.services.jobadservice.infrastructure.service.reference.jobcenter;

import ch.admin.seco.jobs.services.jobadservice.application.JobCenterService;
import ch.admin.seco.jobs.services.jobadservice.domain.jobcenter.JobCenter;
import ch.admin.seco.jobs.services.jobadservice.domain.jobcenter.JobCenterAddress;
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

    @Override
    public JobCenter findJobCenterByCode(String code) {
        JobCenterResource jobCenterResource = jobCenterApiClient.searchJobCenterByCode(code);
        if (jobCenterResource != null) {
            AddressResource jobCenterAddressResource = jobCenterResource.getAddress();
            if (jobCenterAddressResource != null) {
                JobCenterAddress jobCenterAddress = new JobCenterAddress(
                        jobCenterAddressResource.getName(),
                        jobCenterAddressResource.getCity(),
                        jobCenterAddressResource.getStreet(),
                        jobCenterAddressResource.getHouseNumber(),
                        jobCenterAddressResource.getZipCode()
                );
                return new JobCenter(
                        jobCenterResource.getId(),
                        jobCenterResource.getCode(),
                        jobCenterResource.getEmail(),
                        jobCenterResource.getPhone(),
                        jobCenterResource.getFax(),
                        jobCenterResource.isShowContactDetailsToPublic(),
                        jobCenterAddress
                );
            }
        }
        return null;
    }
}
