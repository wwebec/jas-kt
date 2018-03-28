package ch.admin.seco.jobs.services.jobadservice.infrastructure.messagebroker.avam.v1;

import org.springframework.ws.client.core.WebServiceTemplate;

import ch.admin.seco.jobs.services.jobadservice.application.RavRegistrationException;
import ch.admin.seco.jobs.services.jobadservice.domain.avam.AvamAction;
import ch.admin.seco.jobs.services.jobadservice.domain.avam.AvamAction;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisement;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisementId;
import ch.admin.seco.jobs.services.jobadservice.infrastructure.messagebroker.avam.AvamWebServiceClient;
import ch.admin.seco.jobs.services.jobadservice.infrastructure.ws.avam.v1.DeliverOste;
import ch.admin.seco.jobs.services.jobadservice.infrastructure.ws.avam.v1.DeliverOsteResponse;
import ch.admin.seco.jobs.services.jobadservice.infrastructure.ws.avam.v1.TOsteEgov;
import ch.admin.seco.jobs.services.jobadservice.infrastructure.ws.avam.v1.WSCredentials;

public class AvamWebServiceClientV1 implements AvamWebServiceClient {

    static final String AVAM_RESPONSE_OK = "NK_AVAM: OK";
    static final String AVAM_RESPONSE_ERROR = "NK_AVAM: ERROR";

    private final AvamJobAdvertisementAssemblerV1 assembler;
    private final WebServiceTemplate webserviceTemplate;
    private final String username;
    private final String password;

    public AvamWebServiceClientV1(WebServiceTemplate webserviceTemplate, String username, String password) {
        this.assembler = new AvamJobAdvertisementAssemblerV1();
        this.webserviceTemplate = webserviceTemplate;
        this.username = username;
        this.password = password;
    }

    public void register(JobAdvertisement jobAdvertisement) {
        AvamAction action = AvamAction.ANMELDUNG;
        TOsteEgov tOsteEgov = assembler.toOsteEgov(jobAdvertisement, action);
        send(jobAdvertisement.getId(), action, tOsteEgov);
    }

    public void deregister(JobAdvertisement jobAdvertisement) {
        AvamAction action = AvamAction.ABMELDUNG;
        TOsteEgov tOsteEgov = assembler.toOsteEgov(jobAdvertisement, action);
        send(jobAdvertisement.getId(), action, tOsteEgov);
    }

    protected void send(JobAdvertisementId jobAdvertisementId, AvamAction action, TOsteEgov tOsteEgov) {
        DeliverOste request = new DeliverOste();
        request.setCredentials(getCredentials());
        request.setOste(tOsteEgov);
        DeliverOsteResponse response = (DeliverOsteResponse) webserviceTemplate.marshalSendAndReceive(request);
        handleResponse(jobAdvertisementId, action, response);
    }

    WSCredentials getCredentials() {
        WSCredentials credentials = new WSCredentials();
        credentials.setUsername(username);
        credentials.setPassword(password);
        return credentials;
    }

    void handleResponse(JobAdvertisementId jobAdvertisementId, AvamAction action, DeliverOsteResponse response) {
        String returnCode = response.getDeliverOsteReturn();
        if (!AVAM_RESPONSE_OK.equals(returnCode)) {
            throw new AvamException(jobAdvertisementId, action.name());
        }
    }

}
