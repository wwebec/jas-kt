package ch.admin.seco.jobs.services.jobadservice.infrastructure.mail;

import static ch.admin.seco.jobs.services.jobadservice.infrastructure.mail.MailDataTestFactory.createDummyMailSendingTaskData;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class MailSendingTaskTest {

    @Test
    public void shouldGetMailSenderDataDeserializeByteArrayPayloadIntoMailSenderData() {
        // given
        MailSendingTask.MailSendingTaskData mailSendingTaskData = createDummyMailSendingTaskData();
        MailSendingTask mailSendingTask = new MailSendingTask(mailSendingTaskData);

        // when
        MailSendingTask.MailSendingTaskData deserializeMailSenderData = mailSendingTask.getMailData();

        // then
        assertThat(deserializeMailSenderData).isEqualTo(mailSendingTaskData);
    }
}