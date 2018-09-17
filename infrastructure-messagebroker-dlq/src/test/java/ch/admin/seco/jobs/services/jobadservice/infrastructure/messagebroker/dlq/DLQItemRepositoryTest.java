package ch.admin.seco.jobs.services.jobadservice.infrastructure.messagebroker.dlq;

import java.time.LocalDateTime;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

@DataJpaTest
@RunWith(SpringRunner.class)
public class DLQItemRepositoryTest {

    @Autowired
    private DLQItemRepository dlqItemRepository;

    @Test
    public void testSave() {
        DLQItem entity = new DLQItem(LocalDateTime.now(), "test-headers", "test-payload", "test-dlqname", "test-aggregate-id");

        dlqItemRepository.save(entity);
    }
}