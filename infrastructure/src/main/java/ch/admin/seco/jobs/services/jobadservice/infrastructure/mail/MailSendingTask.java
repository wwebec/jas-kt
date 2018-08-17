package ch.admin.seco.jobs.services.jobadservice.infrastructure.mail;

import java.time.LocalDateTime;
import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.Id;

import org.apache.commons.lang3.SerializationUtils;

import ch.admin.seco.jobs.services.jobadservice.core.domain.IdGenerator;
import ch.admin.seco.jobs.services.jobadservice.core.time.TimeMachine;

@Entity
class MailSendingTask {

    @Id
    private String id;

    private byte[] payload;

    private LocalDateTime created;

    MailSendingTask(MailData mailData) {
        this.id = IdGenerator.timeBasedUUID().toString();
        this.payload = SerializationUtils.serialize(mailData);
        this.created = TimeMachine.now();
    }

    MailSendingTask() {
        // for jpa
    }

    MailData getMailSenderData() {
        return SerializationUtils.deserialize(this.payload);
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public String getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (o == null || getClass() != o.getClass()) { return false; }
        MailSendingTask that = (MailSendingTask) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
