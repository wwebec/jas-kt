package ch.admin.seco.jobs.services.jobadservice.infrastructure.mail;

import ch.admin.seco.jobs.services.jobadservice.application.MailSenderData;
import ch.admin.seco.jobs.services.jobadservice.application.MailSenderService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.MessagingException;
import javax.mail.util.ByteArrayDataSource;

public class DefaultMailSenderService implements MailSenderService {

    private static final Logger LOG = LoggerFactory.getLogger(DefaultMailSenderService.class);

    private static final String UTF_8 = "UTF-8";

    private final SpringTemplateEngine templateEngine;

    private final JavaMailSender mailSender;

    private final MailSenderProperties mailSenderProperties;

    private final MessageSource messageSource;

    DefaultMailSenderService(SpringTemplateEngine templateEngine,
                             JavaMailSender mailSender,
                             MailSenderProperties mailSenderProperties,
                             MessageSource messageSource) {
        this.templateEngine = templateEngine;
        this.mailSender = mailSender;
        this.mailSenderProperties = mailSenderProperties;
        this.messageSource = messageSource;
    }

    @Async
    @Override
    public void send(MailSenderData mailSenderData) {
        Context context = new Context();
        context.setVariable("baseUrl", mailSenderProperties.getBaseUrl());
        context.setVariable("user", null);
        context.setVariables(mailSenderData.getTemplateVariables());
        context.setLocale(mailSenderData.getLocale());
        final String content = StringUtils.strip(templateEngine.process(mailSenderData.getTemplateName(), context));
        final String from = mailSenderData.getFrom().orElse(mailSenderProperties.getFromAddress());
        final String subject = messageSource.getMessage(mailSenderData.getSubject(), null, mailSenderData.getSubject(), mailSenderData.getLocale());
        if (LOG.isDebugEnabled()) {
            LOG.debug("Sending email with MailSenderData={}, \n BODY={}", mailSenderData, content);
        }
        mailSender.send(mimeMessage -> {
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, !mailSenderData.getEmailAttachments().isEmpty(), UTF_8);
            message.setFrom(from);
            message.setReplyTo(from);
            message.setTo(mailSenderData.getTo());
            message.setCc(mailSenderData.getCc());
            message.setBcc(mailSenderData.getBcc());
            message.setSubject(subject);
            message.setText(content, true);
            mailSenderData.getEmailAttachments().forEach(attachment -> {
                try {
                    message.addAttachment(attachment.getFileName(), new ByteArrayDataSource(attachment.getContent(), attachment.getMimeType()));
                } catch (MessagingException e) {
                    LOG.error(String.format("Failed to attach document %s to email with template %s, abort sending", attachment.getFileName(), mailSenderData.getTemplateName()), e);
                    throw new IllegalStateException(e);
                }
            });
        });
    }

}
