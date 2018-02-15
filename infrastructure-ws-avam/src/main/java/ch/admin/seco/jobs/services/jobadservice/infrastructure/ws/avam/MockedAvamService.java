package ch.admin.seco.jobs.services.jobadservice.infrastructure.ws.avam;

import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisement;

public class MockedAvamService extends AvamService {

    public MockedAvamService(String endPointUrl, String username, String password) {
        super(endPointUrl, username, password);
    }

    @Override
    protected void send(JobAdvertisement jobAdvertisement, String actionCode) {

    }

}
