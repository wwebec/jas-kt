package ch.admin.seco.jobs.services.jobadservice.infrastructure.service.reference.location;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Component;

@Component
public class LocationApiClientFallback implements LocationApiClient {

    @Override
    public LocationResource getLocationById(UUID id) {
        return null;
    }

    @Override
    public List<LocationResource> findLocationByPostalCode(String postalCode) {
        return Collections.emptyList();
    }
}
