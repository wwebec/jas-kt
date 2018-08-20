package ch.admin.seco.jobs.services.jobadservice.infrastructure.mail;

import java.nio.charset.StandardCharsets;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.persistence.EntityManagerFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.Pollers;
import org.springframework.integration.jpa.dsl.Jpa;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

@Configuration
public class MailIntegrationFlowConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(MailIntegrationFlowConfig.class);

    private final EntityManagerFactory entityManagerFactory;

    private final JavaMailSender mailSender;

    private static final String CONTENT_ENCODING = StandardCharsets.UTF_8.name();

    public MailIntegrationFlowConfig(EntityManagerFactory entityManagerFactory, JavaMailSender mailSender) {
        this.entityManagerFactory = entityManagerFactory;
        this.mailSender = mailSender;
    }

    @Bean
    public IntegrationFlow retrievingGatewayFlow() {
        return IntegrationFlows
                .from(Jpa.inboundAdapter(this.entityManagerFactory)
                        .entityClass(MailSendingTask.class)
                        .jpaQuery("SELECT t FROM MailSendingTask t ORDER BY t.created ASC")
                        .maxResults(1)
                        .expectSingleResult(true)
                        .deleteAfterPoll(true), c -> c.poller(Pollers.fixedRate(1000).transactional()))
                .handle(this::sendMail)
                .get();
    }

    private Object sendMail(MailSendingTask mailSendingTask, Map<String, Object> headers) {
        LOGGER.debug("About to send Mail {}", mailSendingTask.getId());
        mailSender.send(mimeMessage -> enrichMessage(mimeMessage, mailSendingTask.getMailSenderData()));
        return null;
    }

    private void enrichMessage(MimeMessage mimeMessage, MailData mailData) throws MessagingException {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Sending email with MailSenderData={}", mailData);
        }
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
