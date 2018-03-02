package ch.admin.seco.jobs.services.jobadservice.infrastructure.messagebroker;

import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.Optional;

import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Processor;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.MessageChannel;

import ch.admin.seco.jobs.services.jobadservice.application.RavRegistrationService;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.JobAdvertisementApplicationService;
import ch.admin.seco.jobs.services.jobadservice.application.profession.ProfessionApplicationService;
import ch.admin.seco.jobs.services.jobadservice.core.domain.events.DomainEventPublisher;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisement;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisementEvent;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.Occupation;
import ch.admin.seco.jobs.services.jobadservice.domain.profession.Profession;
import ch.admin.seco.jobs.services.jobadservice.infrastructure.messagebroker.messages.DeregisterJobAdvertisementMessage;
import ch.admin.seco.jobs.services.jobadservice.infrastructure.messagebroker.messages.RegisterJobAdvertisementMessage;

public class AvamService implements RavRegistrationService {

    private final DomainEventPublisher domainEventPublisher;

    private final JobAdvertisementApplicationService jobAdvertisementApplicationService;
    private final ProfessionApplicationService professionApplicationService;

    private final MessageChannel output;

    public AvamService(DomainEventPublisher domainEventPublisher, JobAdvertisementApplicationService jobAdvertisementApplicationService, ProfessionApplicationService professionApplicationService, MessageChannel output) {
        this.domainEventPublisher = domainEventPublisher;
        this.jobAdvertisementApplicationService = jobAdvertisementApplicationService;
        this.professionApplicationService = professionApplicationService;
        this.output = output;
    }

    @Override
    public void register(JobAdvertisement jobAdvertisement) {
        List<Profession> professionCodes = resolveAVAMProfessionCodes(jobAdvertisement);
        output.send(MessageBuilder
                .withPayload(new RegisterJobAdvertisementMessage(jobAdvertisement, professionCodes))
                .setHeader("action", "register")
                .build());
    }

    @Override
    public void deregister(JobAdvertisement jobAdvertisement) {
        output.send(MessageBuilder
                .withPayload(new DeregisterJobAdvertisementMessage(jobAdvertisement))
                .setHeader("action", "deregister")
                .build());
    }

    @StreamListener(target = Processor.INPUT, condition = "headers['event']=='JOB_ADVERTISEMENT_APPROVED'")
    public void handleApprovedEvent(JobAdvertisementEvent jobAdvertisementEvent) {
        domainEventPublisher.publishEvent(jobAdvertisementEvent);
    }

    @StreamListener(target = Processor.INPUT, condition = "headers['event']=='JOB_ADVERTISEMENT_REJECTED'")
    public void handleRejectedEvent(JobAdvertisementEvent jobAdvertisementEvent) {
        domainEventPublisher.publishEvent(jobAdvertisementEvent);
    }

    private List<Profession> resolveAVAMProfessionCodes(JobAdvertisement jobAdvertisement) {
        return jobAdvertisement.getOccupations().stream()
                .map(Occupation::getProfessionId)
                .map(professionid -> professionApplicationService.getProfessionCode(professionid))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(toList());
    }
}
