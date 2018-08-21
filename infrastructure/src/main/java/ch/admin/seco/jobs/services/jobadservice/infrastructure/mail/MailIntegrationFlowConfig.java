package ch.admin.seco.jobs.services.jobadservice.infrastructure.mail;

import java.util.Map;

import javax.persistence.EntityManagerFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.Pollers;
import org.springframework.integration.handler.GenericHandler;
import org.springframework.integration.jpa.dsl.Jpa;
import org.springframework.mail.javamail.JavaMailSender;

import ch.admin.seco.jobs.services.jobadservice.infrastructure.integration.IntegrationBasisConfig;
import ch.admin.seco.jobs.services.jobadservice.infrastructure.integration.RetryAdviceFactory;

@Configuration
@Import(IntegrationBasisConfig.class)
class MailIntegrationFlowConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(MailIntegrationFlowConfig.class);

    private final EntityManagerFactory entityManagerFactory;

    private final JavaMailSender mailSender;

    private final RetryAdviceFactory retryAdviceFactory;

    public MailIntegrationFlowConfig(EntityManagerFactory entityManagerFactory, JavaMailSender mailSender, RetryAdviceFactory retryAdviceFactory) {
        this.entityManagerFactory = entityManagerFactory;
        this.mailSender = mailSender;
        this.retryAdviceFactory = retryAdviceFactory;
    }

    @Bean
    IntegrationFlow mailSendingFlow() {
        return IntegrationFlows
                .from(Jpa.inboundAdapter(this.entityManagerFactory)
                        .entityClass(MailSendingTask.class)
                        .jpaQuery("SELECT t FROM MailSendingTask t ORDER BY t.created ASC")
                        .maxResults(1)
                        .expectSingleResult(true)
                        .deleteAfterPoll(true), c -> c.poller(Pollers.fixedDelay(200).transactional()))
                .handle((GenericHandler<MailSendingTask>) this::sendMail, c -> c.advice(this.retryAdviceFactory.retryAdvice()))
                .get();
    }


    private Object sendMail(MailSendingTask mailSendingTask, Map<String, Object> headers) {
        LOGGER.debug("About to send Mail {}", mailSendingTask.getId());
        mailSender.send(new MailPreparator(mailSendingTask.getMailData()));
        return null;
    }
}
