package ch.admin.seco.jobs.services.jobadservice.infrastructure.messagebroker.x28;

import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.JobAdvertisementAlreadyExistsException;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.JobAdvertisementApplicationService;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.create.CreateJobAdvertisementFromX28Dto;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.update.UpdateJobAdvertisementFromX28Dto;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisement;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisementRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Optional;

import static ch.admin.seco.jobs.services.jobadservice.infrastructure.messagebroker.MessageBrokerChannels.CREATE_FROM_X28_CONDITION;
import static ch.admin.seco.jobs.services.jobadservice.infrastructure.messagebroker.MessageBrokerChannels.JOB_AD_ACTION_CHANNEL;

@Service
public class X28Adapter {

    private static final Logger LOG = LoggerFactory.getLogger(X28Adapter.class);

    private final JobAdvertisementApplicationService jobAdvertisementApplicationService;

    private final JobAdvertisementRepository jobAdvertisementRepository;

    private final TransactionTemplate transactionTemplate;

    public X28Adapter(JobAdvertisementApplicationService jobAdvertisementApplicationService, JobAdvertisementRepository jobAdvertisementRepository, TransactionTemplate transactionTemplate) {
        this.jobAdvertisementApplicationService = jobAdvertisementApplicationService;
        this.jobAdvertisementRepository = jobAdvertisementRepository;
        this.transactionTemplate = transactionTemplate;
    }

    @StreamListener(target = JOB_AD_ACTION_CHANNEL, condition = CREATE_FROM_X28_CONDITION)
    public void handleCreateFromX28Action(CreateJobAdvertisementFromX28Dto createFromX28) {
        try {
            if (createFromX28.getStellennummerEgov() != null && exists(createFromX28.getStellennummerEgov())) {
                jobAdvertisementApplicationService.updateFromX28(prepareUpdateJobAdvertisementFromX28Dto(createFromX28));
            } else {
                jobAdvertisementApplicationService.createFromX28(createFromX28);
            }
        } catch (JobAdvertisementAlreadyExistsException e) {
            LOG.debug(e.getMessage());
        }
    }

    private UpdateJobAdvertisementFromX28Dto prepareUpdateJobAdvertisementFromX28Dto(CreateJobAdvertisementFromX28Dto createFromX28) {
        return new UpdateJobAdvertisementFromX28Dto(
                createFromX28.getStellennummerEgov(),
                createFromX28.getFingerprint(),
                createFromX28.getProfessionCodes()
        );
    }

    private boolean exists(String stellennummerEgov) {
        Optional<JobAdvertisement> result = transactionTemplate.execute(status -> jobAdvertisementRepository.findByStellennummerEgov(stellennummerEgov));
        return result.isPresent();
    }
}
