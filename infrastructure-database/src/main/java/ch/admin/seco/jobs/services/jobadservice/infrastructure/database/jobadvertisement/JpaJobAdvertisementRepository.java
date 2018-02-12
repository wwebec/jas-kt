package ch.admin.seco.jobs.services.jobadservice.infrastructure.database.jobadvertisement;

import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisement;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisementId;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(propagation = Propagation.MANDATORY)
public class JpaJobAdvertisementRepository implements JobAdvertisementRepository {

    private final SpringDataJobAdvertisementRepository repository;

    @Autowired
    public JpaJobAdvertisementRepository(SpringDataJobAdvertisementRepository repository) {
        this.repository = repository;
    }

    @Override
    public JobAdvertisement findById(JobAdvertisementId jobAdvertisementId) {
        return this.repository.getOne(jobAdvertisementId);
    }

    @Override
    public JobAdvertisement save(JobAdvertisement jobAdvertisement) {
        return this.repository.save(jobAdvertisement);
    }

}
