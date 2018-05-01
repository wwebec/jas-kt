package ch.admin.seco.jobs.services.jobadservice.infrastructure.service.reference.jobcenter;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "referenceservice", url = "${feign.referenceservice.url:}", fallback = JobCenterApiClientFallback.class, decode404 = true)
public interface JobCenterApiClient {

    @GetMapping(value = "/api/job-centers/by-location", consumes = "application/json")
    JobCenterResource searchJobCenterByLocation(
            @RequestParam("countryCode") String countryCode,
            @RequestParam("postalCode") String postalCode
    );

}
