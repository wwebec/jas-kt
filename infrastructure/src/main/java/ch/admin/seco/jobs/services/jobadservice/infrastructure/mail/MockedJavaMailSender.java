package ch.admin.seco.jobs.services.jobadservice.infrastructure.mail;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Part;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MockedJavaMailSender extends JavaMailSenderImpl {

    private static final Logger LOG = LoggerFactory.getLogger(MockedJavaMailSender.class);

    @Override
    protected void doSend(MimeMessage[] mimeMessages, Object[] originalMessages) {
        for (MimeMessage mimeMessage : mimeMessages) {
            try {
                LOG.info("Mail sended:\nFrom: {}\nTo: {}\nCc: {}\nBcc: {}\nSubject: {}\nAttachments: {}\nContent:\n{}",
                        mimeMessage.getFrom(),
                        mimeMessage.getRecipients(Message.RecipientType.TO),
                        mimeMessage.getRecipients(Message.RecipientType.CC),
                        mimeMessage.getRecipients(Message.RecipientType.BCC),
                        mimeMessage.getSubject(),
                        extractAttachmentFileNames(mimeMessage),
                        mimeMessage.getContent());
            } catch (MessagingException | IOException e) {
                LOG.error("Sending mail error:", e);
            }
        }
    }

    private List<String> extractAttachmentFileNames(MimeMessage mimeMessage) {
        try {
            String contentType = mimeMessage.getContentType();
            if (contentType.contains("multipart")) {
                List<String> fileNames = new ArrayList<>();
                MimeMultipart mimeMultipart = (MimeMultipart) mimeMessage.getContent();
                for (int i = 0; i < mimeMultipart.getCount(); i++) {
                    MimeBodyPart mimeBodyPart = (MimeBodyPart) mimeMultipart.getBodyPart(i);
                    if (Part.ATTACHMENT.equalsIgnoreCase(mimeBodyPart.getDisposition())) {
                        fileNames.add(mimeBodyPart.getFileName() + " (Bytes: " + mimeBodyPart.getSize() + ")");
                    }
                }
                return fileNames;
            }
        } catch (MessagingException | IOException e) {
            LOG.error("Unable to read attachments", e);
        }
        return null;
    }

}
