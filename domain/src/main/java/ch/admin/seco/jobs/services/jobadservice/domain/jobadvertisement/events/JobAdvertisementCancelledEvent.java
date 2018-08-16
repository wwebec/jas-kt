package ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.events;

import ch.admin.seco.jobs.services.jobadservice.core.conditions.Condition;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisement;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.SourceSystem;

/**
 * JobAdvertisement has been cancelled be the user
 */
public class JobAdvertisementCancelledEvent extends JobAdvertisementEvent {

    public JobAdvertisementCancelledEvent(JobAdvertisement jobAdvertisement, SourceSystem cancelledBy) {
        super(JobAdvertisementEvents.JOB_ADVERTISEMENT_CANCELLED, jobAdvertisement);
        Condition.notNull(cancelledBy, "Missing cancellation source system");
        additionalAttributes.put("cancelledBy", cancelledBy);
    }

}
