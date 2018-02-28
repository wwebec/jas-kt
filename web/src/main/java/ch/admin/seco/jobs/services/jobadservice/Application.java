package ch.admin.seco.jobs.services.jobadservice;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

import ch.admin.seco.jobs.services.jobadservice.core.domain.events.DomainEventPublisher;

@SpringBootApplication
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
@EnableJpaRepositories
@EnableFeignClients
public class Application {

    @Autowired
    private DomainEventPublisher domainEventPublisher;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @PostConstruct
    public void domainEventPublisherRegistry() {
        DomainEventPublisher.set(domainEventPublisher);
    }

}
