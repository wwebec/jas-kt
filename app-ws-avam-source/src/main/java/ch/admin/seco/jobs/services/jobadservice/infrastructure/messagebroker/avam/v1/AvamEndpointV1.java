package ch.admin.seco.jobs.services.jobadservice.infrastructure.messagebroker.avam.v1;

import static java.util.Objects.nonNull;
import static org.springframework.util.StringUtils.hasText;

import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.context.annotation.Profile;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import ch.admin.seco.jobs.services.jobadservice.application.ProfileRegistry;
import ch.admin.seco.jobs.services.jobadservice.infrastructure.messagebroker.avam.AvamSource;
import ch.admin.seco.jobs.services.jobadservice.infrastructure.ws.avam.source.v1.InsertOste;
import ch.admin.seco.jobs.services.jobadservice.infrastructure.ws.avam.source.v1.InsertOsteResponse;
import ch.admin.seco.jobs.services.jobadservice.infrastructure.ws.avam.source.v1.WSOsteEgov;

@Endpoint
@Profile(ProfileRegistry.AVAM_WSDL_V1)
public class AvamEndpointV1 {
    private static final Logger LOG = LoggerFactory.getLogger(AvamEndpointV1.class);

    private static final String RESPONSE_OK = "SECO_WS: OK";
    private static final String RESPONSE_ERROR = "SECO_WS: ERROR";
    private static final String NAMESPACE_URI = "http://valueobjects.common.avam.bit.admin.ch";

    private final AvamSource avamSource;
    private final JobAdvertisementFromAvamAssemblerV1 jobAdvertisementFromAvamAssemblerV1;

    public AvamEndpointV1(AvamSource avamSource, JobAdvertisementFromAvamAssemblerV1 jobAdvertisementFromAvamAssemblerV1) {
        this.avamSource = avamSource;
        this.jobAdvertisementFromAvamAssemblerV1 = jobAdvertisementFromAvamAssemblerV1;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "insertOste")
    @ResponsePayload
    public InsertOsteResponse receiveJobAdvertisementFromAvam(@RequestPayload InsertOste request) {

        WSOsteEgov avamJobAdvertisement = request.getOste();

        try {
            if (isRejected(avamJobAdvertisement)) {
                avamSource.reject(jobAdvertisementFromAvamAssemblerV1.createRejectionDto(avamJobAdvertisement));
            } else if (isCancelled(avamJobAdvertisement)) {
                avamSource.cancel(jobAdvertisementFromAvamAssemblerV1.createCancellationDto(avamJobAdvertisement));
            } else if (isApproved(avamJobAdvertisement)) {
                avamSource.approve(jobAdvertisementFromAvamAssemblerV1.createApprovaldDto(avamJobAdvertisement));
            } else if (isCreatedFromAvam(avamJobAdvertisement)) {
                avamSource.create(jobAdvertisementFromAvamAssemblerV1.createCreateJobAdvertisementAvamDto(avamJobAdvertisement));
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
        return !isCancelled(avamJobAdvertisement);
    }

    private boolean isCancelled(WSOsteEgov avamJobAdvertisement) {
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
