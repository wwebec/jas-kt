package ch.admin.seco.jobs.services.jobadservice.infrastructure.elasticsearch.write.apiuser;

import ch.admin.seco.jobs.services.jobadservice.domain.apiuser.ApiUser;
import ch.admin.seco.jobs.services.jobadservice.domain.apiuser.ApiUserRepository;
import ch.admin.seco.jobs.services.jobadservice.domain.apiuser.events.ApiUserEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.Optional;

import static ch.admin.seco.jobs.services.jobadservice.domain.apiuser.events.ApiUserEvents.*;

@Component("api-user-event-listener")
public class IndexerEventListener {
    private static Logger LOG = LoggerFactory.getLogger(IndexerEventListener.class);

    private ApiUserRepository apiUserRepository;
    private ApiUserElasticsearchRepository apiUserElasticsearchRepository;

    @Autowired
    public IndexerEventListener(ApiUserRepository apiUserRepository,
                                ApiUserElasticsearchRepository apiUserElasticsearchRepository) {
        this.apiUserRepository = apiUserRepository;
        this.apiUserElasticsearchRepository = apiUserElasticsearchRepository;
    }

    @TransactionalEventListener
    public void handle(ApiUserEvent event) {
        if (hasToUpdate(event)) {
            Optional<ApiUser> apiUserOptional = this.apiUserRepository.findById(event.getAggregateId());
            if (apiUserOptional.isPresent()) {
                this.apiUserElasticsearchRepository.save(new ApiUserDocument(apiUserOptional.get()));
            } else {
                LOG.warn("ApiUser not found for the given id: {}", event.getAggregateId());
            }
        }
    }

    private boolean hasToUpdate(ApiUserEvent event) {
        if (API_USER_CREATED.getDomainEventType().equals(event.getDomainEventType())) {
            return true;
        }
        if (API_USER_UPDATED_DETAILS.getDomainEventType().equals(event.getDomainEventType())) {
            return true;
        }
        if (API_USER_UPDATED_STATUS.getDomainEventType().equals(event.getDomainEventType())) {
            return true;
        }
        return false;
    }

}
