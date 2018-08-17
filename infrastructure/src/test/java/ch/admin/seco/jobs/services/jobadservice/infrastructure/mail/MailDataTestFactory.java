package ch.admin.seco.jobs.services.jobadservice.infrastructure.mail;

class MailDataTestFactory {

    static MailData createDummyMailData() {
        return new MailData.Builder()
                .setFrom("from")
                .setTo("to")
                .setSubject("subject")
                .setBcc("bcc")
                .setCc("cc")
                .setSubject("subject")
                .setContent("content")
                .build();
    }
}
