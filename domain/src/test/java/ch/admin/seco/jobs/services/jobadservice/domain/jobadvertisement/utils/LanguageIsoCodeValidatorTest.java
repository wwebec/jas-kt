package ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.utils;


import static org.assertj.core.api.Assertions.assertThat;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.junit.Before;
import org.junit.Test;

import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.LanguageSkill;

public class LanguageIsoCodeValidatorTest {

	private Validator validator;

	@Before
	public void setUp() {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
	}

	@Test
	public void testNoViolation() {
		LanguageSkill languageSkill = new LanguageSkill.Builder().setLanguageIsoCode("de").build();

		Set<ConstraintViolation<LanguageSkill>> constraintViolations = validator.validate(languageSkill);

		assertThat(constraintViolations).isEmpty();
	}

	@Test
	public void testInvalid() {
		LanguageSkill languageSkill = new LanguageSkill.Builder().setLanguageIsoCode("xxx").build();

		Set<ConstraintViolation<LanguageSkill>> constraintViolations = validator.validate(languageSkill);

		assertThat(constraintViolations).hasSize(1);
	}
}
