package ch.admin.seco.jobs.services.jobadservice.infrastructure.elasticsearch.read.jobadvertisement;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ValidWorkingTimeRangeValidator.class)
@Documented
public @interface ValidWorkingTimeRange {

    String message() default "workingTimeMin should be less than or equal to workingTimeMax";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

