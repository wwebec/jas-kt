package ch.admin.seco.jobs.services.jobadservice.infrastructure.mail;

import org.thymeleaf.spring5.SpringTemplateEngine;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.javamail.JavaMailSender;

import ch.admin.seco.jobs.services.jobadservice.application.MailSenderService;
import ch.admin.seco.jobs.services.jobadservice.application.ProfileRegistry;

@Configuration
@EnableConfigurationProperties(MailSenderProperties.class)
public class MailSenderConfig {

    private final MailSendingTaskRepository mailSendingTaskRepository;

    private final SpringTemplateEngine springTemplateEngine;

    private final MailSenderProperties mailSenderProperties;

    private final MessageSource messageSource;

    public MailSenderConfig(MailSendingTaskRepository mailSendingTaskRepository,
            SpringTemplateEngine springTemplateEngine,
            MailSenderProperties mailSenderProperties,
            MessageSource messageSource) {
        this.mailSendingTaskRepository = mailSendingTaskRepository;
        this.springTemplateEngine = springTemplateEngine;
        this.mailSenderProperties = mailSenderProperties;
        this.messageSource = messageSource;
    }

    @Bean
    public MailSenderService mailSenderService() {
        return new DefaultMailSenderService(mailSendingTaskRepository);
    }

    @Bean
    public MailPreparator mailPreparator() {
        return new MailPreparator(springTemplateEngine, mailSenderProperties, messageSource);
    }

    @Bean
    public MailSendingTaskHealthCheck mailSendingTaskHealthCheck() {
        return new MailSendingTaskHealthCheck(this.mailSendingTaskRepository, this.mailSenderProperties.getMailQueueThreshold());
    }

    @Configuration
    @Profile(ProfileRegistry.MAIL_MOCK)
    static class MockedMailSendingConfig {

        @Bean
        public JavaMailSender javaMailSender() {
            return new MockedJavaMailSender();
        }
    }
}
