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
    private static final Logger LOG = LoggerFactory.getLogger(AvamEndpoint.class);

    private static final String RESPONSE_OK = "SECO_WS: OK";
    private static final String RESPONSE_ERROR = "SECO_WS: ERROR";
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

        WSOsteEgov avamJobAdvertisement = request.getOste();

        try {
            if (isRejected(avamJobAdvertisement)) {
                avamSource.reject(jobAdvertisementFromAvamAssembler.createRejectionDto(avamJobAdvertisement));
            } else if (isCanceled(avamJobAdvertisement)) {
                avamSource.cancel(jobAdvertisementFromAvamAssembler.createCancellationDto(avamJobAdvertisement));
            } else if (isApproved(avamJobAdvertisement)) {
                avamSource.approve(jobAdvertisementFromAvamAssembler.createApprovaldDto(avamJobAdvertisement));
            } else if (isCreatedFromAvam(avamJobAdvertisement)) {
                // TODO how to handle avamJobAdvertisement.isPublikation() == false?
                avamSource.create(jobAdvertisementFromAvamAssembler.createCreateJobAdvertisementAvamDto(avamJobAdvertisement));
            } else {
                LOG.warn("Received JobAdvertisement in unknown state from AVAM: {}", transformToXml(request));
                return response(RESPONSE_ERROR);
            }

            return response(RESPONSE_OK);

        } catch (Throwable e) {
            LOG.warn("Processing 'InsertOste' failed: {}", transformToXml(request), e);
            return response(RESPONSE_ERROR);
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
            LOG.error("Marshalling of JaxbObject failed", e);
            return xmlRootObject.toString();
        }
    }

    private boolean isCreatedFromAvam(WSOsteEgov avamJobAdvertisement) {
        return !isCanceled(avamJobAdvertisement);
    }

    private boolean isCanceled(WSOsteEgov avamJobAdvertisement) {
        return hasText(avamJobAdvertisement.getAbmeldeGrundCode());
    }

    private boolean isRejected(WSOsteEgov avamJobAdvertisement) {
        return isFromJobroom(avamJobAdvertisement) && isTrue(avamJobAdvertisement.isAbgelehnt());
    }

    private boolean isApproved(WSOsteEgov avamJobAdvertisement) {
        return isFromJobroom(avamJobAdvertisement);
    }

    private boolean isTrue(Boolean bool) {
        return nonNull(bool) && bool;
    }

    private boolean isFromJobroom(WSOsteEgov avamJobAdvertisement) {
        return hasText(avamJobAdvertisement.getStellennummerEgov());
    }

    private InsertOsteResponse response(String returnCode) {
        InsertOsteResponse insertOsteResponse = new InsertOsteResponse();
        insertOsteResponse.setReturn(returnCode);
        return insertOsteResponse;
    }
}
