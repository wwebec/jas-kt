package ch.admin.seco.jobs.services.jobadservice.infrastructure.web.eureka;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.boot.actuate.health.CompositeHealthIndicator;
import org.springframework.boot.actuate.health.HealthAggregator;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.cloud.netflix.eureka.EurekaHealthCheckHandler;
import org.springframework.util.ReflectionUtils;

class IgnoreAwareEurekaHealthCheckHandler extends EurekaHealthCheckHandler {

    private static final String INDICATORS_FIELD_NAME = "indicators";

    private final List<String> ignoredHealthIndicators = new ArrayList<>();

    IgnoreAwareEurekaHealthCheckHandler(HealthAggregator healthAggregator, List<String> ignoredHealthIndicators) {
        super(healthAggregator);
        this.ignoredHealthIndicators.addAll(
                ignoredHealthIndicators.stream()
                        .map(String::toLowerCase)
                        .collect(Collectors.toList())
        );
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        super.afterPropertiesSet();
        Map<String, HealthIndicator> healthIndicators = getHealthIndicatorMap(super.getHealthIndicator());
        healthIndicators.keySet()
                .removeIf(healthIndicator -> ignoredHealthIndicators.contains(healthIndicator.toLowerCase()));
    }

    private Map<String, HealthIndicator> getHealthIndicatorMap(CompositeHealthIndicator healthIndicator) {
        Field indicatorsFields = ReflectionUtils.findField(CompositeHealthIndicator.class, INDICATORS_FIELD_NAME);
        if (indicatorsFields == null) {
            throw new IllegalStateException("Could not find the field 'indicators' on class: " + CompositeHealthIndicator.class);
        }
        indicatorsFields.setAccessible(true);
        Object field = ReflectionUtils.getField(indicatorsFields, healthIndicator);
        return (Map<String, HealthIndicator>) field;
    }
}
