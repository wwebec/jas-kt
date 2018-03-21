package ch.admin.seco.jobs.services.jobadservice.infrastructure.messagebroker.avam;

import ch.admin.seco.jobs.services.jobadservice.core.conditions.Condition;
import ch.admin.seco.jobs.services.jobadservice.core.time.TimeMachine;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisementId;

import javax.persistence.*;
import javax.validation.Valid;
import java.time.LocalDateTime;

@Entity
public class AvamTask {

    @EmbeddedId
    @AttributeOverride(name = "value", column = @Column(name = "ID"))
    @Valid
    private AvamTaskId avamTaskId;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "JOB_ADVERTISEMENT_ID"))
    @Valid
    private JobAdvertisementId jobAdvertisementId;

    @Enumerated(EnumType.STRING)
    private AvamTaskType type;

    private LocalDateTime created;

    protected AvamTask() {
        // For reflection libs
    }

    AvamTask(AvamTaskId avamTaskId, JobAdvertisementId jobAdvertisementId, AvamTaskType type) {
        this.avamTaskId = Condition.notNull(avamTaskId);
        this.jobAdvertisementId = Condition.notNull(jobAdvertisementId);
        this.type = Condition.notNull(type);
        this.created = TimeMachine.now();
    }

    public AvamTaskId getAvamTaskId() {
        return avamTaskId;
    }

    public JobAdvertisementId getJobAdvertisementId() {
        return jobAdvertisementId;
    }

    public AvamTaskType getType() {
        return type;
    }

    public LocalDateTime getCreated() {
        return created;
    }
}
