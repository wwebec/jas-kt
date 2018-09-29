package ch.admin.seco.jobs.services.jobadservice.infrastructure.messagebroker.avam;

import ch.admin.seco.jobs.services.jobadservice.infrastructure.ws.avam.DeliverOste;
import ch.admin.seco.jobs.services.jobadservice.infrastructure.ws.avam.DeliverOsteResponse;
import com.sun.xml.internal.bind.marshaller.CharacterEscapeHandler;
import org.apache.commons.text.StringEscapeUtils;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.soap.SoapMessageFactory;
import org.springframework.ws.soap.axiom.AxiomSoapMessageFactory;

import java.util.HashMap;

@Configuration
@EnableConfigurationProperties(AvamProperties.class)
public class AvamWebServiceClientConfig {

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
        webServiceTemplate.setMessageFactory(messageFactory());

        return webServiceTemplate;
    }

    @Bean
    public SoapMessageFactory messageFactory() {
        AxiomSoapMessageFactory messageFactory = new AxiomSoapMessageFactory();
        messageFactory.setPayloadCaching(false);

        return messageFactory;
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

        HashMap<String, Object> marshallerProperties = new HashMap<>();
        marshallerProperties.put(CharacterEscapeHandler.class.getName(), (CharacterEscapeHandler) (ch, start, length, isAttVal, out)
                -> out.write(StringEscapeUtils.escapeXml10(new String(ch, start, length))));
        marshaller.setMarshallerProperties(marshallerProperties);

        return marshaller;
    }
}
