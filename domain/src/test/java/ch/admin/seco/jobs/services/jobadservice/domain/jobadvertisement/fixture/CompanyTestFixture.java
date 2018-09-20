package ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.fixture;

import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.Company;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisementId;

public class CompanyTestFixture {
    public static Company testCompany() {
        return testCompany(true);
    }

    public static Company testCompany(boolean surrogate) {
        return new Company.Builder()
                .setName("name")
                .setStreet("street")
                .setHouseNumber("houseNumber")
                .setPostalCode("postalCode")
                .setCity("city")
                .setCountryIsoCode("CH")
                .setPhone("phone")
                .setEmail("email")
                .setSurrogate(surrogate)
                .build();
    }

    public static Company testCompanyWithNameAndCityAndPostalCode() {
        return new Company.Builder<>()
                .setName("Test-Company")
                .setCity("Test-Cizy")
                .setPostalCode("1234")
                .build();
    }

    public static Company testCompany(JobAdvertisementId jobAdvertisementId) {
        return testCompany(String.format("name-%s", jobAdvertisementId.getValue()));
    }

    public static Company testCompany(String companyName) {
        return new Company.Builder()
                .setName(companyName)
                .setStreet("street")
                .setPostalCode("postalCode")
                .setCity("city")
                .setCountryIsoCode("ch")
                .setSurrogate(false)
                .build();
    }
}
