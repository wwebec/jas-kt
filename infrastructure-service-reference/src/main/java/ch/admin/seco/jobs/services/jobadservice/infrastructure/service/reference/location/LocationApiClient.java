package ch.admin.seco.jobs.services.jobadservice.infrastructure.service.reference.location;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "localities", fallback = LocationApiClientFallback.class, decode404 = true)
public interface LocationApiClient {

    @GetMapping("/api/localities/{id}")
    LocationResource getLocationById(@PathVariable("id") UUID id);

    @GetMapping(value = "/api/localities")
    List<LocationResource> findLocationByPostalCode(@RequestParam("zipCode") String postalCode);

}

@Component
class LocationApiClientFallback implements LocationApiClient {

    @Override
    public LocationResource getLocationById(UUID id) {
        return null;
    }

    @Override
    public List<LocationResource> findLocationByPostalCode(String postalCode) {
        if(postalCode.equalsIgnoreCase("3000")) {
            return Collections.singletonList(
                    new LocationResource(
                            null,
                            "Bern",
                            "3000",
                            "351",
                            "BE",
                            "BE01",
                            new GeoPointResource(46.9480900, 7.4474400)
                    )
            );
        }
        return Collections.emptyList();
    }
}
