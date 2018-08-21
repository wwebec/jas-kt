package ch.admin.seco.jobs.services.jobadservice.infrastructure.mail;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.awaitility.Awaitility.await;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class MailIntegrationFlowConfigTest {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private MailSendingTaskRepository mailSendingTaskRepository;

    @Test
    public void shouldSendEmailIfThereIsMailSendingTask() {
        // given
        MailSendingTask mailSendingTask = new MailSendingTask(MailDataTestFactory.createDummyMailData());

        // when
        mailSendingTaskRepository.save(mailSendingTask);

        // then
        await().atMost(10, SECONDS)
                .untilAsserted(() -> verify(mailSender, only()).send(any(MimeMessagePreparator.class)));

    }

    @SpringBootApplication
    static class TestConfig {

        @MockBean
        JavaMailSender mailSender;
    }
}