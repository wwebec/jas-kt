package ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.events;

import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisement;

/**
 * AVAM has rejected the JobAdvertisement
 */
public class JobAdvertisementRejectedEvent extends JobAdvertisementEvent {

    public JobAdvertisementRejectedEvent(JobAdvertisement jobAdvertisement) {
        super(JobAdvertisementEvents.JOB_ADVERTISEMENT_REJECTED, jobAdvertisement);
    }

}
