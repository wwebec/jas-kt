package ch.admin.seco.jobs.services.jobadservice.infrastructure.messagebroker.x28;


import static ch.admin.seco.jobs.services.jobadservice.infrastructure.messagebroker.MessageBrokerChannels.CREATE_FROM_X28_CONDITION;
import static ch.admin.seco.jobs.services.jobadservice.infrastructure.messagebroker.MessageBrokerChannels.JOB_AD_ACTION_CHANNEL;
import static ch.admin.seco.jobs.services.jobadservice.infrastructure.messagebroker.MessageBrokerChannels.UPDATE_FROM_X28_CONDITION;

import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Service;

import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.JobAdvertisementApplicationService;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.CreateJobAdvertisementFromX28Dto;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.UpdateJobAdvertisementFromX28Dto;

@Service
public class X28Adapter {

    private final JobAdvertisementApplicationService jobAdvertisementApplicationService;

    public X28Adapter(JobAdvertisementApplicationService jobAdvertisementApplicationService) {
        this.jobAdvertisementApplicationService = jobAdvertisementApplicationService;
    }

    @StreamListener(target = JOB_AD_ACTION_CHANNEL, condition = CREATE_FROM_X28_CONDITION)
    public void handleCreateFromX28Action(CreateJobAdvertisementFromX28Dto createJobAdvertisementFromX28Dto) {
        jobAdvertisementApplicationService.createFromX28(createJobAdvertisementFromX28Dto);
    }

    @StreamListener(target = JOB_AD_ACTION_CHANNEL, condition = UPDATE_FROM_X28_CONDITION)
    public void handleUpdateFromX28Action(UpdateJobAdvertisementFromX28Dto updateJobAdvertisementFromX28Dto) {
        jobAdvertisementApplicationService.updateFromX28(updateJobAdvertisementFromX28Dto);
    }
}
