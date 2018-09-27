package ch.admin.seco.jobs.services.jobadservice.integration.x28.importer.config;

import static ch.admin.seco.jobs.services.jobadservice.infrastructure.messagebroker.messages.MessageHeaders.ACTION;
import static ch.admin.seco.jobs.services.jobadservice.infrastructure.messagebroker.messages.MessageHeaders.PARTITION_KEY;
import static ch.admin.seco.jobs.services.jobadservice.infrastructure.messagebroker.messages.MessageHeaders.PAYLOAD_TYPE;
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

import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.x28.X28CreateJobAdvertisementDto;
import ch.admin.seco.jobs.services.jobadservice.infrastructure.messagebroker.messages.JobAdvertisementAction;

public class X28JobAdWriter implements ItemWriter<X28CreateJobAdvertisementDto> {

    private static final Logger LOG = LoggerFactory.getLogger(X28JobAdWriter.class);

    private final MessageChannel output;

    public X28JobAdWriter(MessageChannel output) {
        this.output = output;
    }

    @Override
    public void write(List<? extends X28CreateJobAdvertisementDto> items) {
        LOG.debug("Send x28 JobAdvertisements ({}) to JobAd service", items.size());
        items.forEach(item -> send(item, item.getFingerprint()));
    }

    private void send(X28CreateJobAdvertisementDto createFromX28, String key) {
        output.send(MessageBuilder
                .withPayload(createFromX28)
                .setHeader(PARTITION_KEY, key)
                .setHeader(RELEVANT_ID, key)
                .setHeader(ACTION, JobAdvertisementAction.CREATE_FROM_X28.name())
                .setHeader(SOURCE_SYSTEM, X28.name())
                .setHeader(TARGET_SYSTEM, JOB_AD_SERVICE.name())
                .setHeader(PAYLOAD_TYPE, createFromX28.getClass().getSimpleName())
                .build());
    }
}
