package ch.admin.seco.jobs.services.jobadservice.infrastructure.mail;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import org.apache.commons.lang3.SerializationUtils;

import ch.admin.seco.jobs.services.jobadservice.core.conditions.Condition;
import ch.admin.seco.jobs.services.jobadservice.core.domain.IdGenerator;
import ch.admin.seco.jobs.services.jobadservice.core.time.TimeMachine;

@Entity
class MailSendingTask {

    @Id
    private String id;

    @Column(length = 2048)
    private byte[] payload;

    private LocalDateTime created;

    MailSendingTask(MailSendingTaskData mailSendingTaskData) {
        this.id = IdGenerator.timeBasedUUID().toString();
        this.payload = SerializationUtils.serialize(mailSendingTaskData);
        this.created = TimeMachine.now();
    }

    MailSendingTask() {
        // for jpa
    }

    MailSendingTaskData getMailData() {
        return SerializationUtils.deserialize(this.payload);
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public String getId() {
        return id;
    }

    public static MailSendingTaskData.Builder builder() {
        return new MailSendingTaskData.Builder();
    }

    @Override
    public String toString() {
        return "MailSendingTask{" +
                "id='" + id + '\'' +
                '}';
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

    static class MailSendingTaskData implements Serializable {

        private static final long serialVersionUID = 1L;

        private final String subject;

        private final String from;

        private final String[] to;

        private final String[] cc;

        private final String[] bcc;

        private final String content;

        private MailSendingTaskData(Builder builder) {
            this.subject = Condition.notBlank(builder.subject, "E-Mail must contain a subject.");
            this.from = builder.from;
            this.to = Condition.notNull(builder.to);
            this.cc = builder.cc;
            this.bcc = builder.bcc;
            this.content = builder.content;
            Condition.isTrue(builder.to.length > 0, "E-Mail must contain at least one receiver.");
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) { return true; }
            if (o == null || getClass() != o.getClass()) { return false; }
            MailSendingTaskData mailSendingTaskData = (MailSendingTaskData) o;
            return Objects.equals(subject, mailSendingTaskData.subject) &&
                    Objects.equals(from, mailSendingTaskData.from) &&
                    Arrays.equals(to, mailSendingTaskData.to) &&
                    Arrays.equals(cc, mailSendingTaskData.cc) &&
                    Arrays.equals(bcc, mailSendingTaskData.bcc) &&
                    Objects.equals(content, mailSendingTaskData.content);
        }

        @Override
        public int hashCode() {

            int result = Objects.hash(subject, from, content);
            result = 31 * result + Arrays.hashCode(to);
            result = 31 * result + Arrays.hashCode(cc);
            result = 31 * result + Arrays.hashCode(bcc);
            return result;
        }

        public String getContent() {
            return content;
        }

        public String getSubject() {
            return subject;
        }

        public String getFrom() {
            return from;
        }

        public String[] getTo() {
            return to;
        }

        public String[] getCc() {
            return cc;
        }

        String[] getBcc() {
            return bcc;
        }

        public static class Builder {

            private String content;

            private String subject;

            private String from;

            private String[] to;

            private String[] cc;

            private String[] bcc;

            private Builder() {
            }

            Builder setSubject(String subject) {
                this.subject = subject;
                return this;
            }

            Builder setFrom(String from) {
                this.from = from;
                return this;
            }

            Builder setTo(String... to) {
                this.to = to;
                return this;
            }

            Builder setCc(String... cc) {
                this.cc = cc;
                return this;
            }

            Builder setBcc(String... bcc) {
                this.bcc = bcc;
                return this;
            }

            Builder setContent(String content) {
                this.content = content;
                return this;
            }

            MailSendingTaskData build() {
                return new MailSendingTaskData(this);
            }
        }
    }
}
