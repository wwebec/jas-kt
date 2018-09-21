package ch.admin.seco.jobs.services.jobadservice.infrastructure.service.reference.location;

import java.util.Optional;
import java.util.UUID;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "referenceservice", url = "${feign.referenceservice.url:}", fallback = LocationApiClientFallback.class, decode404 = true)
public interface LocationApiClient {

    @GetMapping("/api/localities/{id}")
    LocationResource getLocationById(@PathVariable("id") UUID id);

    @Cacheable("location")
    @GetMapping(value = "/api/localities")
    Optional<LocationResource> findLocationByPostalCodeAndCity(@RequestParam("zipCode") String zipCode, @RequestParam("city") String city);
}
