package ch.admin.seco.jobs.services.jobadservice.infrastructure.mail;

import java.util.Map;

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

@Configuration
public class MailIntegrationFlowConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(MailIntegrationFlowConfig.class);

    private final EntityManagerFactory entityManagerFactory;

    private final JavaMailSender mailSender;

    public MailIntegrationFlowConfig(EntityManagerFactory entityManagerFactory, JavaMailSender mailSender) {
        this.entityManagerFactory = entityManagerFactory;
        this.mailSender = mailSender;
    }

    @Bean
    public IntegrationFlow mailSendingFlow() {
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

/*    private RequestHandlerRetryAdvice retryAdvice(){
        RequestHandlerRetryAdvice requestHandlerRetryAdvice = new RequestHandlerRetryAdvice();
        RetryTemplate retryTemplate = new RetryTemplate();
        ExponentialBackOffPolicy backOffPolicy = new ExponentialBackOffPolicy();
        retryTemplate.setBackOffPolicy(backOffPolicy);
        requestHandlerRetryAdvice.setRetryTemplate(retryTemplate);
        return requestHandlerRetryAdvice;
    }*/

    private Object sendMail(MailSendingTask mailSendingTask, Map<String, Object> headers) {
        LOGGER.debug("About to send Mail {}", mailSendingTask.getId());
        mailSender.send(new MailPreparator(mailSendingTask.getMailData()));
        return null;
    }
}
