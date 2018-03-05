package ch.admin.seco.jobs.services.jobadservice.infrastructure.messagebroker.avam;

import ch.admin.seco.jobs.services.jobadservice.application.ProfessionService;

public class MockedAvamJmsService extends AvamJmsService {

    public MockedAvamJmsService(ProfessionService professionService) {
        super(professionService);
    }

}
