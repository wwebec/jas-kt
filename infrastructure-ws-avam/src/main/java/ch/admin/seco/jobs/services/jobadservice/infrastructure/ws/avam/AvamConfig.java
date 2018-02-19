package ch.admin.seco.jobs.services.jobadservice.infrastructure.ws.avam;

import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.ProfileRegistry;
import ch.admin.seco.jobs.services.jobadservice.application.profession.ProfessionApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.ws.client.core.WebServiceTemplate;

@Configuration
@EnableConfigurationProperties(AvamProperties.class)
public class AvamConfig {

    @Configuration
    @Profile(ProfileRegistry.AVAM_MOCK)
    static class AvamMockedConfig {

        private final ProfessionApplicationService professionApplicationService;

        @Autowired
        AvamMockedConfig(ProfessionApplicationService professionApplicationService) {
            this.professionApplicationService = professionApplicationService;
        }

        @Bean
        public AvamService avamService() {
            return new MockedAvamService(professionApplicationService, null, null, null);
        }

    }

    @Configuration
    @Profile('!' + ProfileRegistry.AVAM_MOCK)
    static class AvamDefaultConfig {

        private final AvamProperties avamProperties;
        private final ProfessionApplicationService professionApplicationService;

        @Autowired
        public AvamDefaultConfig(AvamProperties avamProperties, ProfessionApplicationService professionApplicationService) {
            this.avamProperties = avamProperties;
            this.professionApplicationService = professionApplicationService;
        }

        @Bean
        public AvamService avamService() throws Exception {
            return new AvamService(
                    professionApplicationService,
                    webServiceTemplate(),
                    avamProperties.getUsername(),
                    avamProperties.getPassword()
            );
        }

        private WebServiceTemplate webServiceTemplate() throws Exception {
            final WebServiceTemplate webServiceTemplate = new WebServiceTemplate();
            webServiceTemplate.setDefaultUri(avamProperties.getEndPointUrl());
            final Jaxb2Marshaller jaxb2Marshaller = this.marshaller();
            webServiceTemplate.setMarshaller(jaxb2Marshaller);
            webServiceTemplate.setUnmarshaller(jaxb2Marshaller);
            return webServiceTemplate;
        }

        private Jaxb2Marshaller marshaller() throws Exception {
            Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
            marshaller.setPackagesToScan("ch.admin.seco.jobs.services.jobadservice.infrastructure.ws.avam.wsdl.*");
            marshaller.afterPropertiesSet();
            return marshaller;
        }
    }

}
