package ch.admin.seco.jobs.services.jobadservice.infrastructure.web;

import ch.admin.seco.jobs.services.jobadservice.application.security.CurrentUserContext;
import ch.admin.seco.jobs.services.jobadservice.application.security.TestingCurrentUserContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class TestWebConfig {

    @Bean("testingCurrentUserContext")
    @Primary
    public CurrentUserContext currentUserContext() {
        return new TestingCurrentUserContext("junit");
    }

}
