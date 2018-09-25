package ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.fixture;

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
                .setCantonCode("BE")
                .setCountryIsoCode("CH");
    }
}
