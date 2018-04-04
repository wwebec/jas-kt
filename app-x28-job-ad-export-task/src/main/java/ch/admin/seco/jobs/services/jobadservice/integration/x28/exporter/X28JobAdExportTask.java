package ch.admin.seco.jobs.services.jobadservice.integration.x28.exporter;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.task.configuration.EnableTask;

import ch.admin.seco.jobs.services.jobadservice.integration.x28.exporter.config.X28Properties;

@SpringBootApplication
@EnableTask
@EnableBatchProcessing
@EnableConfigurationProperties(X28Properties.class)
public class X28JobAdExportTask {

    public static void main(String[] args) {
        SpringApplication.run(X28JobAdExportTask.class, args);
    }
}
