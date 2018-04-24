package ch.admin.seco.jobs.services.jobadservice.infrastructure.database.eventstore;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Transactional(propagation = Propagation.MANDATORY)
public interface StoredEventRepository extends JpaRepository<StoredEvent, String>, StoredEventRepositoryCustom {

    @Query("select e from StoredEvent e where e.aggregateId = :aggregateId and e.aggregateType = :aggregateType order by e.registrationTime desc")
    Page<StoredEvent> findByAggregateId(@Param("aggregateId") String aggregateId, @Param("aggregateType") String aggregateType, Pageable pageable);
}
