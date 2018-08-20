package ch.admin.seco.jobs.services.jobadservice.infrastructure.mail;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@DataJpaTest
public class MailSendingTaskRepositoryTest {

    @Autowired
    private MailSendingTaskRepository repository;

    @Test
    public void shouldSaveAndFine() {
        // given
        MailSendingTask sendingTask = new MailSendingTask(MailDataTestFactory.createDummyMailData());
        // when
        this.repository.save(sendingTask);

        // then
        Optional<MailSendingTask> mailSendingTask = this.repository.findById(sendingTask.getId());
        assertThat(mailSendingTask).isPresent();
        assertThat(mailSendingTask.get().getMailData()).isNotNull();
    }

    @SpringBootApplication
    static class TestConfig {

    }
}