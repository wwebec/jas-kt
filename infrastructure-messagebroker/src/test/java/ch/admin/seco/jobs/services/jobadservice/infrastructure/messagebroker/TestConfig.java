package ch.admin.seco.jobs.services.jobadservice.infrastructure.messagebroker;

import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisement;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisementRepository;
import ch.admin.seco.jobs.services.jobadservice.infrastructure.messagebroker.x28.X28MessageLog;
import ch.admin.seco.jobs.services.jobadservice.infrastructure.messagebroker.x28.X28MessageLogRepository;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.Properties;

@SpringBootApplication
@EnableJpaRepositories(basePackageClasses = {JobAdvertisementRepository.class, X28MessageLogRepository.class})
@EntityScan(basePackageClasses = {JobAdvertisement.class, X28MessageLog.class})
public class TestConfig {

    @Bean
    public static PropertySourcesPlaceholderConfigurer properties() throws Exception {
        //todo: Review this
        final PropertySourcesPlaceholderConfigurer pspc = new PropertySourcesPlaceholderConfigurer();
        Properties properties = new Properties();

        properties.setProperty("spring.jpa.properties.javax.persistence.validation.mode", "none");

        pspc.setProperties(properties);
        return pspc;
    }
}
