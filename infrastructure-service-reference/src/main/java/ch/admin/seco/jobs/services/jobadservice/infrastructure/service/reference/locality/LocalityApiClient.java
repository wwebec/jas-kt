package ch.admin.seco.jobs.services.jobadservice.infrastructure.service.reference.locality;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "localities", fallback = LocalityApiClientFallback.class, decode404 = true)
public interface LocalityApiClient {

    @GetMapping("/api/localities/{id}")
    LocalityResource getLocality(@PathVariable("id") UUID id);

    @GetMapping(value = "/api/localities")
    List<LocalityResource> findLocalitiesByPostalCode(@RequestParam("zipCode") String postalCode);

}

@Component
class LocalityApiClientFallback implements LocalityApiClient {

    @Override
    public LocalityResource getLocality(UUID id) {
        return null;
    }

    @Override
    public List<LocalityResource> findLocalitiesByPostalCode(String postalCode) {
        return Collections.emptyList();
    }
}
