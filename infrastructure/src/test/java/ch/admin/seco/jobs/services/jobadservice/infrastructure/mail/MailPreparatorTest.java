package ch.admin.seco.jobs.services.jobadservice.infrastructure.mail;

import static ch.admin.seco.jobs.services.jobadservice.infrastructure.mail.MailDataTestFactory.createDummyMailSendingTaskData;
import static javax.mail.Message.RecipientType.BCC;
import static javax.mail.Message.RecipientType.CC;
import static javax.mail.Message.RecipientType.TO;
import static javax.mail.internet.InternetAddress.parse;
import static org.assertj.core.api.Assertions.assertThat;

import javax.mail.Address;
import javax.mail.Session;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.junit.Test;

public class MailPreparatorTest {

    @Test
    public void shouldPrepareMimeMessageWithMailData() throws Exception {
        //given
        MailSendingTask.MailSendingTaskData mailSendingTaskData = createDummyMailSendingTaskData();
        MailPreparator preparator = new MailPreparator(mailSendingTaskData);
        MimeMessage message = new MimeMessage((Session) null);

        //when
        preparator.prepare(message);

        //then
        assertThat(message.getFrom()).isEqualTo(InternetAddress.parse(mailSendingTaskData.getFrom()));
        assertThat(message.getReplyTo()).isEqualTo(InternetAddress.parse(mailSendingTaskData.getFrom()));
        assertThat(message.getRecipients(TO)).containsExactly(toAddresses(mailSendingTaskData.getTo()));
        assertThat(message.getRecipients(CC)).containsExactly(toAddresses(mailSendingTaskData.getCc()));
        assertThat(message.getRecipients(BCC)).containsExactly(toAddresses(mailSendingTaskData.getBcc()));
        assertThat(message.getSubject()).isEqualTo(mailSendingTaskData.getSubject());
        assertThat(message.getContent()).isEqualTo(mailSendingTaskData.getContent());
    }

    private Address[] toAddresses (String ... recipients) throws AddressException {
        InternetAddress[] addresses = new InternetAddress[recipients.length];
        for (int i = 0; i < addresses.length; i++) {
            addresses[i] =  parse(recipients[i])[0];
        }
        return addresses;
    }
}