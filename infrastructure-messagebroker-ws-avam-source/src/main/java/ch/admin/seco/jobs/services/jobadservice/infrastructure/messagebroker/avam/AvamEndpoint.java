package ch.admin.seco.jobs.services.jobadservice.infrastructure.messagebroker.avam;

import static java.util.Objects.nonNull;
import static org.springframework.util.StringUtils.hasText;

import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import ch.admin.seco.jobs.services.jobadservice.infrastructure.ws.avam.source.InsertOste;
import ch.admin.seco.jobs.services.jobadservice.infrastructure.ws.avam.source.InsertOsteResponse;
import ch.admin.seco.jobs.services.jobadservice.infrastructure.ws.avam.source.WSOsteEgov;

@Endpoint
public class AvamEndpoint {
    private static final Logger LOGGER = LoggerFactory.getLogger(AvamEndpoint.class);

    private static final String SECO_WS_OK = "SECO_WS: OK";
    private static final String SECO_WS_ERROR = "SECO_WS: ERROR";
    private static final String NAMESPACE_URI = "http://valueobjects.common.avam.bit.admin.ch";

    private final AvamSource avamSource;
    private final JobAdvertisementFromAvamAssembler jobAdvertisementFromAvamAssembler;

    public AvamEndpoint(AvamSource avamSource, JobAdvertisementFromAvamAssembler jobAdvertisementFromAvamAssembler) {
        this.avamSource = avamSource;
        this.jobAdvertisementFromAvamAssembler = jobAdvertisementFromAvamAssembler;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "insertOste")
    @ResponsePayload
    public InsertOsteResponse receiveJobAdvertisementFromAvam(@RequestPayload InsertOste request) {

        WSOsteEgov oste = request.getOste();

        try {
            if (isRejected(oste)) {
                avamSource.reject(jobAdvertisementFromAvamAssembler.createRejectJobAdvertisement(oste));
            } else if (isApproved(oste)) {
                avamSource.approve(jobAdvertisementFromAvamAssembler.createApproveJobAdvertisement(oste));
            } else if (isCreatedOrUpdatedByRAV(oste)) {
                avamSource.update(jobAdvertisementFromAvamAssembler.createUpdateJobAdvertisement(oste));
            } else if (isCanceledByRAV(oste)) {
                avamSource.cancel(jobAdvertisementFromAvamAssembler.createCancelJobAdvertisement(oste));
            } else {
                LOGGER.warn("Received JobAdvertisement in unknown state from AVAM: {}", transformToXml(request));
                return createReponse(SECO_WS_ERROR);
            }

            return createReponse(SECO_WS_OK);

        } catch (Throwable e) {
            LOGGER.warn("Processing 'InsertOste' failed: {}", transformToXml(request), e);
            return createReponse(SECO_WS_ERROR);
        }
    }

    private String transformToXml(Object xmlRootObject) {
        try {
            JAXBContext context = JAXBContext.newInstance(xmlRootObject.getClass());
            Marshaller m = context.createMarshaller();

            StringWriter sw = new StringWriter();
            m.marshal(xmlRootObject, sw);
            return sw.toString();

        } catch (JAXBException e) {
            LOGGER.error("Marshalling of JaxbObject failed", e);
            return xmlRootObject.toString();
        }
    }

    private boolean isCreatedOrUpdatedByRAV(WSOsteEgov oste) {
        return isFromRAV(oste) && !isCanceled(oste);

    }

    private boolean isCanceledByRAV(WSOsteEgov oste) {
        return isFromRAV(oste) && !isCanceled(oste);
    }

    private boolean isFromRAV(WSOsteEgov oste) {
        return !isFromJobroom(oste);
    }

    private boolean isCanceled(WSOsteEgov oste) {
        return hasText(oste.getAbmeldeGrundCode());
    }

    private boolean isRejected(WSOsteEgov oste) {
        return isFromJobroom(oste) && isTrue(oste.isAbgelehnt());
    }

    private boolean isApproved(WSOsteEgov oste) {
        return isFromJobroom(oste);
    }

    private boolean isTrue(Boolean bool) {
        return nonNull(bool) && bool;
    }

    private boolean isFromJobroom(WSOsteEgov oste) {
        return hasText(oste.getStellennummerEgov());
    }

    private InsertOsteResponse createReponse(String returnCode) {
        InsertOsteResponse insertOsteResponse = new InsertOsteResponse();
        insertOsteResponse.setReturn(returnCode);
        return insertOsteResponse;
    }
}
