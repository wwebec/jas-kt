package ch.admin.seco.jobs.services.jobadservice.infrastructure.messagebroker.avam.v2;

import ch.admin.seco.jobs.services.jobadservice.application.ProfileRegistry;
import ch.admin.seco.jobs.services.jobadservice.infrastructure.messagebroker.avam.AvamProperties;
import ch.admin.seco.jobs.services.jobadservice.infrastructure.messagebroker.avam.AvamWebServiceClient;
import ch.admin.seco.jobs.services.jobadservice.infrastructure.ws.avam.v1.DeliverOste;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.ws.client.core.WebServiceTemplate;

@Configuration
@Profile(ProfileRegistry.AVAM_WSDL_V2)
public class AvamWebServiceClientV2Config {
    private final AvamProperties avamProperties;

    public AvamWebServiceClientV2Config(AvamProperties avamProperties) {
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
        return new AvamWebServiceClientV2(
                webServiceTemplate(),
                avamProperties.getUsername(),
                avamProperties.getPassword());
    }

    @Bean
    public Jaxb2Marshaller marshaller() {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setClassesToBeBound(DeliverOste.class);
        return marshaller;
    }
}
