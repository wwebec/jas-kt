package ch.admin.seco.jobs.services.jobadservice;

import static ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.ProfileRegistry.CLOUD;
import static ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.ProfileRegistry.DEV;
import static ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.ProfileRegistry.PROD;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Collection;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

import ch.admin.seco.jobs.services.jobadservice.core.domain.events.DomainEventPublisher;
import ch.admin.seco.jobs.services.jobadservice.infrastructure.web.config.DefaultProfileUtil;

@SpringBootApplication
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
@EnableJpaRepositories
@EnableFeignClients
@EnableScheduling
@EnableDiscoveryClient
public class Application {

    private static final Logger log = LoggerFactory.getLogger(Application.class);

    private final Environment env;
    private final DomainEventPublisher domainEventPublisher;

    public Application(Environment env, DomainEventPublisher domainEventPublisher) {
        this.env = env;
        this.domainEventPublisher = domainEventPublisher;
    }

    /**
     * Main method, used to run the application.
     *
     * @param args the command line arguments
     * @throws UnknownHostException if the local host name could not be resolved into an address
     */
    public static void main(String[] args) throws UnknownHostException {
        SpringApplication app = new SpringApplication(Application.class);
        DefaultProfileUtil.addDefaultProfile(app);
        Environment env = app.run(args).getEnvironment();
        displayAppStartInfo(env);
    }

    private static void displayAppStartInfo(Environment env) throws UnknownHostException {
        String protocol = "http";
        if (env.getProperty("server.ssl.key-store") != null) {
            protocol = "https";
        }
        log.info("\n----------------------------------------------------------\n\t" +
                        "Application '{}' is running! Access URLs:\n\t" +
                        "Local: \t\t{}://localhost:{}\n\t" +
                        "External: \t{}://{}:{}\n\t" +
                        "Profile(s): \t{}\n----------------------------------------------------------",
                env.getProperty("spring.application.name"),
                protocol,
                env.getProperty("server.port"),
                protocol,
                InetAddress.getLocalHost().getHostAddress(),
                env.getProperty("server.port"),
                DefaultProfileUtil.getActiveProfiles(env));

        String configServerStatus = env.getProperty("configserver.status");
        log.info("\n----------------------------------------------------------\n\t" +
                        "Config Server: \t{}\n----------------------------------------------------------",
                configServerStatus == null ? "Not found or not setup for this application" : configServerStatus);
    }

    /**
     * Initializes job-ad-service.
     *
     * Spring profiles can be configured with a program arguments --spring.profiles.active=your-active-profile
     *
     * You can find more information on how profiles work with JHipster on <a href="http://www.jhipster.tech/profiles/">http://www.jhipster.tech/profiles/</a>.
     */
    @PostConstruct
    public void initApplication() {
        Collection<String> activeProfiles = Arrays.asList(env.getActiveProfiles());
        if (activeProfiles.contains(DEV) && activeProfiles.contains(PROD)) {
            log.error("You have misconfigured your application! It should not run " +
                    "with both the 'dev' and 'prod' profiles at the same time.");
        }
        if (activeProfiles.contains(DEV) && activeProfiles.contains(CLOUD)) {
            log.error("You have misconfigured your application! It should not " +
                    "run with both the 'dev' and 'cloud' profiles at the same time.");
        }
    }
}
