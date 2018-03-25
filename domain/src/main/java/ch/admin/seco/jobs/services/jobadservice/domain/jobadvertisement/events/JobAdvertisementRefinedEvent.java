package ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.events;

import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisement;

/**
 * JobAdvertisement has been refined from X28
 */
public class JobAdvertisementRefinedEvent extends JobAdvertisementEvent {

    public JobAdvertisementRefinedEvent(JobAdvertisement jobAdvertisement) {
        super(JobAdvertisementEvents.JOB_ADVERTISEMENT_REFINED, jobAdvertisement);
    }

}
