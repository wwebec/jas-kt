package ch.admin.seco.jobs.services.jobadservice.infrastructure.messagebroker.avam;

import ch.admin.seco.jobs.services.jobadservice.application.ProfileRegistry;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.SourceSystem;
import ch.admin.seco.jobs.services.jobadservice.infrastructure.ws.avam.source.InsertOste;
import ch.admin.seco.jobs.services.jobadservice.infrastructure.ws.avam.source.InsertOsteResponse;
import ch.admin.seco.jobs.services.jobadservice.infrastructure.ws.avam.source.WSOsteEgov;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.StringWriter;

import static org.springframework.util.StringUtils.hasText;

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
        if (LOG.isDebugEnabled()) {
            LOG.debug("Recieved request: {}", transformToXml(request));
        }
        LOG.info("Recieved stellennummerAvam={}, stellennummerEgov={}", request.getOste().getStellennummerAvam(), request.getOste().getStellennummerEgov());

        WSOsteEgov avamJobAdvertisement = request.getOste();

        try {
            if (isRejected(avamJobAdvertisement)) {
                avamSource.reject(jobAdvertisementFromAvamAssembler.createRejectionDto(avamJobAdvertisement));
            } else if (isCancelled(avamJobAdvertisement)) {
                avamSource.cancel(jobAdvertisementFromAvamAssembler.createCancellationDto(avamJobAdvertisement));
            } else if (isApproved(avamJobAdvertisement)) {
                avamSource.approve(jobAdvertisementFromAvamAssembler.createApprovaldDto(avamJobAdvertisement));
            } else if (isCreatedFromAvam(avamJobAdvertisement)) {
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

    private boolean isRejected(WSOsteEgov avamJobAdvertisement) {
        return isFromJobroom(avamJobAdvertisement) && (avamJobAdvertisement.getAblehnungGrundCode() != null);
    }

    private boolean isCancelled(WSOsteEgov avamJobAdvertisement) {
        return hasText(avamJobAdvertisement.getAbmeldeGrundCode());
    }

    private boolean isApproved(WSOsteEgov avamJobAdvertisement) {
        return isFromJobroom(avamJobAdvertisement);
    }

    private boolean isCreatedFromAvam(WSOsteEgov avamJobAdvertisement) {
        return !isCancelled(avamJobAdvertisement);
    }

    private boolean isFromJobroom(WSOsteEgov avamJobAdvertisement) {
        return (AvamCodeResolver.SOURCE_SYSTEM.getLeft(SourceSystem.JOBROOM).equals(avamJobAdvertisement.getQuelleCode())
                || AvamCodeResolver.SOURCE_SYSTEM.getLeft(SourceSystem.API).equals(avamJobAdvertisement.getQuelleCode()));
    }

    private InsertOsteResponse response(String returnCode) {
        InsertOsteResponse insertOsteResponse = new InsertOsteResponse();
        insertOsteResponse.setReturn(returnCode);
        return insertOsteResponse;
    }
}
