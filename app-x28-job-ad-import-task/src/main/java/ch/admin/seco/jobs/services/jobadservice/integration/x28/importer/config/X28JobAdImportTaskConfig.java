package ch.admin.seco.jobs.services.jobadservice.integration.x28.importer.config;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Date;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zeroturnaround.zip.ZipUtil;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.xml.StaxEventItemReader;
import org.springframework.batch.item.xml.builder.StaxEventItemReaderBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.PathResource;
import org.springframework.integration.core.MessageSource;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

import ch.admin.seco.jobs.services.jobadservice.integration.x28.jobadimport.Oste;
import ch.admin.seco.jobs.services.jobadservice.integration.x28.jobadimport.OsteList;

@Configuration
public class X28JobAdImportTaskConfig {
    private static final Logger LOG = LoggerFactory.getLogger(X28JobAdImportTaskConfig.class);
    private static final String PARAMETER_XML_FILE_PATH = "XML_FILE_PATH";
    private static final String PARAMETER_LAST_MODIFIED_TIME = "LAST_MODIFIED_TIME";

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final MessageChannel messageBrokerOutputChannel;
    private final MessageSource<File> x28JobAdDataFileMessageSource;

    @Autowired
    public X28JobAdImportTaskConfig(
            JobBuilderFactory jobBuilderFactory,
            StepBuilderFactory stepBuilderFactory,
            MessageSource<File> x28JobAdDataFileMessageSource,
            MessageChannel output) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
        this.x28JobAdDataFileMessageSource = x28JobAdDataFileMessageSource;
        this.messageBrokerOutputChannel = output;
    }

    @Bean
    public Job x28ImportJob(
            StaxEventItemReader<Oste> xmlFileReader, X28JobAdWriter x28JobAdWriter) {
        return jobBuilderFactory.get("x28-jobad-xml-import")
                .incrementer(new RunIdIncrementer())
                .listener(new CleanupXmlFileJobExecutionListener())
                .start(stepBuilderFactory
                        .get("download-from-sftp")
                        .tasklet(downloadFromSftpServer())
                        .build())
                .on("NO_FILE").end()
                .on("*")
                .to(stepBuilderFactory
                        .get("send-to-job-ad-service")
                        .<Oste, Oste>chunk(10)
                        .reader(xmlFileReader)
                        .writer(x28JobAdWriter)
                        .build())
                .build()
                .build();
    }

    @Bean
    public Tasklet downloadFromSftpServer() {
        return (contribution, chunkContext) -> {
            Message<File> x28JobAdDataFileMessage = x28JobAdDataFileMessageSource.receive();
            if ((x28JobAdDataFileMessage == null) || (x28JobAdDataFileMessage.getPayload() == null)) {

                LOG.error("X28 JobAd data file not available");
                contribution.setExitStatus(new ExitStatus("NO_FILE"));
                return RepeatStatus.FINISHED;
            }

            File originFile = x28JobAdDataFileMessage.getPayload();
            File xmlFile = unzip(originFile);
            if (!xmlFile.equals(originFile)) {
                delete(originFile);
            }

            ExecutionContext executionContext = chunkContext.getStepContext().getStepExecution().getJobExecution().getExecutionContext();
            executionContext.put(PARAMETER_XML_FILE_PATH, xmlFile);
            executionContext.put(PARAMETER_LAST_MODIFIED_TIME, getLastModifiedTime(xmlFile));
            return RepeatStatus.FINISHED;
        };
    }

    @Bean
    public Jaxb2Marshaller X28Marshaller() {
        Jaxb2Marshaller jaxb2Marshaller = new Jaxb2Marshaller();
        jaxb2Marshaller.setClassesToBeBound(OsteList.class);
        return jaxb2Marshaller;
    }

    @Bean
    @JobScope
    public StaxEventItemReader<Oste> xmlFileReader(@Value("#{jobExecutionContext['" + PARAMETER_XML_FILE_PATH + "']}") File xmlFile) {
        return new StaxEventItemReaderBuilder<Oste>()
                .resource(new PathResource(xmlFile.toPath()))
                .unmarshaller(X28Marshaller())
                .strict(false)
                .saveState(false)
                .addFragmentRootElements("oste")
                .build();
    }

    @Bean
    public X28JobAdWriter x28JobAdWriter() {
        return new X28JobAdWriter(messageBrokerOutputChannel);
    }

    private void delete(File file) {
        if (Files.isWritable(file.toPath())) {
            try {
                Files.delete(file.toPath());
            } catch (IOException e) {
                file.deleteOnExit();
                LOG.error("Failed to delete file {}", file.getAbsolutePath());
            }
        }
    }

    private File unzip(File originFile) throws IOException {
        if (originFile.getName().endsWith(".zip")) {
            ZipFile zipFile = new ZipFile(originFile);
            ZipEntry zipEntry = zipFile.entries().nextElement();

            File targetFile = Files.createTempFile(originFile.toPath().getParent(), null, null).toFile();
            ZipUtil.unpackEntry(zipFile, zipEntry.getName(), targetFile);
            targetFile.setLastModified(zipEntry.getLastModifiedTime().toMillis());
            return targetFile;
        } else {
            return originFile;
        }
    }

    private Date getLastModifiedTime(File xmlFile) {
        try {
            return new Date(Files.getLastModifiedTime(xmlFile.toPath()).toMillis());
        } catch (IOException e) {
            return new Date();
        }
    }

    private class CleanupXmlFileJobExecutionListener extends JobExecutionListenerSupport {
        @Override
        public void afterJob(JobExecution jobExecution) {
            if (jobExecution.getExecutionContext().containsKey(PARAMETER_XML_FILE_PATH)) {

                File xmlFile = (File) (jobExecution.getExecutionContext().get(PARAMETER_XML_FILE_PATH));
                delete(xmlFile);
            }
        }
    }
}
