package ch.admin.seco.jobs.services.jobadservice.infrastructure.ws.avam;

import ch.admin.seco.jobs.services.jobadservice.application.RavRegistrationService;
import ch.admin.seco.jobs.services.jobadservice.application.profession.ProfessionApplicationService;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisement;
import ch.admin.seco.jobs.services.jobadservice.infrastructure.ws.avam.wsdl.DeliverOste;
import ch.admin.seco.jobs.services.jobadservice.infrastructure.ws.avam.wsdl.DeliverOsteResponse;
import ch.admin.seco.jobs.services.jobadservice.infrastructure.ws.avam.wsdl.TOsteEgov;
import ch.admin.seco.jobs.services.jobadservice.infrastructure.ws.avam.wsdl.WSCredentials;
import org.springframework.ws.client.core.WebServiceTemplate;

public class AvamService implements RavRegistrationService {

    static final String AVAM_RESPONSE_OK = "NK_AVAM: OK";
    private static final String AVAM_RESPONSE_ERROR = "NK_AVAM: ERROR";

    private final JobAdvertisementAssembler assembler;
    private final WebServiceTemplate webserviceTemplate;
    private final String username;
    private final String password;

    public AvamService(ProfessionApplicationService professionApplicationService, WebServiceTemplate webserviceTemplate, String username, String password) {
        this.assembler = new JobAdvertisementAssembler(professionApplicationService);
        this.webserviceTemplate = webserviceTemplate;
        this.username = username;
        this.password = password;
    }

    @Override
    public void registrate(JobAdvertisement jobAdvertisement) {
        TOsteEgov tOsteEgov = assembler.toOsteEgov(jobAdvertisement, AvamAction.ANMELDUNG);
        send(tOsteEgov);
    }

    @Override
    public void deregister(JobAdvertisement jobAdvertisement) {
        TOsteEgov tOsteEgov = assembler.toOsteEgov(jobAdvertisement, AvamAction.ABMELDUNG);
        send(tOsteEgov);
    }

    protected void send(TOsteEgov tOsteEgov) {
        DeliverOste request = new DeliverOste();
        request.setCredentials(getCredentials());
        request.setOste(tOsteEgov);
        DeliverOsteResponse response = (DeliverOsteResponse) webserviceTemplate.marshalSendAndReceive(request);
        handleResponse(response);
    }

    WSCredentials getCredentials() {
        WSCredentials credentials = new WSCredentials();
        credentials.setUsername(username);
        credentials.setPassword(password);
        return credentials;
    }

    void handleResponse(DeliverOsteResponse response) {
        String returnCode = response.getDeliverOsteReturn();
        switch (returnCode) {
            case AVAM_RESPONSE_ERROR:
                // TODO Handle error
                break;
            case AVAM_RESPONSE_OK:
                // TODO Handle success (e.g. publish event)
                break;
        }
    }

}
