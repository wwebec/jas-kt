package ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.support.incrementer.DataFieldMaxValueIncrementer;
import org.springframework.stereotype.Component;

import ch.admin.seco.jobs.services.jobadservice.core.domain.events.DomainEventPublisher;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.events.JobAdvertisementCreatedEvent;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.events.JobAdvertisementPublishPublicEvent;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.events.JobAdvertisementRefiningEvent;

@Component
public class JobAdvertisementFactory {

    private final JobAdvertisementRepository jobAdvertisementRepository;
    private final DataFieldMaxValueIncrementer stellennummerEgovGenerator;

    @Autowired
    public JobAdvertisementFactory(JobAdvertisementRepository jobAdvertisementRepository,
            DataFieldMaxValueIncrementer stellennummerEgovGenerator) {
        this.jobAdvertisementRepository = jobAdvertisementRepository;
        this.stellennummerEgovGenerator = stellennummerEgovGenerator;
    }

    public JobAdvertisement createFromWebForm(Locale language, String title, String description, JobAdvertisementUpdater updater) {
        JobAdvertisement jobAdvertisement = new JobAdvertisement.Builder()
                .setId(new JobAdvertisementId())
                .setSourceSystem(SourceSystem.JOBROOM)
                .setStatus(JobAdvertisementStatus.CREATED)
                .setLanguage(language)
                .setTitle(title)
                .setDescription(description)
                .setStellennummerEgov(this.stellennummerEgovGenerator.nextStringValue())
                .setReportToRav(true)
                .build();

        jobAdvertisement.init(updater);
        JobAdvertisement newJobAdvertisement = jobAdvertisementRepository.save(jobAdvertisement);
        DomainEventPublisher.publish(new JobAdvertisementCreatedEvent(newJobAdvertisement));
        return newJobAdvertisement;
    }

    public JobAdvertisement createFromApi(Locale language, String title, String description, JobAdvertisementUpdater updater, boolean reportToRav) {
        JobAdvertisement jobAdvertisement = new JobAdvertisement.Builder()
                .setId(new JobAdvertisementId())
                .setSourceSystem(SourceSystem.API)
                .setStatus(JobAdvertisementStatus.CREATED)
                .setLanguage(language)
                .setTitle(title)
                .setDescription(description)
                .setStellennummerEgov(this.stellennummerEgovGenerator.nextStringValue())
                .setReportToRav(reportToRav)
                .build();

        jobAdvertisement.init(updater);
        JobAdvertisement newJobAdvertisement = jobAdvertisementRepository.save(jobAdvertisement);
        DomainEventPublisher.publish(new JobAdvertisementCreatedEvent(newJobAdvertisement));
        return newJobAdvertisement;
    }

    public JobAdvertisement createFromAvam(Locale language, String title, String description, JobAdvertisementUpdater updater) {
        // TODO Tbd which data are passed to create the JobAdvertisement Object
        JobAdvertisement jobAdvertisement = new JobAdvertisement.Builder()
                .setId(new JobAdvertisementId())
                .setSourceSystem(SourceSystem.RAV)
                .setStatus(JobAdvertisementStatus.REFINING)
                .setLanguage(language)
                .setTitle(title)
                .setDescription(description)
                .setReportToRav(true)
                .build();

        jobAdvertisement.init(updater);
        JobAdvertisement newJobAdvertisement = jobAdvertisementRepository.save(jobAdvertisement);
        DomainEventPublisher.publish(new JobAdvertisementRefiningEvent(newJobAdvertisement));
        return newJobAdvertisement;
    }

    public JobAdvertisement createFromExtern(Locale language, String title, String description, JobAdvertisementUpdater updater) {
        // TODO Tbd which data are passed to create the JobAdvertisement Object
        JobAdvertisement jobAdvertisement = jobAdvertisementRepository.findByFingerprint(updater.getFingerprint())
                .orElseGet(() ->
                        new JobAdvertisement.Builder()
                                .setId(new JobAdvertisementId())
                                .setSourceSystem(SourceSystem.EXTERN)
                                .setStatus(JobAdvertisementStatus.PUBLISHED_PUBLIC)
                                .setLanguage(language)
                                .setTitle(title)
                                .setDescription(description)
                                .setReportToRav(false)
                                .build());

        jobAdvertisement.init(updater);
        JobAdvertisement newJobAdvertisement = jobAdvertisementRepository.save(jobAdvertisement);
        DomainEventPublisher.publish(new JobAdvertisementPublishPublicEvent(newJobAdvertisement));
        return newJobAdvertisement;
    }
}
