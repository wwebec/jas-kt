package ch.admin.seco.jobs.services.jobadservice.integration.x28.exporter.config;


import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateSettings;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.cloud.task.configuration.DefaultTaskConfigurer;
import org.springframework.cloud.task.repository.support.TaskRepositoryInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisement;

@Configuration
@EnableConfigurationProperties({JobAdServiceDataSourceProperties.class, JpaProperties.class})
public class DataSourceConfig {
    private static final Logger LOG = LoggerFactory.getLogger(DataSourceConfig.class);

    @Primary
    @Bean
    PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }

    /*
     * The batch data source is passed from SCDF as default data source.
     */
    @Bean
    @Primary
    @ConfigurationProperties(prefix = "spring.datasource")
    DataSourceProperties batchDataSourceProperties() {
        return new DataSourceProperties();
    }

    /*
     * Initialize Batch Metadata DataSource.
     */
    @Primary
    @Bean
    DataSource batchDataSource() {
        return batchDataSourceProperties().initializeDataSourceBuilder().build();
    }

    /*
     * Setup TaskRepository.
     *
     * This is executed automatically if only one DataSource exists.
     */
    @Bean
    DefaultTaskConfigurer defaultTaskConfigurer() {
        return new DefaultTaskConfigurer(batchDataSource());
    }

    /*
     * Initialize Schema for TaskRepository.
     *
     * This is executed automatically if only one DataSource exists.
     */
    @Bean
    TaskRepositoryInitializer myTaskRepositoryInitializer() {
        TaskRepositoryInitializer taskRepositoryInitializer = new TaskRepositoryInitializer();
        taskRepositoryInitializer.setDataSource(batchDataSource());
        return taskRepositoryInitializer;
    }

    /*
     * Define data source for JobAdvertisement export with custom prefix.
     */
    @Bean
    JobAdServiceDataSourceProperties jobAdServiceDataSourceProperties() {
        return new JobAdServiceDataSourceProperties();
    }

    @Bean
    DataSource jobAdServiceDataSource() {
        JobAdServiceDataSourceProperties jobAdServiceDataSourceProperties = jobAdServiceDataSourceProperties();
        LOG.info("Building jobAdServiceDataSource ({})", jobAdServiceDataSourceProperties.getUrl());
        return jobAdServiceDataSourceProperties.initializeDataSourceBuilder().build();
    }

    /*
     * Manually initialize custom data sources with hibernate properties.
     *
     * Not required for the @Primary DataSource.
     */
    @Bean
    LocalContainerEntityManagerFactoryBean jobAdServiceEntityManagerFactory(EntityManagerFactoryBuilder builder, JpaProperties jpaProperties) {
        return builder
                .dataSource(jobAdServiceDataSource())
                .packages(JobAdvertisement.class)
                .properties(jpaProperties.getHibernateProperties(new HibernateSettings().ddlAuto("none")))
                .persistenceUnit("job-ad")
                .build();
    }

    @Bean
    PlatformTransactionManager jobAdTransactionManager(EntityManagerFactory jobAdServiceEntityManagerFactory) {
        return new JpaTransactionManager(jobAdServiceEntityManagerFactory);
    }
}
