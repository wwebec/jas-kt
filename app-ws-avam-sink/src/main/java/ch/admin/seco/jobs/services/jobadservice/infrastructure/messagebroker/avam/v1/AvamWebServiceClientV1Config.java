package ch.admin.seco.jobs.services.jobadservice.infrastructure.messagebroker.avam.v1;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.ws.client.core.WebServiceTemplate;

import ch.admin.seco.jobs.services.jobadservice.application.ProfileRegistry;
import ch.admin.seco.jobs.services.jobadservice.infrastructure.messagebroker.avam.AvamProperties;
import ch.admin.seco.jobs.services.jobadservice.infrastructure.messagebroker.avam.AvamWebServiceClient;
import ch.admin.seco.jobs.services.jobadservice.infrastructure.ws.avam.v1.DeliverOste;
import ch.admin.seco.jobs.services.jobadservice.infrastructure.ws.avam.v1.DeliverOsteResponse;

@Configuration
@Profile(ProfileRegistry.AVAM_WSDL_V1)
public class AvamWebServiceClientV1Config {
    private final AvamProperties avamProperties;

    public AvamWebServiceClientV1Config(AvamProperties avamProperties) {
        this.avamProperties = avamProperties;
    }

    @Bean
    public WebServiceTemplate webServiceTemplate() {
        final WebServiceTemplate webServiceTemplate = new WebServiceTemplate();
        webServiceTemplate.setDefaultUri(avamProperties.getEndPointUrl());
        webServiceTemplate.setMarshaller(marshaller());
        webServiceTemplate.setUnmarshaller(marshaller());
        return webServiceTemplate;
    }

    @Bean
    public AvamWebServiceClient avamService() {
        return new AvamWebServiceClientV1(
                webServiceTemplate(),
                avamProperties.getUsername(),
                avamProperties.getPassword());
    }

    @Bean
    public Jaxb2Marshaller marshaller() {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setClassesToBeBound(DeliverOste.class, DeliverOsteResponse.class);
        return marshaller;
    }
}
