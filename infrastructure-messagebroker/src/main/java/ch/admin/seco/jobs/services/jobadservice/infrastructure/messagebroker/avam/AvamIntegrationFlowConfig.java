package ch.admin.seco.jobs.services.jobadservice.infrastructure.messagebroker.avam;

import java.util.Map;
import java.util.Optional;

import javax.persistence.EntityManagerFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.Pollers;
import org.springframework.integration.handler.GenericHandler;
import org.springframework.integration.jpa.dsl.Jpa;

import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisement;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisementRepository;
import ch.admin.seco.jobs.services.jobadservice.infrastructure.integration.IntegrationBasisConfig;
import ch.admin.seco.jobs.services.jobadservice.infrastructure.integration.RetryAdviceFactory;

@Configuration
@Import(IntegrationBasisConfig.class)
public class AvamIntegrationFlowConfig {

    private final Logger LOG = LoggerFactory.getLogger(AvamIntegrationFlowConfig.class);

    private final AvamService avamService;

    private final JobAdvertisementRepository jobAdvertisementRepository;

    private final EntityManagerFactory entityManagerFactory;

    private final RetryAdviceFactory retryAdviceFactory;

    public AvamIntegrationFlowConfig(AvamService avamService, JobAdvertisementRepository jobAdvertisementRepository, EntityManagerFactory entityManagerFactory, RetryAdviceFactory retryAdviceFactory) {
        this.avamService = avamService;
        this.jobAdvertisementRepository = jobAdvertisementRepository;
        this.entityManagerFactory = entityManagerFactory;
        this.retryAdviceFactory = retryAdviceFactory;
    }

    @Bean
    public IntegrationFlow retrievingGatewayFlow() {
        return IntegrationFlows
                .from(Jpa.inboundAdapter(this.entityManagerFactory)
                        .entityClass(AvamTask.class)
                        .jpaQuery("SELECT t FROM AvamTask t ORDER BY t.created ASC")
                        .maxResults(1)
                        .expectSingleResult(true)
                        .deleteAfterPoll(true), c -> c.poller(Pollers.fixedDelay(200).transactional()))
                .handle((GenericHandler<AvamTask>) this::sendToAvam, c -> c.advice(this.retryAdviceFactory.retryAdvice()))
                .get();
    }

    private Object sendToAvam(AvamTask avamTask, Map<String, Object> headers) {
        LOG.debug("Find AVAM-task for JobAdversiementId: '{}'", avamTask.getJobAdvertisementId().getValue());
        Optional<JobAdvertisement> jobAdvertisement = jobAdvertisementRepository.findById(avamTask.getJobAdvertisementId());
        if (jobAdvertisement.isPresent()) {
            switch (avamTask.getType()) {
                case REGISTER:
                    avamService.register(jobAdvertisement.get());
                    break;
                case DEREGISTER:
                    avamService.deregister(jobAdvertisement.get());
                    break;
                default:
                    throw new UnsupportedOperationException(avamTask.getType() + " unknown");
            }
        } else {
            LOG.error("Missing JobAdvertisementId '{}'. AVAM-Task will be removed", avamTask.getJobAdvertisementId().getValue());
        }
        return null;
    }
}
