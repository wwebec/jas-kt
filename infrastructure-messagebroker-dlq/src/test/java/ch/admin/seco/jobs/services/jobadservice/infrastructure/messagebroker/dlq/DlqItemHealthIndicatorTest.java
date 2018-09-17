package ch.admin.seco.jobs.services.jobadservice.infrastructure.messagebroker.dlq;


import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.Status;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;


@SpringBootTest
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public class DlqItemHealthIndicatorTest {

    @Autowired
    private DlqItemHealthIndicator dlqItemHealthIndicator;

    @Autowired
    private DLQItemRepository dlqItemRepository;

    @Test
    public void testHealthIsUp() {
        // when
        Health.Builder builder = new Health.Builder();
        dlqItemHealthIndicator.doHealthCheck(builder);
        Health health = builder.build();

        // then
        assertThat(health.getStatus()).isEqualTo(Status.UP);
    }

    @Test
    public void testHealthIsDown() {
        // given
        dlqItemRepository.save(new DLQItem(LocalDateTime.now(), "TEST-H", "TEST-Payload", "TEST-NAME", "TEST-AGGREGATEID"));

        // when
        Health.Builder builder = new Health.Builder();
        dlqItemHealthIndicator.doHealthCheck(builder);
        Health health = builder.build();

        // then
        assertThat(health.getStatus()).isEqualTo(Status.DOWN);
    }
}