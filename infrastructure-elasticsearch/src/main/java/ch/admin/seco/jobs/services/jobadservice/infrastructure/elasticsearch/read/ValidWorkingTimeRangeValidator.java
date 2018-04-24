package ch.admin.seco.jobs.services.jobadservice.infrastructure.elasticsearch.read;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ValidWorkingTimeRangeValidator implements ConstraintValidator<ValidWorkingTimeRange, JobAdvertisementSearchRequest> {
    @Override
    public void initialize(ValidWorkingTimeRange constraintAnnotation) {

    }

    @Override
    public boolean isValid(JobAdvertisementSearchRequest jobSearchRequest, ConstraintValidatorContext context) {
        return (jobSearchRequest.getWorkloadPercentageMin() == null)
                || (jobSearchRequest.getWorkloadPercentageMax() == null)
                || (jobSearchRequest.getWorkloadPercentageMin() <= jobSearchRequest.getWorkloadPercentageMax());
    }
}
