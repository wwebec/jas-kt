package ch.admin.seco.jobs.services.jobadservice.infrastructure.mail;

import org.springframework.boot.actuate.health.AbstractHealthIndicator;
import org.springframework.boot.actuate.health.Health;

class MailSendingTaskHealthIndicator extends AbstractHealthIndicator {

    private final MailSendingTaskRepository mailSendingTaskRepository;

    private final int mailQueueThreshold;

    MailSendingTaskHealthIndicator(MailSendingTaskRepository mailSendingTaskRepository, int mailQueueThreshold) {
        this.mailSendingTaskRepository = mailSendingTaskRepository;
        this.mailQueueThreshold = mailQueueThreshold;
    }

    @Override
    protected void doHealthCheck(Health.Builder builder) {
        long size = mailSendingTaskRepository.count();
        builder.withDetail("number-of-tasks", size);
        builder.withDetail("mail-queue-threshold", this.mailQueueThreshold);
        if (size > this.mailQueueThreshold) {
            builder.down();
        } else {
            builder.up();
        }
    }
}

