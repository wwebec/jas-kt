package ch.admin.seco.jobs.services.jobadservice;

import ch.admin.seco.jobs.services.jobadservice.application.LocalityService;
import ch.admin.seco.jobs.services.jobadservice.application.RavRegistrationService;
import ch.admin.seco.jobs.services.jobadservice.application.ReportingObligationService;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"ch.admin.seco.jobs.services.jobadservice.application", "ch.admin.seco.jobs.services.jobadservice.domain"})
public class TestApplicationConfig {

    @MockBean
    private RavRegistrationService ravRegistrationService;

    @MockBean
    private ReportingObligationService reportingObligationService;

    @MockBean
    private LocalityService localityService;

}
