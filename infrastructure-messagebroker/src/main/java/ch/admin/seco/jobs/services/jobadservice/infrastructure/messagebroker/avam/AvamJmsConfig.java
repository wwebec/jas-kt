package ch.admin.seco.jobs.services.jobadservice.infrastructure.messagebroker.avam;

import ch.admin.seco.jobs.services.jobadservice.application.ProfessionService;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.ProfileRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

@Configuration
@EnableConfigurationProperties(AvamJmsProperties.class)
public class AvamJmsConfig {

    @Configuration
    @Profile(ProfileRegistry.AVAM_MOCK)
    static class AvamMockedConfig {

        private final ProfessionService professionService;

        @Autowired
        AvamMockedConfig(ProfessionService professionService) {
            this.professionService = professionService;
        }

        @Bean
        public AvamJmsService avamService() {
            return new MockedAvamJmsService(professionService);
        }

    }

    @Configuration
    @Profile('!' + ProfileRegistry.AVAM_MOCK)
    static class AvamDefaultConfig {

        private final ProfessionService professionService;

        @Autowired
        public AvamDefaultConfig(ProfessionService professionService) {
            this.professionService = professionService;
        }

        @Bean
        public AvamJmsService avamService() throws Exception {
            return new AvamJmsService(professionService);
        }

        private Jaxb2Marshaller marshaller() throws Exception {
            Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
            marshaller.setPackagesToScan("ch.admin.seco.jobs.services.jobadservice.infrastructure.ws.avam.wsdl.*");
            marshaller.afterPropertiesSet();
            return marshaller;
        }

    }
}
