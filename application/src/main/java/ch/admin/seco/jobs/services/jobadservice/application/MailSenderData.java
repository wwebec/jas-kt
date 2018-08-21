package ch.admin.seco.jobs.services.jobadservice.application;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import ch.admin.seco.jobs.services.jobadservice.core.conditions.Condition;

public class MailSenderData implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String subject;

    private final String from;

    private final String[] to;

    private final String[] cc;

    private final String[] bcc;

    private final String templateName;

    private final Map<String, Object> templateVariables;

    private final Locale locale;

    private MailSenderData(Builder builder) {
        this.subject = Condition.notBlank(builder.subject, "E-Mail must contain a subject.");
        this.from = builder.from;
        this.to = Condition.notNull(builder.to);
        this.cc = builder.cc;
        this.bcc = builder.bcc;
        this.templateName = Condition.notBlank(builder.templateName, "E-Mail must contain a template.");
        if (builder.templateVariables != null) {
            this.templateVariables = builder.templateVariables;
        } else {
            this.templateVariables = Collections.emptyMap();
        }
        this.locale = Condition.notNull(builder.locale, "E-Mail must contain a locale.");
        Condition.isTrue(builder.to.length > 0, "E-Mail must contain at least one receiver.");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (o == null || getClass() != o.getClass()) { return false; }
        MailSenderData that = (MailSenderData) o;
        return Objects.equals(subject, that.subject) &&
                Objects.equals(from, that.from) &&
                Arrays.equals(to, that.to) &&
                Arrays.equals(cc, that.cc) &&
                Arrays.equals(bcc, that.bcc) &&
                Objects.equals(templateName, that.templateName) &&
                Objects.equals(templateVariables, that.templateVariables) &&
                Objects.equals(locale, that.locale);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(subject, from, templateName, templateVariables, locale);
        result = 31 * result + Arrays.hashCode(to);
        result = 31 * result + Arrays.hashCode(cc);
        result = 31 * result + Arrays.hashCode(bcc);
        return result;
    }

    public String getSubject() {
        return subject;
    }

    public Optional<String> getFrom() {
        return Optional.ofNullable(from);
    }

    public String[] getTo() {
        return to;
    }

    public String[] getCc() {
        return cc;
    }

    public Optional<String[]> getBcc() {
        return Optional.ofNullable(bcc);
    }

    public String getTemplateName() {
        return templateName;
    }

    public Map<String, Object> getTemplateVariables() {
        return templateVariables;
    }

    public Locale getLocale() {
        return locale;
    }

    public static class Builder {

        private String subject;

        private String from;

        private String[] to;

        private String[] cc;

        private String[] bcc;

        private String templateName;

        private Map<String, Object> templateVariables;

        private Locale locale;

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

        public Builder setTemplateName(String templateName) {
            this.templateName = templateName;
            return this;
        }

        public Builder setTemplateVariables(Map<String, Object> templateVariables) {
            this.templateVariables = templateVariables;
            return this;
        }

        public Builder setLocale(Locale locale) {
            this.locale = locale;
            return this;
        }

        public MailSenderData build() {
            return new MailSenderData(this);
        }

    }

    @Override
    public String toString() {
        return "MailSenderData{" +
                "subject='" + subject + '\'' +
                ", from='" + from + '\'' +
                ", to=" + Arrays.toString(to) +
                ", cc=" + Arrays.toString(cc) +
                ", bcc=" + Arrays.toString(bcc) +
                ", templateName='" + templateName + '\'' +
                ", templateVariables=" + templateVariables +
                ", locale=" + locale +
                '}';
    }
}
