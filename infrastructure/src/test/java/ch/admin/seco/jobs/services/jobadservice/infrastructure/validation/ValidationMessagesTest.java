package ch.admin.seco.jobs.services.jobadservice.infrastructure.validation;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

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
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.utils.LanguageIsoCode;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.utils.PhoneNumber;
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
        dummyClass.supportedLanguageIsoCode = "xx";
        dummyClass.number = 99;
        dummyClass.countryCode = "de";
        dummyClass.languageIsoCode = "de-ch";
        BeanPropertyBindingResult ls = new BeanPropertyBindingResult(dummyClass, "ls");

        // when
        validator.validate(dummyClass, ls);

        // then
        assertThat(ls.getErrorCount()).isEqualTo(6);

        FieldError stringFieldError = ls.getFieldError("name");
        assertThat(stringFieldError).isNotNull();
        assertThat(stringFieldError.getDefaultMessage()).isEqualTo("must not be blank");

        FieldError languageIsoCodeFieldError = ls.getFieldError("supportedLanguageIsoCode");
        assertThat(languageIsoCodeFieldError).isNotNull();
        assertThat(languageIsoCodeFieldError.getDefaultMessage()).isEqualTo("'xx' is a unsupported language ISO-Code");

        FieldError numberFieldError = ls.getFieldError("number");
        assertThat(numberFieldError).isNotNull();
        assertThat(numberFieldError.getDefaultMessage()).isEqualTo("must be less than or equal to 9");

        FieldError countryFieldError = ls.getFieldError("countryCode");
        assertThat(countryFieldError).isNotNull();
        assertThat(countryFieldError.getDefaultMessage()).contains("'de' is a invalid country ISO-Code");

        FieldError languageIsoCode = ls.getFieldError("languageIsoCode");
        assertThat(languageIsoCode).isNotNull();
        assertThat(languageIsoCode.getDefaultMessage()).contains("'de-ch' is a invalid language ISO-Code");
    }

    @Test
    public void testPhoneNumberValidationWithMessages() {
        // given
        PhoneValidationClass phoneValidationClass = new PhoneValidationClass();
        phoneValidationClass.shortPhoneNumber = "+423234567";
        phoneValidationClass.longPhoneNumber =  "+42323456789012345678";
        phoneValidationClass.withoutPlusSign = "42312345678";
        phoneValidationClass.containsIllegalCharacter = "+421a4567123";
        BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(phoneValidationClass, "pv");

        // when
        validator.validate(phoneValidationClass, bindingResult);

        // then
        assertThat(bindingResult.getErrorCount()).isEqualTo(5);

        List<FieldError> shortPhoneNumber = bindingResult.getFieldErrors("shortPhoneNumber");
        assertThat(shortPhoneNumber).isNotEmpty();
        assertThat(shortPhoneNumber).hasSize(2);

        FieldError longPhoneNumber = bindingResult.getFieldError("longPhoneNumber");
        assertThat(longPhoneNumber).isNotNull();
        assertThat(longPhoneNumber.getDefaultMessage()).contains("Phone number length must be between 11 and 20");

        FieldError withoutPlusSign = bindingResult.getFieldError("withoutPlusSign");
        assertThat(withoutPlusSign).isNotNull();
        assertThat(withoutPlusSign.getDefaultMessage()).contains("42312345678 doesn't match to [+][0-9]{10,} pattern");

        FieldError containsIllegalCharacter = bindingResult.getFieldError("containsIllegalCharacter");
        assertThat(containsIllegalCharacter).isNotNull();
        assertThat(containsIllegalCharacter.getDefaultMessage()).contains("+421a4567123 doesn't match to [+][0-9]{10,} pattern");
    }

    @SpringBootApplication
    static class TestConfig {

    }

    static class DummyClass {

        @NotBlank
        String name;

        @SupportedLanguageIsoCode
        String supportedLanguageIsoCode;

        @Min(1)
        @Max(9)
        int number;

        @NotBlank
        @CountryIsoCode
        String countryCode;

        @NotBlank
        @LanguageIsoCode
        String languageIsoCode;
    }

    static class PhoneValidationClass {
        @PhoneNumber
        String shortPhoneNumber;

        @PhoneNumber
        String longPhoneNumber;

        @PhoneNumber
        String withoutPlusSign;

        @PhoneNumber
        String containsIllegalCharacter;
    }
}
