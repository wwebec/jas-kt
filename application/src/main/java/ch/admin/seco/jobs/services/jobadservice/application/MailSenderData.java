package ch.admin.seco.jobs.services.jobadservice.application;

import ch.admin.seco.jobs.services.jobadservice.core.conditions.Condition;

import java.util.*;

public class MailSenderData {

    private final String subject;

    private final String from;

    private final String[] to;

    private final String[] cc;

    private final String[] bcc;

    private final String templateName;

    private final Map<String, Object> templateVariables;

    private final Locale locale;

    private final Collection<EmailAttachement> emailAttachments;

    MailSenderData(Builder builder) {
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
        if (builder.emailAttachements != null) {
            this.emailAttachments = builder.emailAttachements;
        } else {
            this.emailAttachments = Collections.emptyList();
        }
        Condition.isTrue(builder.to.length > 0, "E-Mail must contain at least one receiver.");
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

    public String[] getBcc() {
        return bcc;
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

    public Collection<EmailAttachement> getEmailAttachments() {
        return Collections.unmodifiableCollection(emailAttachments);
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

        private Collection<EmailAttachement> emailAttachements;

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

        public Builder setEmailAttachements(Collection<EmailAttachement> emailAttachements) {
            this.emailAttachements = emailAttachements;
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
                ", emailAttachments=" + emailAttachments +
                '}';
    }

    public static class EmailAttachement {

        private static final String MIMETYPE_PDF = "application/pdf";

        private final String fileName;

        private final String mimeType;

        private final byte[] content;

        EmailAttachement(String fileName, String mimeType, byte[] content) {
            this.fileName = fileName;
            this.mimeType = mimeType;
            this.content = content;
        }

        public static EmailAttachement createPdfAttachement(String fileName, byte[] content) {
            return new EmailAttachement(fileName, MIMETYPE_PDF, content);
        }

        public String getFileName() {
            return fileName;
        }

        public String getMimeType() {
            return mimeType;
        }

        public byte[] getContent() {
            return content;
        }


        @Override
        public String toString() {
            return "EmailAttachement{" +
                    "fileName='" + fileName + '\'' +
                    ", mimeType='" + mimeType + '\'' +
                    ", content-size='" + content.length + '\'' +
                    '}';
        }
    }
}