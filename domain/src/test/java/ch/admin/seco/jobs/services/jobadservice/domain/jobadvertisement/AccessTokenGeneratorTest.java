package ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement;

import ch.admin.seco.jobs.services.jobadservice.core.time.TimeMachine;
import org.junit.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

public class AccessTokenGeneratorTest {

    @Test
    public void testGenerateToken() {
        TimeMachine.useFixedClockAt(LocalDateTime.of(2018, 1, 1, 1, 2, 3, 4));
        AccessTokenGenerator generator = new AccessTokenGenerator();

        String token = generator.generateToken();

        assertThat(token).isNotNull();
        assertThat(token).isEqualTo("tq0khqnjnFjyTEmPGnxgQA==");
    }

}