package ch.admin.seco.jobs.services.jobadservice.infrastructure.mail;

import java.util.Locale;

import ch.admin.seco.jobs.services.jobadservice.application.MailSenderData;

class MailSenderDataFactory {

    static MailSenderData createDummyMailSenderData() {
        return new MailSenderData.Builder()
                .setFrom("from")
                .setTo("to")
                .setSubject("subject")
                .setTemplateName("templateName")
                .setLocale(Locale.getDefault())
                .build();
    }
}
