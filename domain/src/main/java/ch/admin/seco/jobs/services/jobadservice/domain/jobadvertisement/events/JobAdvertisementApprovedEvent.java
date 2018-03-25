package ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.events;

import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisement;

/**
 * AVAM has approved the JobAdvertisement
 */
public class JobAdvertisementApprovedEvent extends JobAdvertisementEvent {

    public JobAdvertisementApprovedEvent(JobAdvertisement jobAdvertisement) {
        super(JobAdvertisementEvents.JOB_ADVERTISEMENT_APPROVED, jobAdvertisement);
    }

}
