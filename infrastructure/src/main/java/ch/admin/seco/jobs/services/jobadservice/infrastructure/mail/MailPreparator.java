package ch.admin.seco.jobs.services.jobadservice.infrastructure.mail;

import java.nio.charset.StandardCharsets;
import java.util.stream.Stream;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.mail.util.ByteArrayDataSource;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.mail.util.IDNEmailAddressConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import org.springframework.context.MessageSource;
import org.springframework.mail.javamail.MimeMessageHelper;

import ch.admin.seco.jobs.services.jobadservice.application.MailSenderData;

class MailPreparator {

    private static final Logger LOGGER = LoggerFactory.getLogger(MailPreparator.class);

    private static final String CONTENT_ENCODING = StandardCharsets.UTF_8.name();

    private final SpringTemplateEngine templateEngine;

    private final MailSenderProperties mailSenderProperties;

    private final MessageSource messageSource;

    private final IDNEmailAddressConverter idnEmailAddressConverter;

    MailPreparator(SpringTemplateEngine templateEngine, MailSenderProperties mailSenderProperties, MessageSource messageSource) {
        this.templateEngine = templateEngine;
        this.mailSenderProperties = mailSenderProperties;
        this.messageSource = messageSource;
        this.idnEmailAddressConverter = new IDNEmailAddressConverter();
    }

    void prepareMail(MailSenderData mailSenderData, MimeMessage mimeMessage) throws MessagingException {
        final String subject = messageSource.getMessage(mailSenderData.getSubject(), null, mailSenderData.getSubject(), mailSenderData.getLocale());
        final String content = createContent(mailSenderData);
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Sending email with MailSenderData={},\nBODY=\n{}", mailSenderData, content);
        }
        enrichMimeMessage(mailSenderData, mimeMessage, subject, content);
    }

    private void enrichMimeMessage(MailSenderData mailSenderData, MimeMessage mimeMessage, String subject, String content) throws MessagingException {
        String from = mailSenderData.getFrom().orElse(mailSenderProperties.getFromAddress());
        String[] bcc = mailSenderData.getBcc().orElse(mailSenderProperties.getBccAddress());
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, !mailSenderData.getEmailAttachments().isEmpty(), CONTENT_ENCODING);
        messageHelper.setFrom(from);
        messageHelper.setReplyTo(from);
        messageHelper.setBcc(encodeEmailAddresses(bcc));
        messageHelper.setTo(encodeEmailAddresses(mailSenderData.getTo()));
        if (mailSenderData.getCc() != null) {
            messageHelper.setCc(encodeEmailAddresses(mailSenderData.getCc()));
        }
        messageHelper.setSubject(subject);
        messageHelper.setText(content, true);
        mailSenderData.getEmailAttachments().forEach((attachment) -> addAttachment(attachment, messageHelper));
    }

    private String createContent(MailSenderData mailSenderData) {
        return StringUtils.strip(templateEngine.process(mailSenderData.getTemplateName(), createTemplateContext(mailSenderData)));
    }

    private Context createTemplateContext(MailSenderData mailSenderData) {
        Context context = new Context();
        context.setVariable("baseUrl", mailSenderProperties.getBaseUrl());
        context.setVariable("linkToJobAdDetailPage", mailSenderProperties.getLinkToJobAdDetailPage());
        context.setVariable("user", null);
        context.setVariables(mailSenderData.getTemplateVariables());
        context.setLocale(mailSenderData.getLocale());
        return context;
    }

    private void addAttachment(MailSenderData.EmailAttachement attachment, MimeMessageHelper message) {
        try {
            message.addAttachment(attachment.getFileName(), new ByteArrayDataSource(attachment.getContent(), attachment.getMimeType()));
        } catch (MessagingException e) {
            throw new IllegalStateException(String.format("Failed to attach document %s", attachment.getFileName()), e);
        }
    }

    private String[] encodeEmailAddresses(String[] addresses) {
        return addresses != null
                ? Stream.of(addresses)
                .map(idnEmailAddressConverter::toASCII)
                .toArray(String[]::new)
                : null;
    }
}
