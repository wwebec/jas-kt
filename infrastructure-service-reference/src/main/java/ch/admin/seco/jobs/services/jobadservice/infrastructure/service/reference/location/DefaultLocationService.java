package ch.admin.seco.jobs.services.jobadservice.infrastructure.service.reference.location;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.stereotype.Service;

import ch.admin.seco.jobs.services.jobadservice.application.LocationService;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.GeoPoint;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.Location;

@Service
@EnableCaching
public class DefaultLocationService implements LocationService {

	private final LocationApiClient locationApiClient;
	private final LocationVerifier locationVerifier;

    @Autowired
    public DefaultLocationService(LocationApiClient locationApiClient, LocationVerifier locationVerifier) {
        this.locationApiClient = locationApiClient;
        this.locationVerifier = locationVerifier;
    }

	@Override
	public Location enrichCodes(Location location) {
		return locationVerifier.verifyLocation(location, locationApiClient::findLocationByPostalCodeAndCity).map(locationResource -> enrichLocationWithLocationResource(location, locationResource))
				.orElse(location);
	}

	@Override
	public boolean verifyLocation(Location location) {
		return locationVerifier.verifyLocation(location, locationApiClient::findLocationByPostalCodeAndCity).isPresent();
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
