package ch.admin.seco.jobs.services.jobadservice.integration.x28.importer.config;

import static ch.admin.seco.jobs.services.jobadservice.infrastructure.messagebroker.messages.MessageHeaders.ACTION;
import static ch.admin.seco.jobs.services.jobadservice.infrastructure.messagebroker.messages.MessageHeaders.PARTITION_KEY;
import static ch.admin.seco.jobs.services.jobadservice.infrastructure.messagebroker.messages.MessageHeaders.RELEVANT_ID;
import static ch.admin.seco.jobs.services.jobadservice.infrastructure.messagebroker.messages.MessageHeaders.SOURCE_SYSTEM;
import static ch.admin.seco.jobs.services.jobadservice.infrastructure.messagebroker.messages.MessageHeaders.TARGET_SYSTEM;
import static ch.admin.seco.jobs.services.jobadservice.infrastructure.messagebroker.messages.MessageSystem.JOB_AD_SERVICE;
import static ch.admin.seco.jobs.services.jobadservice.infrastructure.messagebroker.messages.MessageSystem.X28;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.batch.item.ItemWriter;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.MessageChannel;

import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.create.CreateJobAdvertisementFromX28Dto;
import ch.admin.seco.jobs.services.jobadservice.infrastructure.messagebroker.messages.JobAdvertisementAction;
import ch.admin.seco.jobs.services.jobadservice.integration.x28.jobadimport.Oste;

public class X28JobAdWriter implements ItemWriter<Oste> {
    private static final Logger LOG = LoggerFactory.getLogger(X28JobAdWriter.class);
    private final MessageChannel output;

    private final JobAdvertisementDtoAssembler jobAdvertisementDtoAssembler;

    public X28JobAdWriter(MessageChannel output) {
        this.output = output;
        this.jobAdvertisementDtoAssembler = new JobAdvertisementDtoAssembler();
    }

    @Override
    public void write(List<? extends Oste> x28JobAdvertisements) {
        LOG.debug("Send x28 JobAdvertisements ({}) to JobAd service", x28JobAdvertisements.size());
        for (Oste x28JobAdvertisement : x28JobAdvertisements) {
            CreateJobAdvertisementFromX28Dto createFromX28 = jobAdvertisementDtoAssembler.createJobAdvertisementFromX28Dto(x28JobAdvertisement);
            send(createFromX28, createFromX28.getFingerprint());
        }
    }

    private void send(CreateJobAdvertisementFromX28Dto createFromX28, String key) {
        output.send(MessageBuilder
                .withPayload(createFromX28)
                .setHeader(PARTITION_KEY, key)
                .setHeader(RELEVANT_ID, key)
                .setHeader(ACTION, JobAdvertisementAction.CREATE_FROM_X28.name())
                .setHeader(SOURCE_SYSTEM, X28.name())
                .setHeader(TARGET_SYSTEM, JOB_AD_SERVICE.name())
                .build());
    }
}
