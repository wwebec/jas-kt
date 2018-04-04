package ch.admin.seco.jobs.services.jobadservice.integration.x28.exporter.config;

import static java.util.Objects.nonNull;
import static org.springframework.util.StringUtils.quote;

import java.util.Properties;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.sftp.outbound.SftpMessageHandler;
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
    public SftpMessageHandler sftpMessageHandler(X28Properties x28Properties, DefaultSftpSessionFactory sftpSessionFactory) {
        SftpMessageHandler sftpMessageHandler = new SftpMessageHandler(sftpSessionFactory);
        sftpMessageHandler.setRemoteDirectoryExpressionString(quote(x28Properties.getRemoteDirectory()));
        return sftpMessageHandler;
    }

    private Properties defaultSessionConfig() {
        Properties properties = new Properties();
        properties.setProperty("StrictHostKeyChecking", "no");
        properties.setProperty("PreferredAuthentications", "password");
        return properties;
    }
}
