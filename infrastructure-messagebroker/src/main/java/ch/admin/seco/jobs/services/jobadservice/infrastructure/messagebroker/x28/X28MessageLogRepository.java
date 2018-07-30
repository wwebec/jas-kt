package ch.admin.seco.jobs.services.jobadservice.infrastructure.messagebroker.x28;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface X28MessageLogRepository extends JpaRepository<X28MessageLog, String> {
}
