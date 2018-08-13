package ch.admin.seco.jobs.services.jobadservice.infrastructure.validation;

import static org.assertj.core.api.Assertions.assertThat;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.Validator;

import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.utils.CountryIsoCode;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.utils.SupportedLanguageIsoCode;

@SpringBootTest
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public class ValidationMessagesTest {

    @Autowired
    private Validator validator;

    @Test
    public void testValidateWithMessage() {
        // given
        DummyClass dummyClass = new DummyClass();
        dummyClass.name = " ";
        dummyClass.languageIsoCode = "xx";
        dummyClass.number = 99;
        dummyClass.countryCode = "de";
        BeanPropertyBindingResult ls = new BeanPropertyBindingResult(dummyClass, "ls");

        // when
        validator.validate(dummyClass, ls);

        // then
        assertThat(ls.getErrorCount()).isEqualTo(4);

        FieldError stringFieldError = ls.getFieldError("name");
        assertThat(stringFieldError).isNotNull();
        assertThat(stringFieldError.getDefaultMessage()).isEqualTo("must not be blank");

        FieldError languageIsoCodeFieldError = ls.getFieldError("languageIsoCode");
        assertThat(languageIsoCodeFieldError).isNotNull();
        assertThat(languageIsoCodeFieldError.getDefaultMessage()).isEqualTo("'xx' is a unsupported language ISO-Code");

        FieldError numberFieldError = ls.getFieldError("number");
        assertThat(numberFieldError).isNotNull();
        assertThat(numberFieldError.getDefaultMessage()).isEqualTo("must be less than or equal to 9");

        FieldError countryFieldError = ls.getFieldError("countryCode");
        assertThat(countryFieldError).isNotNull();
        assertThat(countryFieldError.getDefaultMessage()).contains("'de' is a invalid country ISO-Code");
    }

    @SpringBootApplication
    static class TestConfig {

    }

    static class DummyClass {

        @NotBlank
        String name;

        @SupportedLanguageIsoCode
        String languageIsoCode;

        @Min(1)
        @Max(9)
        int number;

        @CountryIsoCode
        String countryCode;
    }
}
