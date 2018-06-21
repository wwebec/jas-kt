package ch.admin.seco.jobs.services.jobadservice.infrastructure.messagebroker.avam.v2;

import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisement;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisementId;
import ch.admin.seco.jobs.services.jobadservice.infrastructure.messagebroker.avam.AvamAction;
import ch.admin.seco.jobs.services.jobadservice.infrastructure.messagebroker.avam.AvamException;
import ch.admin.seco.jobs.services.jobadservice.infrastructure.messagebroker.avam.AvamWebServiceClient;
import ch.admin.seco.jobs.services.jobadservice.infrastructure.ws.avam.v2.DeliverOste;
import ch.admin.seco.jobs.services.jobadservice.infrastructure.ws.avam.v2.DeliverOsteResponse;
import ch.admin.seco.jobs.services.jobadservice.infrastructure.ws.avam.v2.TOsteEgov;
import ch.admin.seco.jobs.services.jobadservice.infrastructure.ws.avam.v2.WSCredentials;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ws.client.core.WebServiceTemplate;

public class AvamWebServiceClientV2 implements AvamWebServiceClient {

    private static final Logger LOG = LoggerFactory.getLogger(AvamWebServiceClientV2.class);

    private static final String AVAM_RESPONSE_OK = "NK_AVAM: OK";

    private final AvamJobAdvertisementAssemblerV2 assembler;
    private final WebServiceTemplate webserviceTemplate;
    private final String username;
    private final String password;

    public AvamWebServiceClientV2(WebServiceTemplate webserviceTemplate, String username, String password) {
        this.assembler = new AvamJobAdvertisementAssemblerV2();
        this.webserviceTemplate = webserviceTemplate;
        this.username = username;
        this.password = password;
    }

    public void register(JobAdvertisement jobAdvertisement) {
        LOG.info("Start sending registration of jobAdvertisement id=" + jobAdvertisement.getId().getValue() + " stellennummerEgov=" + jobAdvertisement.getStellennummerEgov() + " to AVAM");
        LOG.debug(jobAdvertisement.toString());
        AvamAction action = AvamAction.ANMELDUNG;
        TOsteEgov tOsteEgov = assembler.toOsteEgov(jobAdvertisement, action);
        send(jobAdvertisement.getId(), action, tOsteEgov);
    }

    public void deregister(JobAdvertisement jobAdvertisement) {
        LOG.info("Start sending deregister of jobAdvertisement id=" + jobAdvertisement.getId().getValue() + " stellennummerAvam=" + jobAdvertisement.getStellennummerEgov() + " stellennummerEgov=" + jobAdvertisement.getStellennummerEgov() + " to AVAM");
        LOG.debug(jobAdvertisement.toString());
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
        if (!AVAM_RESPONSE_OK.equals(StringUtils.trim(returnCode))) {
            throw new AvamException(jobAdvertisementId, action.name(), returnCode);
        }
    }
}
