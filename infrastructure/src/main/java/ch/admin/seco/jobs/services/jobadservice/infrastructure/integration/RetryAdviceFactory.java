package ch.admin.seco.jobs.services.jobadservice.infrastructure.integration;

import org.springframework.integration.handler.advice.RequestHandlerRetryAdvice;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

public class RetryAdviceFactory {

    private final int backOffPeriod;

    private final int maxAttempts;

    RetryAdviceFactory(int backOffPeriod, int maxAttempts) {
        this.backOffPeriod = backOffPeriod;
        this.maxAttempts = maxAttempts;
    }

    public RequestHandlerRetryAdvice retryAdvice() {
        RequestHandlerRetryAdvice requestHandlerRetryAdvice = new RequestHandlerRetryAdvice();
        FixedBackOffPolicy backOffPolicy = new FixedBackOffPolicy();
        backOffPolicy.setBackOffPeriod(backOffPeriod);
        RetryTemplate retryTemplate = new RetryTemplate();
        retryTemplate.setBackOffPolicy(backOffPolicy);
        retryTemplate.setRetryPolicy(new SimpleRetryPolicy(maxAttempts));
        requestHandlerRetryAdvice.setRetryTemplate(retryTemplate);
        return requestHandlerRetryAdvice;
    }
}
