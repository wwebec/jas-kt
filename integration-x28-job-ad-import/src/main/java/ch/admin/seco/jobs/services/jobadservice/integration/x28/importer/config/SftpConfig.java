package ch.admin.seco.jobs.services.jobadservice.integration.x28.importer.config;

import static java.util.Objects.nonNull;

import java.io.File;
import java.util.Properties;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.sftp.filters.SftpSimplePatternFileListFilter;
import org.springframework.integration.sftp.inbound.SftpInboundFileSynchronizer;
import org.springframework.integration.sftp.inbound.SftpInboundFileSynchronizingMessageSource;
import org.springframework.integration.sftp.session.DefaultSftpSessionFactory;

@Configuration
public class SftpConfig {

    @Bean
    public DefaultSftpSessionFactory x28SftpSessionFactory(X28Properties x28Properties) {
        DefaultSftpSessionFactory factory = new DefaultSftpSessionFactory();
        factory.setHost(x28Properties.getHost());
        if (nonNull(x28Properties.getPort())) {
            factory.setPort(x28Properties.getPort());
        }
        factory.setUser(x28Properties.getUsername());
        factory.setPassword(x28Properties.getPassword());
        factory.setAllowUnknownKeys(x28Properties.getAllowUnknownKeys());
        factory.setSessionConfig(defaultSessionConfig());
        factory.setClientVersion("SSH-2.0-SFTP");

        return factory;
    }

    @Bean
    public SftpInboundFileSynchronizingMessageSource x28JobAdDataFileMessageSource(X28Properties x28Properties, DefaultSftpSessionFactory sftpSessionFactory) {
        SftpInboundFileSynchronizer sftpInboundFileSynchronizer = sftpInboundFileSynchronizer(x28Properties, sftpSessionFactory);
        SftpInboundFileSynchronizingMessageSource messageSource = new SftpInboundFileSynchronizingMessageSource(sftpInboundFileSynchronizer);
        messageSource.setAutoCreateLocalDirectory(true);
        messageSource.setLocalDirectory(new File(x28Properties.getLocalDirectory()));
        return messageSource;
    }

    @Bean
    public SftpInboundFileSynchronizer sftpInboundFileSynchronizer(X28Properties x28Properties, DefaultSftpSessionFactory sftpSessionFactory) {
        SftpInboundFileSynchronizer synchronizer = new SftpInboundFileSynchronizer(sftpSessionFactory);
        synchronizer.setRemoteDirectory(x28Properties.getRemoteDirectory());
        synchronizer.setFilter(new SftpSimplePatternFileListFilter(x28Properties.getFileNamePattern()));
        synchronizer.setPreserveTimestamp(true);
        return synchronizer;
    }

    private Properties defaultSessionConfig() {
        Properties properties = new Properties();
        properties.setProperty("StrictHostKeyChecking", "no");
        properties.setProperty("PreferredAuthentications", "password");
        return properties;
    }
}
