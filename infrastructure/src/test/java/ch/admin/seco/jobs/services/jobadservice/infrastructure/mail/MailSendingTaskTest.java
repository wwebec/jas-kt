package ch.admin.seco.jobs.services.jobadservice.infrastructure.mail;

import static ch.admin.seco.jobs.services.jobadservice.infrastructure.mail.MailSenderDataFactory.createDummyMailSenderData;
import static org.assertj.core.api.Assertions.assertThat;

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
}