package ch.admin.seco.jobs.services.jobadservice.infrastructure.messagebroker.avam;

import ch.admin.seco.jobs.services.jobadservice.application.RavRegistrationService;
import ch.admin.seco.jobs.services.jobadservice.core.domain.events.DomainEventPublisher;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisement;

public class MockedAvamService implements RavRegistrationService {

    public MockedAvamService(DomainEventPublisher domainEventPublisher) {
    }

    @Override
    public void register(JobAdvertisement jobAdvertisement) {

    }

    @Override
    public void deregister(JobAdvertisement jobAdvertisement) {

    }
}
