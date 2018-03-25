package ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.events;

import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisement;

/**
 * JobAdvertisement can be refined by X28
 */
public class JobAdvertisementRefiningEvent extends JobAdvertisementEvent {

    public JobAdvertisementRefiningEvent(JobAdvertisement jobAdvertisement) {
        super(JobAdvertisementEvents.JOB_ADVERTISEMENT_REFINING, jobAdvertisement);
    }

}
