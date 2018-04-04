package ch.admin.seco.jobs.services.jobadservice.integration.x28.importer.config;

import static ch.admin.seco.jobs.services.jobadservice.infrastructure.messagebroker.messages.JobAdvertisementAction.CREATE_FROM_X28;
import static ch.admin.seco.jobs.services.jobadservice.infrastructure.messagebroker.messages.JobAdvertisementAction.UPDATE_FROM_X28;
import static ch.admin.seco.jobs.services.jobadservice.infrastructure.messagebroker.messages.MessageHeaders.ACTION;
import static ch.admin.seco.jobs.services.jobadservice.infrastructure.messagebroker.messages.MessageHeaders.SOURCE_SYSTEM;
import static ch.admin.seco.jobs.services.jobadservice.infrastructure.messagebroker.messages.MessageHeaders.TARGET_SYSTEM;
import static ch.admin.seco.jobs.services.jobadservice.infrastructure.messagebroker.messages.MessageSystem.JOB_AD_SERVICE;
import static ch.admin.seco.jobs.services.jobadservice.infrastructure.messagebroker.messages.MessageSystem.X28;
import static org.springframework.util.StringUtils.isEmpty;

import java.util.List;

import org.springframework.batch.item.ItemWriter;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.MessageChannel;

import ch.admin.seco.jobs.services.jobadservice.infrastructure.messagebroker.messages.JobAdvertisementAction;
import ch.admin.seco.jobs.services.jobadservice.integration.x28.jobadimport.Oste;

public class X28JobAdWriter implements ItemWriter<Oste> {
    private final MessageChannel output;

    private final JobAdvertisementDtoAssembler jobAdvertisementDtoAssembler;

    public X28JobAdWriter(MessageChannel output) {
        this.output = output;
        this.jobAdvertisementDtoAssembler = new JobAdvertisementDtoAssembler();
    }

    @Override
    public void write(List<? extends Oste> x28JobAdvertisements) {
        for (Oste x28JobAdvertisement : x28JobAdvertisements) {
            if (isExternalJobAdvertisement(x28JobAdvertisement)) {
                send(jobAdvertisementDtoAssembler.createFromX28(x28JobAdvertisement), CREATE_FROM_X28);
            } else {
                send(jobAdvertisementDtoAssembler.updateFromX28(x28JobAdvertisement), UPDATE_FROM_X28);
            }
        }
    }

    public void send(Object createJobAdvertisementX28Dto, JobAdvertisementAction action) {
        output.send(MessageBuilder
                .withPayload(createJobAdvertisementX28Dto)
                .setHeader(ACTION, action.name())
                .setHeader(SOURCE_SYSTEM, X28.name())
                .setHeader(TARGET_SYSTEM, JOB_AD_SERVICE.name())
                .build());
    }

    private boolean isExternalJobAdvertisement(Oste oste) {
        return isEmpty(oste.getStellennummerEGov());
    }
}
