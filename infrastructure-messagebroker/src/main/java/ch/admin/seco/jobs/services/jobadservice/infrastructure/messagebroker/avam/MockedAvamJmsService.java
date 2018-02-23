package ch.admin.seco.jobs.services.jobadservice.infrastructure.messagebroker.avam;

import ch.admin.seco.jobs.services.jobadservice.application.profession.ProfessionApplicationService;

public class MockedAvamJmsService extends AvamJmsService {

    public MockedAvamJmsService(ProfessionApplicationService professionApplicationService) {
        super(professionApplicationService);
    }

}
