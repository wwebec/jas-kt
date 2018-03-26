package ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement;

import ch.admin.seco.jobs.services.jobadservice.core.domain.events.AuditUser;
import ch.admin.seco.jobs.services.jobadservice.core.domain.events.DomainEventPublisher;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.events.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.support.incrementer.DataFieldMaxValueIncrementer;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class JobAdvertisementFactory {

    private final JobAdvertisementRepository jobAdvertisementRepository;
    private final DataFieldMaxValueIncrementer stellennummerEgovGenerator;
    private final AccessTokenGenerator accessTokenGenerator;

    @Autowired
    public JobAdvertisementFactory(JobAdvertisementRepository jobAdvertisementRepository,
                                   AccessTokenGenerator accessTokenGenerator,
                                   DataFieldMaxValueIncrementer stellennummerEgovGenerator) {
        this.jobAdvertisementRepository = jobAdvertisementRepository;
        this.accessTokenGenerator = accessTokenGenerator;
        this.stellennummerEgovGenerator = stellennummerEgovGenerator;
    }

    public JobAdvertisement createFromWebForm(JobAdvertisementCreator creator) {
        JobAdvertisement jobAdvertisement = new JobAdvertisement.Builder()
                .setId(new JobAdvertisementId())
                .setStatus(JobAdvertisementStatus.CREATED)
                .setSourceSystem(SourceSystem.JOBROOM)
                .setStellennummerEgov(stellennummerEgovGenerator.nextStringValue())
                .setReportingObligation(creator.isReportingObligation())
                .setReportToAvam(true)
                .setJobCenterCode(creator.getJobCenterCode())
                .setJobContent(creator.getJobContent())
                .setOwner(toOwner(creator.getAuditUser()))
                .setContact(creator.getContact())
                .setPublication(creator.getPublication())
                .build();

        JobAdvertisement newJobAdvertisement = jobAdvertisementRepository.save(jobAdvertisement);
        DomainEventPublisher.publish(new JobAdvertisementCreatedEvent(newJobAdvertisement));
        return newJobAdvertisement;
    }

    public JobAdvertisement createFromApi(JobAdvertisementCreator creator) {
        JobAdvertisement jobAdvertisement = new JobAdvertisement.Builder()
                .setId(new JobAdvertisementId())
                .setStatus(JobAdvertisementStatus.CREATED)
                .setSourceSystem(SourceSystem.JOBROOM)
                .setStellennummerEgov(stellennummerEgovGenerator.nextStringValue())
                .setReportingObligation(creator.isReportingObligation())
                .setReportToAvam(creator.isReportToAvam())
                .setJobCenterCode(creator.getJobCenterCode())
                .setJobContent(creator.getJobContent())
                .setOwner(toOwner(creator.getAuditUser()))
                .setContact(creator.getContact())
                .setPublication(creator.getPublication())
                .build();

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
                .setReportToAvam(true)
                .build();

        jobAdvertisement.init(updater);
        JobAdvertisement newJobAdvertisement = jobAdvertisementRepository.save(jobAdvertisement);
        DomainEventPublisher.publish(new JobAdvertisementRefiningEvent(newJobAdvertisement));
        return newJobAdvertisement;
    }

    public JobAdvertisement createFromExtern(Locale language, String title, String description, JobAdvertisementUpdater updater) {
        // TODO Tbd which data are passed to create the JobAdvertisement Object
        JobAdvertisement jobAdvertisement = new JobAdvertisement.Builder()
                .setId(new JobAdvertisementId())
                .setSourceSystem(SourceSystem.EXTERN)
                .setStatus(JobAdvertisementStatus.PUBLISHED_PUBLIC)
                .setLanguage(language)
                .setTitle(title)
                .setDescription(description)
                .setReportToAvam(false)
                .build();

        jobAdvertisement.init(updater);
        JobAdvertisement newJobAdvertisement = jobAdvertisementRepository.save(jobAdvertisement);
        DomainEventPublisher.publish(new JobAdvertisementPublishPublicEvent(newJobAdvertisement));
        return newJobAdvertisement;
    }

    private Owner toOwner(AuditUser auditUser) {
        if(auditUser != null) {
            return new Owner.Builder()
                    .setUserId(auditUser.getExternalId())
                    .setAccessToken(accessTokenGenerator.generateToken())
                    .build();
        } else {
            return new Owner.Builder()
                    .setAccessToken(accessTokenGenerator.generateToken())
                    .build();
        }
    }

}
