package ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.utils;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Size(min = 9,  max = 20, message = "{phone-number.invalid-length.message}")
@Pattern(regexp = "[+][0-9]{8,}", message = "{phone-number.invalid.message}")
@Documented
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {})
public @interface PhoneNumber {

    String message() default "";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
