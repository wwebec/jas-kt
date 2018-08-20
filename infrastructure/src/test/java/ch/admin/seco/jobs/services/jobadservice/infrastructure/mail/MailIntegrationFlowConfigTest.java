package ch.admin.seco.jobs.services.jobadservice.infrastructure.mail;

import static ch.admin.seco.jobs.services.jobadservice.infrastructure.mail.MailDataTestFactory.createDummyMailData;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.thymeleaf.spring5.SpringTemplateEngine;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.MessageSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MailIntegrationFlowConfigTest {

    @Autowired
    private MailIntegrationFlowConfig config;

    @Autowired
    private JavaMailSender mailSender;

    private MessageChannel retrievingGateway;

    @Before
    public void setUp() {
        this.retrievingGateway = this.config.retrievingGatewayFlow().getInputChannel();
    }

    @Test
    public void shouldSendEmailIfThereIsMailSendingTask() {
        this.retrievingGateway.send(MessageBuilder.withPayload(new MailSendingTask(createDummyMailData())).build());

        verify(mailSender, only()).send(any(MimeMessagePreparator.class));
    }

    @SpringBootApplication(exclude = JpaRepositoriesAutoConfiguration.class)
    static class TestConfig {

        @MockBean
        JavaMailSender mailSender;

        @MockBean
        MailSendingTaskRepository repository;

        @MockBean
        MailSenderProperties mailSenderProperties;

        @MockBean
        MessageSource messageSource;

        @MockBean
        SpringTemplateEngine templateEngine;
    }
}