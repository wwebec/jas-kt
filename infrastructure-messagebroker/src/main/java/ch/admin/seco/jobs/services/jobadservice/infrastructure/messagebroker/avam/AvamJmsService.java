package ch.admin.seco.jobs.services.jobadservice.infrastructure.messagebroker.avam;

import ch.admin.seco.jobs.services.jobadservice.application.RavRegistrationService;
import ch.admin.seco.jobs.services.jobadservice.application.profession.ProfessionApplicationService;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisement;

public class AvamJmsService implements RavRegistrationService {

    private final ProfessionApplicationService professionApplicationService;

    public AvamJmsService(ProfessionApplicationService professionApplicationService) {
        this.professionApplicationService = professionApplicationService;
    }

    @Override
    public void registrate(JobAdvertisement jobAdvertisement) {

    }

    @Override
    public void deregister(JobAdvertisement jobAdvertisement) {

    }

}
