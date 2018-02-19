package ch.admin.seco.jobs.services.jobadservice.infrastructure.ws.avam;

import ch.admin.seco.jobs.services.jobadservice.application.profession.ProfessionApplicationService;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisement;
import ch.admin.seco.jobs.services.jobadservice.infrastructure.ws.avam.wsdl.DeliverOste;
import ch.admin.seco.jobs.services.jobadservice.infrastructure.ws.avam.wsdl.DeliverOsteResponse;
import ch.admin.seco.jobs.services.jobadservice.infrastructure.ws.avam.wsdl.TOsteEgov;
import ch.admin.seco.jobs.services.jobadservice.infrastructure.ws.avam.wsdl.WSCredentials;
import org.springframework.ws.client.core.WebServiceTemplate;

public class AvamService {

    public static final String AVAM_RESPONSE_CODE_OK = "NK_AVAM: OK";
    public static final String AVAM_RESPONSE_CODE_ERROR = "NK_AVAM: ERROR";

    private final ProfessionApplicationService professionApplicationService;
    private final WebServiceTemplate webserviceTemplate;
    private final String username;
    private final String password;
    private final JobAdvertisementAssembler assembler;

    public AvamService(ProfessionApplicationService professionApplicationService, WebServiceTemplate webserviceTemplate, String username, String password) {
        this.professionApplicationService = professionApplicationService;
        this.webserviceTemplate = webserviceTemplate;
        this.username = username;
        this.password = password;
        this.assembler = new JobAdvertisementAssembler(professionApplicationService);
    }

    public void sendAnmeldung(JobAdvertisement jobAdvertisement) {
        TOsteEgov tOsteEgov = assembler.toOsteEgov(jobAdvertisement, AvamAction.ANMELDUNG);
        send(tOsteEgov);
    }

    public void sendAbmeldung(JobAdvertisement jobAdvertisement) {
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

    protected WSCredentials getCredentials() {
        WSCredentials credentials = new WSCredentials();
        credentials.setUsername(username);
        credentials.setPassword(password);
        return credentials;
    }

    protected void handleResponse(DeliverOsteResponse response) {
        String returnCode = response.getDeliverOsteReturn();
        switch (returnCode) {
            case AVAM_RESPONSE_CODE_ERROR:
                return;
            case AVAM_RESPONSE_CODE_OK:
                return;
        }
    }

}
