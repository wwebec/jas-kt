package ch.admin.seco.jobs.services.jobadservice.infrastructure.mail;

class MailDataTestFactory {

    static MailSendingTask.MailSendingTaskData createDummyMailSendingTaskData() {
        return MailSendingTask.builder()
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
