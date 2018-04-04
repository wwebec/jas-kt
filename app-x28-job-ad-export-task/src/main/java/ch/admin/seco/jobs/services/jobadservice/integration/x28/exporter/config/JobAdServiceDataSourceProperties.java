package ch.admin.seco.jobs.services.jobadservice.integration.x28.exporter.config;

import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("spring.datasource.jobadservice")
public class JobAdServiceDataSourceProperties extends DataSourceProperties {
}
