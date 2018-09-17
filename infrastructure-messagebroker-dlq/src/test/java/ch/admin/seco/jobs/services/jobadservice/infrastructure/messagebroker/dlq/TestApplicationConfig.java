package ch.admin.seco.jobs.services.jobadservice.infrastructure.messagebroker.dlq;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.mock.mockito.MockBean;

import ch.admin.seco.jobs.services.jobadservice.application.MailSenderService;

@SpringBootApplication
public class TestApplicationConfig {

    @MockBean
    MailSenderService mailSenderService;
}
