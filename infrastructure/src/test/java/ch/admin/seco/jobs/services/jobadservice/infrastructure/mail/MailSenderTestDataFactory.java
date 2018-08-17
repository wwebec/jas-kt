package ch.admin.seco.jobs.services.jobadservice.infrastructure.mail;

import java.util.Collections;
import java.util.Locale;

import ch.admin.seco.jobs.services.jobadservice.application.MailSenderData;

class MailSenderTestDataFactory {

    static MailSenderData createDummyMailSenderData() {
        return new MailSenderData.Builder()
                .setFrom("from")
                .setTo("to")
                .setSubject("subject")
                .setTemplateName("templateName")
                .setLocale(Locale.getDefault())
                .setBcc("bcc")
                .setCc("cc")
                .setEmailAttachements(Collections.emptyList())
                .setTemplateVariables(Collections.emptyMap())
                .build();
    }
}
