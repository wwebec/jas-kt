package ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class AccessTokenGeneratorTest {

    @Test
    public void testGenerateToken() {
        AccessTokenGenerator generator = new AccessTokenGenerator();

        String token = generator.generateToken();

        assertThat(token)
                .isNotNull()
                .hasSize(24);
    }

}