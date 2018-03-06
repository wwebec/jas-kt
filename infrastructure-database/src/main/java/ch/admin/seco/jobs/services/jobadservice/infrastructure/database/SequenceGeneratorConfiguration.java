package ch.admin.seco.jobs.services.jobadservice.infrastructure.database;

import org.apache.commons.lang3.NotImplementedException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.support.incrementer.DataFieldMaxValueIncrementer;
import org.springframework.jdbc.support.incrementer.H2SequenceMaxValueIncrementer;
import org.springframework.jdbc.support.incrementer.PostgreSQLSequenceMaxValueIncrementer;

import javax.sql.DataSource;

@Configuration
@ConditionalOnClass({DataSource.class})
@AutoConfigureAfter(DataSourceAutoConfiguration.class)
public class SequenceGeneratorConfiguration {
    private final DataSource dataSource;
    private final DataSourceProperties dataSourceProperties;
    private final String stellennummerEgovGeneratorSequenceName;

    public SequenceGeneratorConfiguration(DataSource dataSource,
                                          DataSourceProperties dataSourceProperties,
                                          @Value("${jobAdvertisement.stellennummerEgovGeneratorSequenceName:STELLEN_NUM_EGOV_SEQ}")
                                                  String egovNumberGeneratorSequenceName) {
        this.dataSource = dataSource;
        this.dataSourceProperties = dataSourceProperties;
        this.stellennummerEgovGeneratorSequenceName = egovNumberGeneratorSequenceName;
    }

    @Bean
    public DataFieldMaxValueIncrementer stellennummerEgovGenerator() {
        final String driverClassName = this.dataSourceProperties.determineDriverClassName();

        if (driverClassName.toUpperCase().contains("H2")) {
            return new H2SequenceMaxValueIncrementer(dataSource, stellennummerEgovGeneratorSequenceName);
        } else if (driverClassName.toUpperCase().contains("POSTGRESQL")) {
            return new PostgreSQLSequenceMaxValueIncrementer(dataSource, stellennummerEgovGeneratorSequenceName);
        } else {
            throw new NotImplementedException(String.format("Implementation is not found for: %s", driverClassName));
        }
    }

}
