package ch.admin.seco.jobs.services.jobadservice.infrastructure.messagebroker.avam;

import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.ProfileRegistry;
import ch.admin.seco.jobs.services.jobadservice.application.profession.ProfessionApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@EnableConfigurationProperties(AvamJmsProperties.class)
public class AvamJmsConfig {

    @Configuration
    @Profile(ProfileRegistry.AVAM_MOCK)
    static class AvamMockedConfig {

        private final ProfessionApplicationService professionApplicationService;

        @Autowired
        AvamMockedConfig(ProfessionApplicationService professionApplicationService) {
            this.professionApplicationService = professionApplicationService;
        }

        @Bean
        public AvamJmsService avamService() {
            return new MockedAvamJmsService(professionApplicationService);
        }

    }

    @Configuration
    @Profile('!' + ProfileRegistry.AVAM_MOCK)
    static class AvamDefaultConfig {

        private final AvamJmsProperties avamJmsProperties;
        private final ProfessionApplicationService professionApplicationService;

        @Autowired
        public AvamDefaultConfig(AvamJmsProperties avamJmsProperties, ProfessionApplicationService professionApplicationService) {
            this.avamJmsProperties = avamJmsProperties;
            this.professionApplicationService = professionApplicationService;
        }

        @Bean
        public AvamJmsService avamService() throws Exception {
            return new AvamJmsService(professionApplicationService);
        }

    }

}
