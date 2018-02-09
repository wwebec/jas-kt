package ch.admin.seco.jobs.services.jobadservice.infrastructure.database.eventstore;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackageClasses = StoredEventRepository.class)
class EventStoreConfig {

    @Bean
    public JpaBasedEventStore eventStore(StoredEventRepository storedEventRepository) {
        return new JpaBasedEventStore(storedEventRepository);
    }

    @Bean
    public DomainEventPersistenceListener domainEventPersistenceListener(StoredEventRepository storedEventRepository) {
        return new DomainEventPersistenceListener(eventStoreObjectMapper(), storedEventRepository);
    }

    static ObjectMapper eventStoreObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.registerModule(new JavaTimeModule());
        return mapper;
    }
}
