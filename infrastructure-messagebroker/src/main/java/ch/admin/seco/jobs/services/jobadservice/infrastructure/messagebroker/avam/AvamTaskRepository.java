package ch.admin.seco.jobs.services.jobadservice.infrastructure.messagebroker.avam;

import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisementId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AvamTaskRepository extends JpaRepository<AvamTask, AvamTaskId> {
}
