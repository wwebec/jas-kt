package ch.admin.seco.jobs.services.jobadservice.infrastructure.ws.avam;

import ch.admin.seco.jobs.services.jobadservice.application.profession.ProfessionApplicationService;
import ch.admin.seco.jobs.services.jobadservice.infrastructure.ws.avam.wsdl.TOsteEgov;
import org.springframework.ws.client.core.WebServiceTemplate;

public class MockedAvamService extends AvamService {

    public MockedAvamService(ProfessionApplicationService professionApplicationService, WebServiceTemplate webserviceTemplate, String username, String password) {
        super(professionApplicationService, webserviceTemplate, username, password);
    }

    @Override
    protected void send(TOsteEgov tOsteEgov) {

    }

}
