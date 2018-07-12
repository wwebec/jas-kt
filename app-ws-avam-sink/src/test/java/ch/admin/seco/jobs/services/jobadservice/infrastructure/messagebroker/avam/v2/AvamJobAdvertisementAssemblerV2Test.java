package ch.admin.seco.jobs.services.jobadservice.infrastructure.messagebroker.avam.v2;

import org.junit.Test;

import static ch.admin.seco.jobs.services.jobadservice.infrastructure.messagebroker.avam.v2.AvamJobAdvertisementAssemblerV2.fetchFirstEmail;
import static org.assertj.core.api.Assertions.assertThat;

public class AvamJobAdvertisementAssemblerV2Test {

    @Test
    public void testFetchFirstEmail() {
        String inputEmails0 = null;
        String inputEmails1 = "";
        String inputEmails2 = "asdf.ghjk@example.org";
        String inputEmails3 = "asdf.ghjk@example.org, test@example.org";
        String inputEmails4 = "asdf.ghjk@example.org, test@example.org, fdsa@example.org";

        assertThat(fetchFirstEmail(inputEmails0)).isNull();
        assertThat(fetchFirstEmail(inputEmails1)).isNull();
        assertThat(fetchFirstEmail(inputEmails2)).isEqualTo("asdf.ghjk@example.org");
        assertThat(fetchFirstEmail(inputEmails3)).isEqualTo("asdf.ghjk@example.org");
        assertThat(fetchFirstEmail(inputEmails4)).isEqualTo("asdf.ghjk@example.org");
    }

}