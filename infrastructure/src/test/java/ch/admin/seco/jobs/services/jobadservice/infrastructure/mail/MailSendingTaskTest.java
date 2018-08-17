package ch.admin.seco.jobs.services.jobadservice.infrastructure.mail;

import static ch.admin.seco.jobs.services.jobadservice.infrastructure.mail.MailSenderTestDataFactory.createDummyMailSenderData;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import ch.admin.seco.jobs.services.jobadservice.application.MailSenderData;

public class MailSendingTaskTest {

    @Test
    public void shouldGetMailSenderDataDeserializeByteArrayPayloadIntoMailSenderData() {
        // given
        MailSenderData mailSenderData = createDummyMailSenderData();
        MailSendingTask mailSendingTask = new MailSendingTask(mailSenderData);

        // when
        MailSenderData deserializeMailSenderData = mailSendingTask.getMailSenderData();

        // then
        assertThat(deserializeMailSenderData).isEqualTo(mailSenderData);
    }
}