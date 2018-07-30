package ch.admin.seco.jobs.services.jobadservice.infrastructure.service.reference.location;

import static org.springframework.util.StringUtils.hasText;

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

    private final LocationApiClient locationApiClient;

    @Autowired
    public DefaultLocationService(LocationApiClient locationApiClient) {
        this.locationApiClient = locationApiClient;
    }

    @Override
    public Location enrichCodes(Location location) {
        if ((location != null) && hasText(location.getPostalCode()) && isManagedCountry(location.getCountryIsoCode())) {
            return locationApiClient.findLocationByPostalCodeAndCity(location.getPostalCode(), location.getCity()).map(matchingLocationResource -> new Location.Builder()
                    .setRemarks(location.getRemarks())
                    .setCity(location.getCity())
                    .setPostalCode(location.getPostalCode())
                    .setCommunalCode(matchingLocationResource.getCommunalCode())
                    .setRegionCode(matchingLocationResource.getRegionCode())
                    .setCantonCode(matchingLocationResource.getCantonCode())
                    .setCountryIsoCode(location.getCountryIsoCode())
                    .setCoordinates(getGeoPoint(matchingLocationResource))
                    .build()
            ).orElse(location);
        }
        return location;
    }

    private GeoPoint getGeoPoint(LocationResource matchingLocationResource) {
        if (matchingLocationResource.getGeoPoint() == null) {
            return null;
        }
        return new GeoPoint(matchingLocationResource.getGeoPoint().getLongitude(), matchingLocationResource.getGeoPoint().getLatitude());
    }

    private boolean isManagedCountry(String countryIsoCode) {
        return COUNTRY_ISO_CODE_SWITZERLAND.equalsIgnoreCase(countryIsoCode) || COUNTRY_ISO_CODE_LIECHTENSTEIN.equalsIgnoreCase(countryIsoCode);
    }
}
