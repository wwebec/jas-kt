package ch.admin.seco.jobs.services.jobadservice.infrastructure.service.reference.location;

import ch.admin.seco.jobs.services.jobadservice.application.LocationService;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.GeoPoint;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.Location;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DefaultLocationService implements LocationService {

    private static final String COUNTRY_ISO_CODE_SWITZERLAND = "CH";
    private static final String COUNTRY_ISO_CODE_LIECHTENSTEIN = "LI";

    private final LocationApiClient locationApiClient;

    @Autowired
    public DefaultLocationService(LocationApiClient locationApiClient) {
        this.locationApiClient = locationApiClient;
    }

    @Override
    public Location enrichCodes(Location location) {
        if ((location != null) && isManagedCountry(location.getCountryIsoCode())) {
            List<LocationResource> locationResources = locationApiClient.findLocationByPostalCode(location.getPostalCode());
            if ((locationResources != null) && !locationResources.isEmpty()) {
                LocationResource matchingLocationResource = locationResources.stream()
                        .filter(locationResource -> locationResource.getCity().equalsIgnoreCase(location.getCity()))
                        .findFirst().orElse(null);
                if (matchingLocationResource != null) {
                    return new Location.Builder()
                            .setRemarks(location.getRemarks())
                            .setCity(location.getCity())
                            .setPostalCode(location.getPostalCode())
                            .setCommunalCode(matchingLocationResource.getCommunalCode())
                            .setRegionCode(matchingLocationResource.getRegionCode())
                            .setCantonCode(matchingLocationResource.getCantonCode())
                            .setCountryIsoCode(location.getCountryIsoCode())
                            .setCoordinates(new GeoPoint(matchingLocationResource.getGeoPoint().getLongitude(), matchingLocationResource.getGeoPoint().getLatitude()))
                            .build();
                }
            }
        }
        return location;
    }

    private boolean isManagedCountry(String countryIsoCode) {
        return COUNTRY_ISO_CODE_SWITZERLAND.equalsIgnoreCase(countryIsoCode) || COUNTRY_ISO_CODE_LIECHTENSTEIN.equalsIgnoreCase(countryIsoCode);
    }
}
