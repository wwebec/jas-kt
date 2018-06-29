package ch.admin.seco.jobs.services.jobadservice.infrastructure.messagebroker.x28;


import static ch.admin.seco.jobs.services.jobadservice.infrastructure.messagebroker.MessageBrokerChannels.CREATE_FROM_X28_CONDITION;
import static ch.admin.seco.jobs.services.jobadservice.infrastructure.messagebroker.MessageBrokerChannels.JOB_AD_ACTION_CHANNEL;

import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.create.CreateJobAdvertisementFromX28Dto;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisementRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Service;

import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.JobAdvertisementAlreadyExistsException;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.JobAdvertisementApplicationService;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.update.UpdateJobAdvertisementFromX28Dto;

@Service
public class X28Adapter {

    private static final Logger LOG = LoggerFactory.getLogger(X28Adapter.class);

    private final JobAdvertisementApplicationService jobAdvertisementApplicationService;

    private final JobAdvertisementRepository jobAdvertisementRepository;

    public X28Adapter(JobAdvertisementApplicationService jobAdvertisementApplicationService, JobAdvertisementRepository jobAdvertisementRepository) {
        this.jobAdvertisementApplicationService = jobAdvertisementApplicationService;
        this.jobAdvertisementRepository = jobAdvertisementRepository;
    }

    @StreamListener(target = JOB_AD_ACTION_CHANNEL, condition = CREATE_FROM_X28_CONDITION)
    public void handleCreateFromX28Action(CreateJobAdvertisementFromX28Dto createFromX28) {
        try {
            if((createFromX28.getStellennummerEgov() == null) || !exists(createFromX28.getStellennummerEgov())) {
                jobAdvertisementApplicationService.createFromX28(createFromX28);
            } else {
                UpdateJobAdvertisementFromX28Dto updateFromX28 = new UpdateJobAdvertisementFromX28Dto(
                        createFromX28.getStellennummerEgov(),
                        createFromX28.getFingerprint(),
                        createFromX28.getProfessionCodes()
                );
                jobAdvertisementApplicationService.updateFromX28(updateFromX28);
            }
        } catch (JobAdvertisementAlreadyExistsException e) {
            LOG.debug(e.getMessage());
        }
    }

    private boolean exists(String stellennummerEgov) {
        return jobAdvertisementRepository.findByStellennummerEgov(stellennummerEgov).isPresent();
    }
}
