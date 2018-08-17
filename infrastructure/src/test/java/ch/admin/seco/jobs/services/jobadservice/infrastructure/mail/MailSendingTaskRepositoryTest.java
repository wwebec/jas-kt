package ch.admin.seco.jobs.services.jobadservice.infrastructure.mail;

import static ch.admin.seco.jobs.services.jobadservice.infrastructure.mail.MailSenderDataFactory.createDummyMailSenderData;
import static org.assertj.core.api.Java6Assertions.assertThat;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@DataJpaTest
@Transactional(propagation = Propagation.NOT_SUPPORTED)
@SpringBootTest(classes = {ThymeleafConfiguration.class, MailSenderConfig.class, MailIntegrationFlowConfig.class})
@EnableAutoConfiguration
public class MailSendingTaskRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    MailSendingTaskRepository repository;

    @Test
    public void shouldFindAllReturn() {
        final MailSendingTask task = new MailSendingTask(createDummyMailSenderData());
        this.entityManager.persist(task);

        final List<MailSendingTask> tasks = this.repository.findAll();

        assertThat(tasks.size()).isEqualTo(1);
        assertThat(tasks.get(0)).isEqualTo(task);
    }
}