package ch.admin.seco.jobs.services.jobadservice.infrastructure.messagebroker.avam;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AvamTaskRepository extends JpaRepository<AvamTask, AvamTaskId> {
}
