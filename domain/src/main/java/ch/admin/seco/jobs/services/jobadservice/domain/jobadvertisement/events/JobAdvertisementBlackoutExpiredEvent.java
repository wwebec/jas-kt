package ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.events;

import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisement;

/**
 * JobAdvertisement riches the end of the blackout period and can be shown now
 */
public class JobAdvertisementBlackoutExpiredEvent extends JobAdvertisementEvent {

    public JobAdvertisementBlackoutExpiredEvent(JobAdvertisement jobAdvertisement) {
        super(JobAdvertisementEvents.JOB_ADVERTISEMENT_BLACKOUT_EXPIRED, jobAdvertisement);
    }

}
