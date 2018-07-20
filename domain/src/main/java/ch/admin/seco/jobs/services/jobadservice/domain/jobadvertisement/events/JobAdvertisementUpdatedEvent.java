package ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.events;

import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisement;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.changes.ChangeLog;

/**
 * AVAM has rejected the JobAdvertisement
 */
public class JobAdvertisementUpdatedEvent extends JobAdvertisementEvent {

    public JobAdvertisementUpdatedEvent(JobAdvertisement jobAdvertisement, ChangeLog changeLog) {
        super(JobAdvertisementEvents.JOB_ADVERTISEMENT_UPDATED, jobAdvertisement);
        additionalAttributes.put("changeLog", changeLog);
    }

}
