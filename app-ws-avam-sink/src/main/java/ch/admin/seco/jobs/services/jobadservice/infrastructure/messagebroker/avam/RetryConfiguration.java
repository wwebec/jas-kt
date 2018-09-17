package ch.admin.seco.jobs.services.jobadservice.infrastructure.messagebroker.avam;

import org.springframework.classify.BinaryExceptionClassifier;
import org.springframework.cloud.stream.annotation.StreamRetryTemplate;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.policy.AlwaysRetryPolicy;
import org.springframework.retry.policy.ExceptionClassifierRetryPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.ws.client.WebServiceIOException;
import org.springframework.ws.soap.SoapMessageException;

import java.util.Arrays;

@Configuration
public class RetryConfiguration {
    private final AvamProperties avamProperties;

    public RetryConfiguration(AvamProperties avamProperties) {
        this.avamProperties = avamProperties;
    }

    @StreamRetryTemplate
    public RetryTemplate sinkConsumerRetryTemplate() {
        final RetryTemplate retryTemplate = new RetryTemplate();
        retryTemplate.setRetryPolicy(retryPolicy());
        retryTemplate.setBackOffPolicy(backOffPolicy());

        return retryTemplate;
    }

    private ExceptionClassifierRetryPolicy retryPolicy() {
        final BinaryExceptionClassifier keepRetryingClassifier = new BinaryExceptionClassifier(Arrays.asList(
                SoapMessageException.class,
                WebServiceIOException.class
        ));
        keepRetryingClassifier.setTraverseCauses(true);

        final SimpleRetryPolicy simpleRetryPolicy = new SimpleRetryPolicy(avamProperties.getSimpleRetryMaxAttempts());
        final AlwaysRetryPolicy alwaysRetryPolicy = new AlwaysRetryPolicy();

        ExceptionClassifierRetryPolicy retryPolicy = new ExceptionClassifierRetryPolicy();
        retryPolicy.setExceptionClassifier(classifiable -> keepRetryingClassifier.classify(classifiable) ? alwaysRetryPolicy : simpleRetryPolicy);

        return retryPolicy;
    }

    private FixedBackOffPolicy backOffPolicy() {
        final FixedBackOffPolicy backOffPolicy = new FixedBackOffPolicy();
        backOffPolicy.setBackOffPeriod(avamProperties.getRetryBackOffPeriod());

        return backOffPolicy;
    }
}
