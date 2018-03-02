package ch.admin.seco.jobs.services.jobadservice.infrastructure.service.reference.locality;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import feign.hystrix.FallbackFactory;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "localities", fallback = LocalityApiClientFactory.class, decode404 = true)
public interface LocalityApiClient {

    @GetMapping("/api/localities/{id}")
    LocalityResource getLocality(@PathVariable("id") UUID id);

    @GetMapping(value = "/api/localities")
    List<LocalityResource> findLocalitiesByZipCode(@RequestParam("zipCode") String zipCode);

}

@Component
class LocalityApiClientFactory implements FallbackFactory<LocalityApiClient> {

    @Override
    public LocalityApiClient create(Throwable cause) {
        return new LocalityApiClient() {
            @Override
            public LocalityResource getLocality(UUID id) {
                return null;
            }

            @Override
            public List<LocalityResource> findLocalitiesByZipCode(String zipCode) {
                return Collections.emptyList();
            }
        };
    }
}
