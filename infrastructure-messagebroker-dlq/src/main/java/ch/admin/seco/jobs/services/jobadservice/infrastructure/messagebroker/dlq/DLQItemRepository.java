package ch.admin.seco.jobs.services.jobadservice.infrastructure.messagebroker.dlq;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Transactional(propagation = Propagation.REQUIRED)
public interface DLQItemRepository extends JpaRepository<DLQItem, String> {

    @Query("Select i from DLQItem i where i.relevantId = :relevantId")
    List<DLQItem> findByRelevantId(String relevantId);
}
