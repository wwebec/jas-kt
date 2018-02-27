package ch.admin.seco.jobs.services.jobadservice.infrastructure.service.reference.locality;

import ch.admin.seco.jobs.services.jobadservice.application.LocalityService;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.GeoPoint;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.Locality;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DefaultLocalityService implements LocalityService {

    private static final String COUNTRY_ISO_CODE_SWITZERLAND = "CH";
    private static final String COUNTRY_ISO_CODE_LIECHTENSTEIN = "LI";

    private final LocalityApiClient localityApiClient;

    @Autowired
    public DefaultLocalityService(LocalityApiClient localityApiClient) {
        this.localityApiClient = localityApiClient;
    }

    @Override
    public Locality enrichCodes(Locality locality) {
        if ((locality != null) && isManagedCountry(locality.getCountryIsoCode())) {
            List<LocalityResource> localityResources = localityApiClient.findLocalitiesByZipCode(locality.getZipCode());
            if ((localityResources != null) && !localityResources.isEmpty()) {
                LocalityResource matchingLocalityResource = localityResources.stream()
                        .filter(localityResource -> localityResource.getCity().equalsIgnoreCase(locality.getCity()))
                        .findFirst().orElse(null);
                if (matchingLocalityResource != null) {
                    return new Locality(
                            locality.getRemarks(),
                            locality.getCity(),
                            locality.getZipCode(),
                            matchingLocalityResource.getCommunalCode(),
                            matchingLocalityResource.getRegionCode(),
                            matchingLocalityResource.getCantonCode(),
                            locality.getCountryIsoCode(),
                            new GeoPoint(matchingLocalityResource.getGeoPoint().getLongitude(), matchingLocalityResource.getGeoPoint().getLatitude())
                    );
                }
            }
        }
        return locality;
    }

    private boolean isManagedCountry(String countryIsoCode) {
        return COUNTRY_ISO_CODE_SWITZERLAND.equalsIgnoreCase(countryIsoCode) || COUNTRY_ISO_CODE_LIECHTENSTEIN.equalsIgnoreCase(countryIsoCode);
    }
}
