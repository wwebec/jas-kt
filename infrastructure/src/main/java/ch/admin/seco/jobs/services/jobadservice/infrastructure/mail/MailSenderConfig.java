package ch.admin.seco.jobs.services.jobadservice.infrastructure.mail;

import org.apache.commons.mail.util.IDNEmailAddressConverter;
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
class MailSenderConfig {

    private final MailSendingTaskRepository mailSendingTaskRepository;

    private final MailSenderProperties mailSenderProperties;

    private final MessageSource messageSource;

    private final SpringTemplateEngine templateEngine;

    private final IDNEmailAddressConverter idnEmailAddressConverter;

    public MailSenderConfig(MailSendingTaskRepository mailSendingTaskRepository,
            MailSenderProperties mailSenderProperties,
            MessageSource messageSource, SpringTemplateEngine templateEngine) {
        this.mailSendingTaskRepository = mailSendingTaskRepository;
        this.mailSenderProperties = mailSenderProperties;
        this.messageSource = messageSource;
        this.templateEngine = templateEngine;
        this.idnEmailAddressConverter = new IDNEmailAddressConverter();
    }

    @Bean
    MailSenderService mailSenderService() {
        return new DefaultMailSenderService(mailSendingTaskRepository, templateEngine, mailSenderProperties, messageSource, idnEmailAddressConverter);
    }

    @Bean
    MailSendingTaskHealthIndicator mailSendingTaskHealthIndicator() {
        return new MailSendingTaskHealthIndicator(this.mailSendingTaskRepository, this.mailSenderProperties.getMailQueueThreshold());
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
