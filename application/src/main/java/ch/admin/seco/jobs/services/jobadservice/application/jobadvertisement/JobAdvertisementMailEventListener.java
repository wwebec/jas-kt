package ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement;

import ch.admin.seco.jobs.services.jobadservice.application.MailSenderData;
import ch.admin.seco.jobs.services.jobadservice.application.MailSenderService;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisement;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisementEvent;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;

import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisementEvents.*;

public class JobAdvertisementMailEventListener {

    public static final String JOB_ADVERTISEMENT_CREATED_TEMPLATE = "JobAdCreatedMail.html";
    public static final String JOB_ADVERTISEMENT_APPROVED_TEMPLATE = "JobAdApprovedMail.html";
    public static final String JOB_ADVERTISEMENT_REJECTED_TEMPLATE = "JobAdRejectedMail.html";
    public static final String JOB_ADVERTISEMENT_CANCELLED_TEMPLATE = "JobAdCancelledMail.html";

    private final JobAdvertisementRepository jobAdvertisementRepository;
    private final MailSenderService mailSenderService;

    @Autowired
    public JobAdvertisementMailEventListener(JobAdvertisementRepository jobAdvertisementRepository, MailSenderService mailSenderService) {
        this.jobAdvertisementRepository = jobAdvertisementRepository;
        this.mailSenderService = mailSenderService;
    }

    @EventListener
    void onCreated(JobAdvertisementEvent jobAdvertisementEvent) {
        if (!JOB_ADVERTISEMENT_CREATED.getDomainEventType().equals(jobAdvertisementEvent.getDomainEventType())) {
            return;
        }
        final JobAdvertisement jobAdvertisement = jobAdvertisementRepository.getOne(jobAdvertisementEvent.getAggregateId());
        mailSenderService.send(
                new MailSenderData.Builder()
                        .setTo(jobAdvertisement.getContact().getEmail())
                        .setSubject(null)
                        .setTemplateName(JOB_ADVERTISEMENT_CREATED_TEMPLATE)
                        .setTemplateVariables(null)
                        .setLocale(null)
                        .build()
        );
    }

    @EventListener
    void onApproved(JobAdvertisementEvent jobAdvertisementEvent) {
        if (!JOB_ADVERTISEMENT_APPROVED.getDomainEventType().equals(jobAdvertisementEvent.getDomainEventType())) {
            return;
        }
        final JobAdvertisement jobAdvertisement = jobAdvertisementRepository.getOne(jobAdvertisementEvent.getAggregateId());
    }

    @EventListener
    void onRejected(JobAdvertisementEvent jobAdvertisementEvent) {
        if (!JOB_ADVERTISEMENT_REJECTED.getDomainEventType().equals(jobAdvertisementEvent.getDomainEventType())) {
            return;
        }
        final JobAdvertisement jobAdvertisement = jobAdvertisementRepository.getOne(jobAdvertisementEvent.getAggregateId());
    }

    @EventListener
    void onCancelled(JobAdvertisementEvent jobAdvertisementEvent) {
        if (!JOB_ADVERTISEMENT_CANCELLED.getDomainEventType().equals(jobAdvertisementEvent.getDomainEventType())) {
            return;
        }
        final JobAdvertisement jobAdvertisement = jobAdvertisementRepository.getOne(jobAdvertisementEvent.getAggregateId());
    }

}
