package ch.admin.seco.jobs.services.jobadservice.infrastructure.messagebroker.avam;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.ws.client.core.WebServiceTemplate;

@Configuration
@EnableConfigurationProperties(AvamProperties.class)
public class AvamConfig {

    private final AvamProperties avamProperties;

    public AvamConfig(AvamProperties avamProperties) {
        this.avamProperties = avamProperties;
    }

    @Bean
    public AvamWebServiceClient avamService() {
        return new AvamWebServiceClient(
            webServiceTemplate(),
            avamProperties.getUsername(),
            avamProperties.getPassword()
        );
    }

    @Bean
    public Jaxb2Marshaller marshaller() {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setPackagesToScan("ch.admin.seco.jobs.services.jobadservice.infrastructure.ws.avam.schema.*");
        return marshaller;
    }

    @Bean
    public WebServiceTemplate webServiceTemplate() {
        final WebServiceTemplate webServiceTemplate = new WebServiceTemplate();
        webServiceTemplate.setDefaultUri(avamProperties.getEndPointUrl());
        webServiceTemplate.setMarshaller(marshaller());
        webServiceTemplate.setUnmarshaller(marshaller());
        return webServiceTemplate;
    }
}
