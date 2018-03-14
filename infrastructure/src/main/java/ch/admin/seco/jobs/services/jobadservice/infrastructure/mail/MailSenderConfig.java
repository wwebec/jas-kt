package ch.admin.seco.jobs.services.jobadservice.infrastructure.mail;

import ch.admin.seco.jobs.services.jobadservice.application.MailSenderService;
import ch.admin.seco.jobs.services.jobadservice.application.ProfileRegistry;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.javamail.JavaMailSender;
import org.thymeleaf.spring5.SpringTemplateEngine;

@Configuration
@EnableConfigurationProperties(MailSenderProperties.class)
public class MailSenderConfig {

    private final SpringTemplateEngine templateEngine;

    private final JavaMailSender javaMailSender;

    private final MailSenderProperties mailSenderProperties;

    private final MessageSource messageSource;

    public MailSenderConfig(MailSenderProperties mailSenderProperties, JavaMailSender javaMailSender, SpringTemplateEngine templateEngine, MessageSource messageSource) {
        this.mailSenderProperties = mailSenderProperties;
        this.javaMailSender = javaMailSender;
        this.templateEngine = templateEngine;
        this.messageSource = messageSource;
    }

    @Bean
    public MailSenderService mailSenderService() {
        return new DefaultMailSenderService(templateEngine, javaMailSender, mailSenderProperties, messageSource);
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
