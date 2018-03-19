package ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.events;

import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisement;

/**
 * The JobAdvertisement is send to AVAM for inspecting
 */
public class JobAdvertisementInspectingEvent extends JobAdvertisementEvent {

    public JobAdvertisementInspectingEvent(JobAdvertisement jobAdvertisement) {
        super(JobAdvertisementEvents.JOB_ADVERTISEMENT_INSPECTING, jobAdvertisement);
    }

}
