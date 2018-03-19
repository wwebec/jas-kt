package ch.admin.seco.jobs.services.jobadservice.infrastructure.messagebroker.avam;

import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.events.JobAdvertisementEvents.JOB_ADVERTISEMENT_CANCELLED;
import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.events.JobAdvertisementEvents.JOB_ADVERTISEMENT_CREATED;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.ws.client.core.WebServiceTemplate;

@Configuration
@EnableConfigurationProperties(AvamProperties.class)
@EnableBinding(Sink.class)
public class AvamConfig {

    final static String JOB_ADVERTISEMENT_CREATED_VALUE = JOB_ADVERTISEMENT_CREATED.getDomainEventType().getValue();
    final static String JOB_ADVERTISEMENT_CANCELLED_VALUE = JOB_ADVERTISEMENT_CANCELLED.getDomainEventType().getValue();

    private final AvamProperties avamProperties;

    public AvamConfig(AvamProperties avamProperties) {
        this.avamProperties = avamProperties;
    }

    @Bean
    public AvamWebService avamService() {
        return new AvamWebService(
            webServiceTemplate(),
            avamProperties.getUsername(),
            avamProperties.getPassword()
        );
    }

    @Bean
    public Jaxb2Marshaller marshaller() {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setPackagesToScan("ch.admin.seco.jobs.services.jobadservice.infrastructure.ws.avam.wsdl.*");
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
