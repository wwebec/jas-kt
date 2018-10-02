package ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.utils;

import java.util.HashSet;
import java.util.Set;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.lang3.StringUtils;

public class SupportedLanguageIsoCodeValidator implements ConstraintValidator<SupportedLanguageIsoCode, String> {

	private static final Set<String> SUPPORTED_LANGUAGES = new HashSet<>();

	static {
		SUPPORTED_LANGUAGES.add("ar");
		SUPPORTED_LANGUAGES.add("bg");
		SUPPORTED_LANGUAGES.add("bs");
		SUPPORTED_LANGUAGES.add("ch");
		SUPPORTED_LANGUAGES.add("de-ch");
		SUPPORTED_LANGUAGES.add("cs");
		SUPPORTED_LANGUAGES.add("da");
		SUPPORTED_LANGUAGES.add("de");
		SUPPORTED_LANGUAGES.add("el");
		SUPPORTED_LANGUAGES.add("en");
		SUPPORTED_LANGUAGES.add("es");
		SUPPORTED_LANGUAGES.add("fi");
		SUPPORTED_LANGUAGES.add("fr");
		SUPPORTED_LANGUAGES.add("he");
		SUPPORTED_LANGUAGES.add("hr");
		SUPPORTED_LANGUAGES.add("hu");
		SUPPORTED_LANGUAGES.add("it");
		SUPPORTED_LANGUAGES.add("ja");
		SUPPORTED_LANGUAGES.add("km");
		SUPPORTED_LANGUAGES.add("ku");
		SUPPORTED_LANGUAGES.add("lt");
		SUPPORTED_LANGUAGES.add("mk");
		SUPPORTED_LANGUAGES.add("nl");
		SUPPORTED_LANGUAGES.add("no");
		SUPPORTED_LANGUAGES.add("pl");
		SUPPORTED_LANGUAGES.add("pt");
		SUPPORTED_LANGUAGES.add("rm");
		SUPPORTED_LANGUAGES.add("ro");
		SUPPORTED_LANGUAGES.add("ru");
		SUPPORTED_LANGUAGES.add("sk");
		SUPPORTED_LANGUAGES.add("sl");
		SUPPORTED_LANGUAGES.add("sq");
		SUPPORTED_LANGUAGES.add("sr");
		SUPPORTED_LANGUAGES.add("sv");
		SUPPORTED_LANGUAGES.add("ta");
		SUPPORTED_LANGUAGES.add("th");
		SUPPORTED_LANGUAGES.add("tr");
		SUPPORTED_LANGUAGES.add("vi");
		SUPPORTED_LANGUAGES.add("zh");
		SUPPORTED_LANGUAGES.add("fa");
		SUPPORTED_LANGUAGES.add("prs");
		SUPPORTED_LANGUAGES.add("ti");
		SUPPORTED_LANGUAGES.add("98"); //other
		SUPPORTED_LANGUAGES.add("99"); //not specified
	}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if (StringUtils.isBlank(value)) {
			return true;
		}
		return SUPPORTED_LANGUAGES.contains(value.toLowerCase());
	}
}
