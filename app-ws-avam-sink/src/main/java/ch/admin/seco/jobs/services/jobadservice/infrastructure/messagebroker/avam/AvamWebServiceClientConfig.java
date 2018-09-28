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
import org.springframework.ws.transport.HeadersAwareSenderWebServiceConnection;
import org.springframework.ws.transport.WebServiceConnection;
import org.springframework.ws.transport.context.TransportContext;
import org.springframework.ws.transport.context.TransportContextHolder;

import java.io.IOException;

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
                TransportContext context = TransportContextHolder.getTransportContext();
                WebServiceConnection connection = context.getConnection();
                if (connection instanceof HeadersAwareSenderWebServiceConnection) {
                    try {
                        ((HeadersAwareSenderWebServiceConnection) connection).addRequestHeader("Content-Type", "text/xml;charset=UTF-8");
                    } catch (IOException e) {
                        LOG.error("Failed to add Content-Type header", e);
                    }
                }

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
