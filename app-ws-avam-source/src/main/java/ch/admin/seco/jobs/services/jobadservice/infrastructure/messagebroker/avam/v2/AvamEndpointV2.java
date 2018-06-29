package ch.admin.seco.jobs.services.jobadservice.infrastructure.messagebroker.avam.v2;

import ch.admin.seco.jobs.services.jobadservice.application.ProfileRegistry;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.SourceSystem;
import ch.admin.seco.jobs.services.jobadservice.infrastructure.messagebroker.avam.AvamCodeResolver;
import ch.admin.seco.jobs.services.jobadservice.infrastructure.messagebroker.avam.AvamSource;
import ch.admin.seco.jobs.services.jobadservice.infrastructure.ws.avam.source.v2.InsertOste;
import ch.admin.seco.jobs.services.jobadservice.infrastructure.ws.avam.source.v2.InsertOsteResponse;
import ch.admin.seco.jobs.services.jobadservice.infrastructure.ws.avam.source.v2.WSOsteEgov;
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
@Profile(ProfileRegistry.AVAM_WSDL_V2)
public class AvamEndpointV2 {
    private static final Logger LOG = LoggerFactory.getLogger(AvamEndpointV2.class);

    private static final String RESPONSE_OK = "SECO_WS: OK";
    private static final String RESPONSE_ERROR = "SECO_WS: ERROR";
    private static final String NAMESPACE_URI = "http://valueobjects.common.avam.bit.admin.ch";

    private final AvamSource avamSource;
    private final JobAdvertisementFromAvamAssemblerV2 jobAdvertisementFromAvamAssemblerV2;

    public AvamEndpointV2(AvamSource avamSource, JobAdvertisementFromAvamAssemblerV2 jobAdvertisementFromAvamAssemblerV2) {
        this.avamSource = avamSource;
        this.jobAdvertisementFromAvamAssemblerV2 = jobAdvertisementFromAvamAssemblerV2;
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
                avamSource.reject(jobAdvertisementFromAvamAssemblerV2.createRejectionDto(avamJobAdvertisement));
            } else if (isCancelled(avamJobAdvertisement)) {
                avamSource.cancel(jobAdvertisementFromAvamAssemblerV2.createCancellationDto(avamJobAdvertisement));
            } else if (isApproved(avamJobAdvertisement)) {
                avamSource.approve(jobAdvertisementFromAvamAssemblerV2.createApprovaldDto(avamJobAdvertisement));
            } else if (isCreatedFromAvam(avamJobAdvertisement)) {
                avamSource.create(jobAdvertisementFromAvamAssemblerV2.createCreateJobAdvertisementAvamDto(avamJobAdvertisement));
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
        return hasText(avamJobAdvertisement.getQuelleCode()) && avamJobAdvertisement.getQuelleCode().equals(AvamCodeResolver.SOURCE_SYSTEM.getLeft(SourceSystem.JOBROOM));
    }

    private InsertOsteResponse response(String returnCode) {
        InsertOsteResponse insertOsteResponse = new InsertOsteResponse();
        insertOsteResponse.setReturn(returnCode);
        return insertOsteResponse;
    }
}
