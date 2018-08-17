package ch.admin.seco.jobs.services.jobadservice.infrastructure.mail;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Locale;

import org.junit.Test;

import ch.admin.seco.jobs.services.jobadservice.application.MailSenderData;

public class MailSendingTaskTest {

    @Test
    public void shouldGetMailSenderDataDeserializeByteArrayPayloadIntoMailSenderData() {
        MailSenderData mailSenderData = createDummyMailSenderData();
        MailSendingTask mailSendingTask = new MailSendingTask(mailSenderData);

        MailSenderData deserializeMailSenderData = mailSendingTask.getMailSenderData();

        assertThat(deserializeMailSenderData).isEqualTo(mailSenderData);
    }

    private MailSenderData createDummyMailSenderData() {
        return new MailSenderData.Builder()
                .setFrom("from")
                .setTo("to")
                .setSubject("subject")
                .setTemplateName("templateName")
                .setLocale(Locale.getDefault())
                .build();
    }
}