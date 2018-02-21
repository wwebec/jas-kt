package ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement;

import ch.admin.seco.jobs.services.jobadservice.core.domain.events.DomainEventPublisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class JobAdvertisementFactory {

    private final JobAdvertisementRepository jobAdvertisementRepository;

    @Autowired
    public JobAdvertisementFactory(JobAdvertisementRepository jobAdvertisementRepository) {
        this.jobAdvertisementRepository = jobAdvertisementRepository;
    }

    public JobAdvertisement createFromWebForm(String title, String description, JobAdvertisementUpdater updater) {
        JobAdvertisement jobAdvertisement = new JobAdvertisement(
                new JobAdvertisementId(),
                SourceSystem.JOBROOM,
                JobAdvertisementStatus.CREATED,
                title,
                description
        );
        jobAdvertisement.init(updater);
        JobAdvertisement newJobAdvertisement = jobAdvertisementRepository.save(jobAdvertisement);
        DomainEventPublisher.publish(new JobAdvertisementEvent(JobAdvertisementEvents.JOB_ADVERTISEMENT_CREATED, newJobAdvertisement));
        return newJobAdvertisement;
    }

    public JobAdvertisement createFromApi(String title, String description, JobAdvertisementUpdater updater) {
        JobAdvertisement jobAdvertisement = new JobAdvertisement(
                new JobAdvertisementId(),
                SourceSystem.API,
                JobAdvertisementStatus.CREATED,
                title,
                description
        );
        jobAdvertisement.init(updater);
        JobAdvertisement newJobAdvertisement = jobAdvertisementRepository.save(jobAdvertisement);
        DomainEventPublisher.publish(new JobAdvertisementEvent(JobAdvertisementEvents.JOB_ADVERTISEMENT_CREATED, newJobAdvertisement));
        return newJobAdvertisement;
    }

    public JobAdvertisement createFromAvam(String title, String description, JobAdvertisementUpdater updater) {
        JobAdvertisement jobAdvertisement = new JobAdvertisement(
                new JobAdvertisementId(),
                SourceSystem.RAV,
                JobAdvertisementStatus.APPROVED,
                title,
                description
        );
        jobAdvertisement.init(updater);
        JobAdvertisement newJobAdvertisement = jobAdvertisementRepository.save(jobAdvertisement);
        DomainEventPublisher.publish(new JobAdvertisementEvent(JobAdvertisementEvents.JOB_ADVERTISEMENT_APPROVED, newJobAdvertisement));
        return newJobAdvertisement;
    }

    public JobAdvertisement createFromExtern(String title, String description, JobAdvertisementUpdater updater) {
        JobAdvertisement jobAdvertisement = new JobAdvertisement(
                new JobAdvertisementId(),
                SourceSystem.EXTERN,
                JobAdvertisementStatus.PUBLISHED_PUBLIC,
                title,
                description
        );
        jobAdvertisement.init(updater);
        JobAdvertisement newJobAdvertisement = jobAdvertisementRepository.save(jobAdvertisement);
        DomainEventPublisher.publish(new JobAdvertisementEvent(JobAdvertisementEvents.JOB_ADVERTISEMENT_PUBLISHED_PUBLIC, newJobAdvertisement));
        return newJobAdvertisement;
    }
}
