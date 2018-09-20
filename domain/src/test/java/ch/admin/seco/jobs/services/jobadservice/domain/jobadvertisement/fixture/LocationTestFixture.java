package ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.fixture;

import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.Location;

public class LocationTestFixture {
    public static Location testLocation() {
        return new Location.Builder()
                .setRemarks("remarks")
                .setCity("city")
                .setPostalCode("postalCode")
                .setCantonCode("BE")
                .setCountryIsoCode("CH")
                .build();
    }
}
