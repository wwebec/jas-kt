package ch.admin.seco.jobs.services.jobadservice.infrastructure.mail;

import ch.admin.seco.jobs.services.jobadservice.application.MailSenderService;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.ProfileRegistry;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.javamail.JavaMailSender;
import org.thymeleaf.spring5.SpringTemplateEngine;

@Configuration
@EnableConfigurationProperties(MailSenderProperties.class)
public class MailSenderConfig {

    private final MailSenderProperties mailSenderProperties;

    private final JavaMailSender javaMailSender;

    private final SpringTemplateEngine templateEngine;

    public MailSenderConfig(MailSenderProperties mailSenderProperties, JavaMailSender javaMailSender, SpringTemplateEngine templateEngine) {
        this.mailSenderProperties = mailSenderProperties;
        this.javaMailSender = javaMailSender;
        this.templateEngine = templateEngine;
    }

    @Bean
    public MailSenderService mailSenderService() {
        return new DefaultMailSenderService(templateEngine, javaMailSender, this.mailSenderProperties.getFromAdress());
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
