package ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.events;

import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisement;

/**
 * JobAdvertisement is published for the public area
 */
public class JobAdvertisementPublishPublicEvent extends JobAdvertisementEvent {

    public JobAdvertisementPublishPublicEvent(JobAdvertisement jobAdvertisement) {
        super(JobAdvertisementEvents.JOB_ADVERTISEMENT_PUBLISH_PUBLIC, jobAdvertisement);
    }

}
