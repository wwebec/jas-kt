package ch.admin.seco.jobs.services.jobadservice.infrastructure.service.reference.location;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Component;

@Component
public class LocationApiClientFallback implements LocationApiClient {

    @Override
    public LocationResource getLocationById(UUID id) {
        return null;
    }

    @Override
    public Optional<LocationResource> findLocationByPostalCodeAndCity(String zipCode, String city) {
        return Optional.empty();
    }
}
