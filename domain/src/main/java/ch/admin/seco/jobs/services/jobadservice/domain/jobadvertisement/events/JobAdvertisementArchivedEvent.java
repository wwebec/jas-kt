package ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.events;

import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisement;

/**
 * JobAdvertisement riches end of life and can be archived
 */
public class JobAdvertisementArchivedEvent extends JobAdvertisementEvent {

    public JobAdvertisementArchivedEvent(JobAdvertisement jobAdvertisement) {
        super(JobAdvertisementEvents.JOB_ADVERTISEMENT_ARCHIVED, jobAdvertisement);
    }

}
