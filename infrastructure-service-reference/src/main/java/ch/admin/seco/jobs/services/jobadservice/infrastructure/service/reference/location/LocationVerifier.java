package ch.admin.seco.jobs.services.jobadservice.infrastructure.service.reference.location;

import static org.springframework.util.StringUtils.hasText;

import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Predicate;

import org.springframework.stereotype.Component;

import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.Location;

@Component
public class LocationVerifier {

	static final String COUNTRY_ISO_CODE_SWITZERLAND = "CH";
	static final String COUNTRY_ISO_CODE_LIECHTENSTEIN = "LI";

	Optional<LocationResource> verifyLocation(Location location, BiFunction<String, String, Optional<LocationResource>> findLocationByPostalCodeAndCityFn) {
		return locationWithPostalCodeInSwitzerlandOrLiechtenstein().test(location) ?
				findLocationByPostalCodeAndCityFn.apply(location.getPostalCode(), location.getCity()) :
				Optional.empty();
	}

	private Predicate<Location> locationWithPostalCodeInSwitzerlandOrLiechtenstein() {
		return location -> location != null &&
				hasText(location.getPostalCode()) &&
				(COUNTRY_ISO_CODE_SWITZERLAND.equalsIgnoreCase(location.getCountryIsoCode()) || COUNTRY_ISO_CODE_LIECHTENSTEIN.equalsIgnoreCase(location.getCountryIsoCode()));
	}
}
