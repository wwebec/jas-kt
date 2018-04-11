package ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.utils;

import ch.admin.seco.jobs.services.jobadservice.core.time.TimeMachine;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.Employment;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.Publication;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PublicationValidator {

    public static List<ValidationError> validate(Publication publication, LocalDate employmentEndDate) {
        List<ValidationError> errors = new ArrayList<>();
        if ((publication.getStartDate() != null) && (TimeMachine.now().toLocalDate().isAfter(publication.getStartDate()))) {
            errors.add(new ValidationError(Publication.class, "startDate", "publication.startDate.before.now"));
        }
        if (publication.getEndDate() != null) {
            if ((publication.getStartDate() != null) && publication.getStartDate().isAfter(publication.getEndDate())) {
                errors.add(new ValidationError(Publication.class, "endDate", "publication.endDate.before.startDate"));
            }
        }
        return errors;
    }

}
