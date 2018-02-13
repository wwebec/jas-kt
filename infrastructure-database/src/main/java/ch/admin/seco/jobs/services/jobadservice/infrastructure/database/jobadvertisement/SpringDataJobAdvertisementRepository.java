package ch.admin.seco.jobs.services.jobadservice.infrastructure.database.jobadvertisement;

import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisement;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisementId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpringDataJobAdvertisementRepository extends JpaRepository<JobAdvertisement, JobAdvertisementId> {
}
