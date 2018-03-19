package ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.events;

import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisement;

/**
 * JobAdvertisement has just be created
 */
public class JobAdvertisementCreatedEvent extends JobAdvertisementEvent {

    public JobAdvertisementCreatedEvent(JobAdvertisement jobAdvertisement) {
        super(JobAdvertisementEvents.JOB_ADVERTISEMENT_CREATED, jobAdvertisement);
    }

}
