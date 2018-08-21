package ch.admin.seco.jobs.services.jobadservice.infrastructure.mail;

import java.nio.charset.StandardCharsets;

import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;

import ch.admin.seco.jobs.services.jobadservice.infrastructure.mail.MailSendingTask.MailSendingTaskData;

class MailPreparator implements MimeMessagePreparator {

    private static final Logger LOGGER = LoggerFactory.getLogger(MailPreparator.class);

    private static final String CONTENT_ENCODING = StandardCharsets.UTF_8.name();

    private final MailSendingTaskData mailSendingTaskData;

    MailPreparator(MailSendingTaskData mailSendingTaskData) {
        this.mailSendingTaskData = mailSendingTaskData;
    }

    @Override
    public void prepare(MimeMessage mimeMessage) throws Exception {
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, CONTENT_ENCODING);
        messageHelper.setFrom(mailSendingTaskData.getFrom());
        messageHelper.setReplyTo(mailSendingTaskData.getFrom());
        messageHelper.setBcc(mailSendingTaskData.getBcc());
        messageHelper.setTo(mailSendingTaskData.getTo());
        if (mailSendingTaskData.getCc() != null) {
            messageHelper.setCc(mailSendingTaskData.getCc());
        }
        messageHelper.setSubject(mailSendingTaskData.getSubject());
        messageHelper.setText(mailSendingTaskData.getContent(), true);
    }
}
