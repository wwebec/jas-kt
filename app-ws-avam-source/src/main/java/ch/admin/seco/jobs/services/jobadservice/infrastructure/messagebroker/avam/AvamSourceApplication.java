package ch.admin.seco.jobs.services.jobadservice.infrastructure.messagebroker.avam;

import static java.util.Collections.singletonMap;
import static org.springframework.core.env.AbstractEnvironment.DEFAULT_PROFILES_PROPERTY_NAME;

import java.util.HashMap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;

import ch.admin.seco.jobs.services.jobadservice.application.ProfileRegistry;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = {
        DataSourceAutoConfiguration.class,
        JpaRepositoriesAutoConfiguration.class,
        HibernateJpaAutoConfiguration.class,
        SecurityAutoConfiguration.class
})
public class AvamSourceApplication {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(AvamSourceApplication.class);
        app.setDefaultProperties(new HashMap<>(singletonMap(DEFAULT_PROFILES_PROPERTY_NAME, ProfileRegistry.AVAM_WSDL_V2)));
        app.run(args);
    }
}
