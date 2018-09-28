package ch.admin.seco.jobs.services.jobadservice.infrastructure.messagebroker.avam;

import ch.admin.seco.jobs.services.jobadservice.infrastructure.ws.avam.DeliverOste;
import ch.admin.seco.jobs.services.jobadservice.infrastructure.ws.avam.DeliverOsteResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.ws.client.WebServiceClientException;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.client.support.interceptor.ClientInterceptor;
import org.springframework.ws.client.support.interceptor.ClientInterceptorAdapter;
import org.springframework.ws.context.MessageContext;

import javax.xml.soap.SOAPMessage;

@Configuration
@EnableConfigurationProperties(AvamProperties.class)
public class AvamWebServiceClientConfig {
    private static final Logger LOG = LoggerFactory.getLogger(AvamWebServiceClientConfig.class);

    private final AvamProperties avamProperties;

    public AvamWebServiceClientConfig(AvamProperties avamProperties) {
        this.avamProperties = avamProperties;
    }

    @Bean
    public WebServiceTemplate webServiceTemplate() {
        final WebServiceTemplate webServiceTemplate = new WebServiceTemplate();
        webServiceTemplate.setDefaultUri(avamProperties.getEndPointUrl());
        webServiceTemplate.setMarshaller(marshaller());
        webServiceTemplate.setUnmarshaller(marshaller());
        webServiceTemplate.setInterceptors(new ClientInterceptor[]{new ClientInterceptorAdapter() {
            @Override
            public boolean handleRequest(MessageContext messageContext) throws WebServiceClientException {
                messageContext.setProperty(SOAPMessage.CHARACTER_SET_ENCODING, "UTF-8");
                return true;
            }
        }});

        return webServiceTemplate;
    }

    @Bean
    public AvamWebServiceClient avamService() {
        return new AvamWebServiceClient(
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
