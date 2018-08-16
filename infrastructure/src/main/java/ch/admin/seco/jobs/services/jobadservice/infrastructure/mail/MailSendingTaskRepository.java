package ch.admin.seco.jobs.services.jobadservice.infrastructure.mail;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Transactional(propagation = Propagation.MANDATORY)
public interface MailSendingTaskRepository extends JpaRepository<MailSendingTask, String> {
}
