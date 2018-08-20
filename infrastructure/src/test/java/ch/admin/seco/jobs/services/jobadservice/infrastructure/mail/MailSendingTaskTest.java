package ch.admin.seco.jobs.services.jobadservice.infrastructure.mail;

import static ch.admin.seco.jobs.services.jobadservice.infrastructure.mail.MailDataTestFactory.createDummyMailData;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class MailSendingTaskTest {

    @Test
    public void shouldGetMailSenderDataDeserializeByteArrayPayloadIntoMailSenderData() {
        // given
        MailData mailData = createDummyMailData();
        MailSendingTask mailSendingTask = new MailSendingTask(mailData);

        // when
        MailData deserializeMailSenderData = mailSendingTask.getMailData();

        // then
        assertThat(deserializeMailSenderData).isEqualTo(mailData);
    }
}