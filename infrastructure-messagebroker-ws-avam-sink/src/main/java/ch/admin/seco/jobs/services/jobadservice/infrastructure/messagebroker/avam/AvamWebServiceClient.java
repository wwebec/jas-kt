package ch.admin.seco.jobs.services.jobadservice.infrastructure.messagebroker.avam;

import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisement;

public interface AvamWebServiceClient {

    void register(JobAdvertisement jobAdvertisement);

    void deregister(JobAdvertisement jobAdvertisement);

}
