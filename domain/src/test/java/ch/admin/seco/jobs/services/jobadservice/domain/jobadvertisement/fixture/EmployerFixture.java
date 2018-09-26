package ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.fixture;

import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.Employer.Builder;

public class EmployerFixture {
    public static Builder testEmployerEmty() {
        return new Builder();
    }

    public static Builder testEmployer() {
        return testEmployerEmty()
             .setName("name")
             .setPostalCode("postalCode")
             .setCity("city")
             .setCountryIsoCode("countryIsoCode");
    }
}
