package ch.admin.seco.jobs.services.jobadservice.infrastructure.service.reference.location;

import static org.apache.commons.lang3.StringUtils.upperCase;
import static org.springframework.util.StringUtils.hasText;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.stereotype.Service;

import ch.admin.seco.jobs.services.jobadservice.application.LocationService;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.GeoPoint;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.Location;

@Service
@EnableCaching
public class DefaultLocationService implements LocationService {

    private static final String COUNTRY_ISO_CODE_SWITZERLAND = "CH";

    private static final String COUNTRY_ISO_CODE_LIECHTENSTEIN = "LI";

    private static final Set<String> MANAGED_COUNTRY_CODES = Collections.unmodifiableSet(new HashSet<>(Arrays.asList(
            COUNTRY_ISO_CODE_LIECHTENSTEIN,
            COUNTRY_ISO_CODE_SWITZERLAND
    )));

    private final LocationApiClient locationApiClient;

    @Autowired
    public DefaultLocationService(LocationApiClient locationApiClient) {
        this.locationApiClient = locationApiClient;
    }

    @Override
    public Location enrichCodes(Location location) {
        if (!isManagedLocations(location)) {
            return location;
        }
        return locationApiClient.findLocationByPostalCodeAndCity(location.getPostalCode(), location.getCity())
                .map(locationResource -> enrichLocationWithLocationResource(location, locationResource))
                .orElse(location);

    }

    @Override
    public boolean verifyLocation(Location location) {
        if (isManagedLocations(location)) {
            return locationApiClient.findLocationByPostalCodeAndCity(location.getPostalCode(), location.getCity()).isPresent();
        }

        return false;
    }

    private boolean isManagedLocations(Location location) {
        return location != null
                && hasText(location.getPostalCode())
                && MANAGED_COUNTRY_CODES.contains(upperCase(location.getCountryIsoCode()));
    }

    private Location enrichLocationWithLocationResource(Location location, LocationResource resource) {
        return new Location.Builder()
                .setRemarks(location.getRemarks())
                .setCity(location.getCity())
                .setPostalCode(location.getPostalCode())
                .setCommunalCode(resource.getCommunalCode())
                .setRegionCode(resource.getRegionCode())
                .setCantonCode(resource.getCantonCode())
                .setCountryIsoCode(location.getCountryIsoCode())
                .setCoordinates(getGeoPoint(resource))
                .build();
    }

    private GeoPoint getGeoPoint(LocationResource matchingLocationResource) {
        if (matchingLocationResource.getGeoPoint() == null) {
            return null;
        }
        return new GeoPoint(matchingLocationResource.getGeoPoint().getLongitude(), matchingLocationResource.getGeoPoint().getLatitude());
    }
}
