package ch.admin.seco.jobs.services.jobadservice.infrastructure.messagebroker.avam;

import ch.admin.seco.jobs.services.jobadservice.application.ProfessionService;
import ch.admin.seco.jobs.services.jobadservice.application.RavRegistrationService;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisement;

public class AvamJmsService implements RavRegistrationService {

    private final ProfessionService professionService;

    public AvamJmsService(ProfessionService professionService) {
        this.professionService = professionService;
    }

    @Override
    public void registrate(JobAdvertisement jobAdvertisement) {

    }

    @Override
    public void deregister(JobAdvertisement jobAdvertisement) {

    }

}
