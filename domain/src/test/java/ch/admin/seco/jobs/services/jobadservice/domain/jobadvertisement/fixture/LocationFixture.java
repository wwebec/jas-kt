package ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.fixture;

import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.fixture.GeoPointFixture.testGeoPoint;

import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.Location.Builder;

public class LocationFixture {

    public static Builder testLocationEmpty() {
        return new Builder();
    }

    public static Builder testLocation() {
        return testLocationEmpty()
                .setRemarks("remarks")
                .setCity("city")
                .setPostalCode("postalCode")
                .setCommunalCode("communalCode")
                .setRegionCode("regionCode")
                .setCantonCode("BE")
                .setCountryIsoCode("CH")
                .setCoordinates(testGeoPoint());
    }
}
