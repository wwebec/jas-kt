package ch.admin.seco.jobs.services.jobadservice.infrastructure.service.reference.jobcenter;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "referenceservice", url = "${feign.referenceservice.url:}", fallback = JobCenterApiClientFallback.class, decode404 = true)
interface JobCenterApiClient {

    @GetMapping(value = "/api/job-centers/by-location")
    JobCenterResource searchJobCenterByLocation(
            @RequestParam("countryCode") String countryCode,
            @RequestParam("postalCode") String postalCode
    );

    @GetMapping(value = "/api/job-centers")
    JobCenterResource searchJobCenterByCode(@RequestParam("code") String code);

    @GetMapping(value = "/api/job-centers")
    List<JobCenterResource> findAllJobCenters();
}
