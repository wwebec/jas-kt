package ch.admin.seco.jobs.services.jobadservice.infrastructure.mail;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;

import ch.admin.seco.jobs.services.jobadservice.core.conditions.Condition;

public class MailData implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String subject;

    private final String from;

    private final String[] to;

    private final String[] cc;

    private final String[] bcc;

    private final String content;

    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (o == null || getClass() != o.getClass()) { return false; }
        MailData mailData = (MailData) o;
        return Objects.equals(subject, mailData.subject) &&
                Objects.equals(from, mailData.from) &&
                Arrays.equals(to, mailData.to) &&
                Arrays.equals(cc, mailData.cc) &&
                Arrays.equals(bcc, mailData.bcc) &&
                Objects.equals(content, mailData.content);
    }

    @Override
    public int hashCode() {

        int result = Objects.hash(subject, from, content);
        result = 31 * result + Arrays.hashCode(to);
        result = 31 * result + Arrays.hashCode(cc);
        result = 31 * result + Arrays.hashCode(bcc);
        return result;
    }

    MailData(Builder builder) {
        this.subject = Condition.notBlank(builder.subject, "E-Mail must contain a subject.");
        this.from = builder.from;
        this.to = Condition.notNull(builder.to);
        this.cc = builder.cc;
        this.bcc = builder.bcc;
        this.content = builder.content;

        Condition.isTrue(builder.to.length > 0, "E-Mail must contain at least one receiver.");
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

    public String[] getBcc() {
        return bcc;
    }

    public static class Builder {

        private String content;

        private String subject;

        private String from;

        private String[] to;

        private String[] cc;

        private String[] bcc;

        public Builder setSubject(String subject) {
            this.subject = subject;
            return this;
        }

        public Builder setFrom(String from) {
            this.from = from;
            return this;
        }

        public Builder setTo(String... to) {
            this.to = to;
            return this;
        }

        public Builder setCc(String... cc) {
            this.cc = cc;
            return this;
        }

        public Builder setBcc(String... bcc) {
            this.bcc = bcc;
            return this;
        }

        public Builder setContent(String content) {
            this.content = content;
            return this;
        }

        public MailData build() {
            return new MailData(this);
        }
    }
}
