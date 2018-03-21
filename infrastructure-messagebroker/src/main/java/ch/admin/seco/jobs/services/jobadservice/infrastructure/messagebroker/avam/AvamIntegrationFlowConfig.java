package ch.admin.seco.jobs.services.jobadservice.infrastructure.messagebroker.avam;

import ch.admin.seco.jobs.services.jobadservice.application.RavRegistrationService;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisement;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisementRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.Pollers;
import org.springframework.integration.jpa.dsl.Jpa;

import javax.persistence.EntityManagerFactory;
import java.util.Map;
import java.util.Optional;

@Configuration
public class AvamIntegrationFlowConfig {

    private final Logger LOG = LoggerFactory.getLogger(AvamIntegrationFlowConfig.class);

    private final RavRegistrationService ravRegistrationService;

    private final JobAdvertisementRepository jobAdvertisementRepository;

    private final EntityManagerFactory entityManagerFactory;

    public AvamIntegrationFlowConfig(RavRegistrationService ravRegistrationService, JobAdvertisementRepository jobAdvertisementRepository, EntityManagerFactory entityManagerFactory) {
        this.ravRegistrationService = ravRegistrationService;
        this.jobAdvertisementRepository = jobAdvertisementRepository;
        this.entityManagerFactory = entityManagerFactory;
    }

    @Bean
    public IntegrationFlow retrievingGatewayFlow() {
        return IntegrationFlows
                .from(Jpa.inboundAdapter(this.entityManagerFactory)
                        .entityClass(AvamTask.class)
                        .jpaQuery("SELECT t FROM AvamTask t ORDER BY t.created ASC")
                        .maxResults(1)
                        .expectSingleResult(true)
                        .deleteAfterPoll(true), c -> c.poller(Pollers.fixedRate(1000).transactional()))
                .handle(this::sendToAvam)
                .get();
    }

    private Object sendToAvam(AvamTask avamTask, Map<String, Object> headers) {
        LOG.debug("Find AVAM-task for JobAdversiementId: '{}'", avamTask.getJobAdvertisementId().getValue());
        Optional<JobAdvertisement> jobAdvertisement = jobAdvertisementRepository.findById(avamTask.getJobAdvertisementId());
        if (jobAdvertisement.isPresent()) {
            switch (avamTask.getType()) {
                case REGISTER:
                    ravRegistrationService.register(jobAdvertisement.get());
                    break;
                case DEREGISTER:
                    ravRegistrationService.deregister(jobAdvertisement.get());
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
