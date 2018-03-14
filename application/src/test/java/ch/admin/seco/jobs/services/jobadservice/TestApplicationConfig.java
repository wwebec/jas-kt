package ch.admin.seco.jobs.services.jobadservice;

import ch.admin.seco.jobs.services.jobadservice.application.MailSenderService;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"ch.admin.seco.jobs.services.jobadservice.application", "ch.admin.seco.jobs.services.jobadservice.domain"})
public class TestApplicationConfig {

    @MockBean
    private MailSenderService mailSenderService;

}
