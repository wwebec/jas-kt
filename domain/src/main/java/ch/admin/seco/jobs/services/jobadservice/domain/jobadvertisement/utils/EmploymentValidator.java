package ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.utils;

import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.Employment;

import java.util.ArrayList;
import java.util.List;

public class EmploymentValidator {

    public static List<ValidationError> validate(Employment employment) {
        List<ValidationError> errors = new ArrayList<>();
        if (employment.getEndDate() != null) {
            if ((employment.getStartDate() != null) && employment.getStartDate().isAfter(employment.getEndDate())) {
                errors.add(new ValidationError(Employment.class, "endDate", "employment.endDate.before.startDate"));
            }
        }
        if (employment.isImmediately() && (employment.getStartDate() != null)) {
            errors.add(new ValidationError(Employment.class, "immediately", "employment.startDate.and.immediately"));
        }
        if (employment.isPermanent() && (employment.getEndDate() != null)) {
            errors.add(new ValidationError(Employment.class, "permanent", "employment.endDate.and.permanent"));
        }
        if((employment.getWorkloadPercentageMin() <= 0) || (employment.getWorkloadPercentageMin() > 100)) {
            errors.add(new ValidationError(Employment.class, "workloadPercentageMin", "employment.workloadPercentageMin.outOfRange"));
        }
        if((employment.getWorkloadPercentageMax() <= 0) || (employment.getWorkloadPercentageMax() > 100)) {
            errors.add(new ValidationError(Employment.class, "workloadPercentageMax", "employment.workloadPercentageMax.outOfRange"));
        }
        if(employment.getWorkloadPercentageMin() > employment.getWorkloadPercentageMax()) {
            errors.add(new ValidationError(Employment.class, "workloadPercentageMin", "employment.workloadPercentageMin.greaterThan.workloadPercentageMax"));
        }
        return errors;
    }

}
