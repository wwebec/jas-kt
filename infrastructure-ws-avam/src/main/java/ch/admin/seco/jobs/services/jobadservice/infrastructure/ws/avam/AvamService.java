package ch.admin.seco.jobs.services.jobadservice.infrastructure.ws.avam;

import ch.admin.seco.jobs.services.jobadservice.application.RavRegistrationException;
import ch.admin.seco.jobs.services.jobadservice.application.RavRegistrationService;
import ch.admin.seco.jobs.services.jobadservice.application.profession.ProfessionApplicationService;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisement;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisementId;
import ch.admin.seco.jobs.services.jobadservice.infrastructure.ws.avam.wsdl.DeliverOste;
import ch.admin.seco.jobs.services.jobadservice.infrastructure.ws.avam.wsdl.DeliverOsteResponse;
import ch.admin.seco.jobs.services.jobadservice.infrastructure.ws.avam.wsdl.TOsteEgov;
import ch.admin.seco.jobs.services.jobadservice.infrastructure.ws.avam.wsdl.WSCredentials;
import org.springframework.ws.client.core.WebServiceTemplate;

public class AvamService implements RavRegistrationService {

    static final String AVAM_RESPONSE_OK = "NK_AVAM: OK";
    static final String AVAM_RESPONSE_ERROR = "NK_AVAM: ERROR";

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
        AvamAction action = AvamAction.ANMELDUNG;
        TOsteEgov tOsteEgov = assembler.toOsteEgov(jobAdvertisement, action);
        send(jobAdvertisement.getId(), action, tOsteEgov);
    }

    @Override
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
            throw new RavRegistrationException(jobAdvertisementId, action.name());
        }
    }

}
