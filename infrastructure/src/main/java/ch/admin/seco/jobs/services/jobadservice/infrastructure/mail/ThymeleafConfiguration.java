package ch.admin.seco.jobs.services.jobadservice.infrastructure.mail;

import java.nio.charset.StandardCharsets;

import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;

/**
 * Documentation to Thymeleaf syntax: https://www.thymeleaf.org/doc/tutorials/3.0/usingthymeleaf.html
 */
@Configuration
@EnableConfigurationProperties(MailSenderProperties.class)
class ThymeleafConfiguration {

    private final MailSenderProperties mailSenderProperties;

    ThymeleafConfiguration(MailSenderProperties mailSenderProperties) {
        this.mailSenderProperties = mailSenderProperties;
    }

    @Bean
    @Description("Thymeleaf template resolver serving HTML 5 emails")
    public ClassLoaderTemplateResolver defaultTemplateResolver() {
        ClassLoaderTemplateResolver emailTemplateResolver = new ClassLoaderTemplateResolver();
        emailTemplateResolver.setPrefix(mailSenderProperties.getTemplatesPath());
        emailTemplateResolver.setSuffix(".html");
        emailTemplateResolver.setTemplateMode("HTML");
        emailTemplateResolver.setCharacterEncoding(StandardCharsets.UTF_8.name());
        emailTemplateResolver.setOrder(1);
        return emailTemplateResolver;
    }
}
