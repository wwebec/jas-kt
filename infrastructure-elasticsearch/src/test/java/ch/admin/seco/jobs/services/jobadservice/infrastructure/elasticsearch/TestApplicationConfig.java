package ch.admin.seco.jobs.services.jobadservice.infrastructure.elasticsearch;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@EnableAutoConfiguration
@Import({ElasticsearchConfiguration.class})
public class TestApplicationConfig {
}
