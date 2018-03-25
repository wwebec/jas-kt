package ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.events;

import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisement;

/**
 * JobAdvertisement riches the end of the publication period and can be archived now
 */
public class JobAdvertisementPublishExpiredEvent extends JobAdvertisementEvent {

    public JobAdvertisementPublishExpiredEvent(JobAdvertisement jobAdvertisement) {
        super(JobAdvertisementEvents.JOB_ADVERTISEMENT_PUBLISH_EXPIRED, jobAdvertisement);
    }

}
