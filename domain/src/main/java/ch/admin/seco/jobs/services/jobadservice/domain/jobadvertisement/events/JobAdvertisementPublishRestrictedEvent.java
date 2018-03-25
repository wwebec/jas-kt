package ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.events;

import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisement;

/**
 * JobAdvertisement is published for the restricted area
 */
public class JobAdvertisementPublishRestrictedEvent extends JobAdvertisementEvent {

    public JobAdvertisementPublishRestrictedEvent(JobAdvertisement jobAdvertisement) {
        super(JobAdvertisementEvents.JOB_ADVERTISEMENT_PUBLISH_RESTRICTED, jobAdvertisement);
    }

}
