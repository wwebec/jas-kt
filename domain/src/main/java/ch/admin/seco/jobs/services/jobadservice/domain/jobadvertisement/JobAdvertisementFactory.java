package ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement;

import ch.admin.seco.jobs.services.jobadservice.core.conditions.Condition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.support.incrementer.DataFieldMaxValueIncrementer;
import org.springframework.stereotype.Component;

import ch.admin.seco.jobs.services.jobadservice.core.domain.events.AuditUser;
import ch.admin.seco.jobs.services.jobadservice.core.domain.events.DomainEventPublisher;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.events.JobAdvertisementCreatedEvent;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.events.JobAdvertisementPublishPublicEvent;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.events.JobAdvertisementApprovedEvent;

@Component
public class JobAdvertisementFactory {

    private final JobAdvertisementRepository jobAdvertisementRepository;
    private final AccessTokenGenerator accessTokenGenerator;
    private final DataFieldMaxValueIncrementer stellennummerEgovGenerator;

    @Autowired
    public JobAdvertisementFactory(JobAdvertisementRepository jobAdvertisementRepository,
                                   AccessTokenGenerator accessTokenGenerator,
                                   DataFieldMaxValueIncrementer stellennummerEgovGenerator) {
        this.jobAdvertisementRepository = jobAdvertisementRepository;
        this.accessTokenGenerator = accessTokenGenerator;
        this.stellennummerEgovGenerator = stellennummerEgovGenerator;
    }

    public JobAdvertisement createFromWebForm(JobAdvertisementCreator creator) {
        Condition.notNull(creator.getContact());
        JobAdvertisement jobAdvertisement = new JobAdvertisement.Builder()
                .setId(new JobAdvertisementId())
                .setStatus(JobAdvertisementStatus.CREATED)
                .setSourceSystem(SourceSystem.JOBROOM)
                .setExternalReference(creator.getExternalReference())
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
        Condition.notNull(creator.getContact());
        JobAdvertisement jobAdvertisement = new JobAdvertisement.Builder()
                .setId(new JobAdvertisementId())
                .setStatus(JobAdvertisementStatus.CREATED)
                .setSourceSystem(SourceSystem.API)
                .setExternalReference(creator.getExternalReference())
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

    public JobAdvertisement createFromAvam(JobAdvertisementCreator creator) {
        JobAdvertisement jobAdvertisement = new JobAdvertisement.Builder()
                .setId(new JobAdvertisementId())
                .setStatus(JobAdvertisementStatus.REFINING)
                .setSourceSystem(SourceSystem.RAV)
                .setStellennummerAvam(creator.getStellennummerAvam())
                .setReportingObligation(creator.isReportingObligation())
                .setReportingObligationEndDate(creator.getReportingObligationEndDate())
                .setReportToAvam(true)
                .setJobCenterCode(creator.getJobCenterCode())
                .setApprovalDate(creator.getApprovalDate())
                .setJobContent(creator.getJobContent())
                .setOwner(toOwner(creator.getAuditUser()))
                .setContact(creator.getContact())
                .setPublication(creator.getPublication())
                .build();

        JobAdvertisement newJobAdvertisement = jobAdvertisementRepository.save(jobAdvertisement);
        DomainEventPublisher.publish(new JobAdvertisementApprovedEvent(newJobAdvertisement));
        return newJobAdvertisement;
    }

    public JobAdvertisement createFromExtern(JobAdvertisementCreator creator) {
        JobAdvertisement jobAdvertisement = new JobAdvertisement.Builder()
                .setId(new JobAdvertisementId())
                .setStatus(JobAdvertisementStatus.PUBLISHED_PUBLIC)
                .setSourceSystem(SourceSystem.EXTERN)
                .setFingerprint(creator.getFingerprint())
                .setReportToAvam(false)
                .setJobContent(creator.getJobContent())
                .setOwner(toOwner(creator.getAuditUser()))
                .setContact(creator.getContact())
                .setPublication(creator.getPublication())
                .build();

        JobAdvertisement newJobAdvertisement = jobAdvertisementRepository.save(jobAdvertisement);
        DomainEventPublisher.publish(new JobAdvertisementPublishPublicEvent(newJobAdvertisement));
        return newJobAdvertisement;
    }

    private Owner toOwner(AuditUser auditUser) {
        if(auditUser != null) {
            return new Owner.Builder()
                    .setUserId(auditUser.getUserId())
                    .setCompanyId(auditUser.getCompanyId())
                    .setAccessToken(accessTokenGenerator.generateToken())
                    .build();
        } else {
            return new Owner.Builder()
                    .setAccessToken(accessTokenGenerator.generateToken())
                    .build();
        }
    }

}
