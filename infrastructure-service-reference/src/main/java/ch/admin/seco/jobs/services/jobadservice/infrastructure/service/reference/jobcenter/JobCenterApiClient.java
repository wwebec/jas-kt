package ch.admin.seco.jobs.services.jobadservice.infrastructure.service.reference.jobcenter;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "referenceservice", fallback = JobCenterApiClientFallback.class, decode404 = true)
public interface JobCenterApiClient {

    @GetMapping(value = "/job-centers/by-location", consumes = "application/json")
    JobCenterResource searchJobCenterByLocation(
            @RequestParam("countryCode") String countryCode,
            @RequestParam("postalCode") String postalCode
    );

}

@Component
class JobCenterApiClientFallback implements JobCenterApiClient {

    @Override
    public JobCenterResource searchJobCenterByLocation(String countryCode, String postalCode) {
        if (countryCode.equalsIgnoreCase("ch") && postalCode.equalsIgnoreCase("3000")) {
            return new JobCenterResource(
                    "id",
                    "3000",
                    null,
                    null,
                    null,
                    false,
                    null
            );
        }
        return null;
    }

}
