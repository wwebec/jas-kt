package ch.admin.seco.jobs.services.jobadservice.integration.x28.importer.config;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.afterburner.AfterburnerModule;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonConfig {

    /*
     * Jackson Afterburner module to speed up serialization/deserialization.
     */
    @Bean
    public AfterburnerModule afterburnerModule() {
        return new AfterburnerModule();
    }

    @Bean
    public Jdk8Module jdk8Module() {
        return new Jdk8Module();
    }

    /*
     * AutoConfiguration of ObjectMapper only works with the spring-web library on the classpath.
     */
    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(afterburnerModule());
        objectMapper.registerModule(jdk8Module());
        objectMapper.registerModule(javaTimeModule());
        objectMapper.configure(MapperFeature.DEFAULT_VIEW_INCLUSION, false);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        objectMapper.configure(JsonGenerator.Feature.WRITE_NUMBERS_AS_STRINGS, true);
        return objectMapper;
    }

    @Bean
    JavaTimeModule javaTimeModule() {
        return new JavaTimeModule();
    }

}
