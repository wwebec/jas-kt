package ch.admin.seco.jobs.services.jobadservice.integration.x28.importer;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.cloud.task.configuration.EnableTask;

import ch.admin.seco.jobs.services.jobadservice.integration.x28.importer.config.X28Properties;

@SpringBootApplication
@EnableTask
@EnableBatchProcessing
@EnableBinding(Source.class)
@EnableConfigurationProperties(X28Properties.class)
public class X28JobAdImportTask {

    public static void main(String[] args) {
        SpringApplication.run(X28JobAdImportTask.class, args);
    }
}
