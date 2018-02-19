package ch.admin.seco.jobs.services.jobadservice.domain.profession;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Transactional(propagation = Propagation.MANDATORY)
public interface ProfessionRepository extends JpaRepository<Profession, ProfessionId> {
}
