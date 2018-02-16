package ch.admin.seco.jobs.services.jobadservice.infrastructure.ws.avam;

import ch.admin.seco.jobs.services.jobadservice.application.ProfileRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@EnableConfigurationProperties(AvamProperties.class)
public class AvamConfig {

    @Configuration
    @Profile(ProfileRegistry.AVAM_MOCK)
    static class AvamMockedConfig {

        @Bean
        public AvamService avamService() {
            return new MockedAvamService(null, null, null);
        }

    }

    @Configuration
    @Profile('!' + ProfileRegistry.AVAM_MOCK)
    static class AvamDefaultConfig {

        private final AvamProperties avamProperties;

        @Autowired
        public AvamDefaultConfig(AvamProperties avamProperties) {
            this.avamProperties = avamProperties;
        }

        @Bean
        public AvamService avamService() {
            return new AvamService(
                    eventStore, avamProperties.getEndPointUrl(),
                    avamProperties.getUsername(),
                    avamProperties.getPassword()
            );
        }
    }

}
