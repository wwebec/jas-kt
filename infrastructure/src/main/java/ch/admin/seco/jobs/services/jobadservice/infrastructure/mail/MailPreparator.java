package ch.admin.seco.jobs.services.jobadservice.infrastructure.mail;

import java.nio.charset.StandardCharsets;

import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;

class MailPreparator implements MimeMessagePreparator {

    private static final Logger LOGGER = LoggerFactory.getLogger(MailPreparator.class);

    private static final String CONTENT_ENCODING = StandardCharsets.UTF_8.name();

    private final MailData mailData;

    MailPreparator(MailData mailData) {
        this.mailData = mailData;
    }

    @Override
    public void prepare(MimeMessage mimeMessage) throws Exception {
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, CONTENT_ENCODING);
        messageHelper.setFrom(mailData.getFrom());
        messageHelper.setReplyTo(mailData.getFrom());
        messageHelper.setBcc(mailData.getBcc());
        messageHelper.setTo(mailData.getTo());
        if (mailData.getCc() != null) {
            messageHelper.setCc(mailData.getCc());
        }
        messageHelper.setSubject(mailData.getSubject());
        messageHelper.setText(mailData.getContent(), true);
    }
}
