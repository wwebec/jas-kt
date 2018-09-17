package ch.admin.seco.jobs.services.jobadservice.infrastructure.messagebroker.dlq;

import org.springframework.boot.actuate.health.AbstractHealthIndicator;
import org.springframework.boot.actuate.health.Health;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class DlqItemHealthIndicator extends AbstractHealthIndicator {

    private final DLQItemRepository dlqItemRepository;

    public DlqItemHealthIndicator(DLQItemRepository dlqItemRepository) {
        this.dlqItemRepository = dlqItemRepository;
    }

    @Override
    protected void doHealthCheck(Health.Builder builder) {
        long count = this.dlqItemRepository.count();
        if (count > 0) {
            builder.down().withDetail("dlq-item-count", count);
        } else {
            builder.up();
        }
    }
}
