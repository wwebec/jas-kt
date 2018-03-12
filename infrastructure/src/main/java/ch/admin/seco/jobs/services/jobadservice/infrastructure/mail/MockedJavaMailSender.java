package ch.admin.seco.jobs.services.jobadservice.infrastructure.mail;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import javax.mail.Address;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;

public class MockedJavaMailSender extends JavaMailSenderImpl {

    private static final Logger LOG = LoggerFactory.getLogger(MockedJavaMailSender.class);

    @Override
    protected void doSend(MimeMessage[] mimeMessages, Object[] originalMessages) {
        for (MimeMessage mimeMessage : mimeMessages) {
            try {
                final Address[] allRecipients = mimeMessage.getAllRecipients();
                LOG.info("From: {}\nTo: {}\nSubject: {}\nAttachments: {}\nContent:\n{}", mimeMessage.getFrom(), allRecipients, mimeMessage.getSubject(), mimeMessage.getContent());
            } catch (MessagingException | IOException e) {
                LOG.error("Sending mail error:", e);
            }
        }
    }

}
