package ch.admin.seco.jobs.services.jobadservice.infrastructure.service.reference.location;

import static ch.admin.seco.jobs.services.jobadservice.infrastructure.service.reference.location.LocationVerifier.COUNTRY_ISO_CODE_LIECHTENSTEIN;
import static ch.admin.seco.jobs.services.jobadservice.infrastructure.service.reference.location.LocationVerifier.COUNTRY_ISO_CODE_SWITZERLAND;
import static org.assertj.core.api.Java6Assertions.assertThat;

import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.Location;

@RunWith(Parameterized.class)
public class LocationVerifierTest {

	@Parameters(name = "of country ISO code {0} and postal code {1} return {2}")
	public static Collection<Object[]> data() {
		return Arrays.asList(new Object[][] {
				{COUNTRY_ISO_CODE_SWITZERLAND, "8134", true},
				{COUNTRY_ISO_CODE_SWITZERLAND, null, false},
				{COUNTRY_ISO_CODE_LIECHTENSTEIN, "8134", true},
				{COUNTRY_ISO_CODE_LIECHTENSTEIN, null, false},
				{"PL", "54-129", false},

		});
	}

	private LocationVerifier locationVerifier = new LocationVerifier();

	private String countryIsoCode;
	private String postalCode;
	private boolean valid;

	public LocationVerifierTest(String countryIsoCode, String postalCode,  boolean valid) {
		this.countryIsoCode = countryIsoCode;
		this.postalCode = postalCode;
		this.valid = valid;
	}

	@Test
	public void shouldVerifyLocation() {
		Location location = new Location.Builder()
				.setCountryIsoCode(countryIsoCode)
				.setPostalCode(postalCode)
				.build();

		assertThat(locationVerifier.verifyLocation(location, (postalCode, city) -> Optional.of(new LocationResource())).isPresent()).isEqualTo(valid);
	}
}