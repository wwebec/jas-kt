package ch.admin.seco.jobs.services.jobadservice.infrastructure.messagebroker.x28;

import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.JobAdvertisementAlreadyExistsException;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.JobAdvertisementApplicationService;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.create.CreateJobAdvertisementFromX28Dto;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.update.UpdateJobAdvertisementFromX28Dto;
import ch.admin.seco.jobs.services.jobadservice.core.time.TimeMachine;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisement;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisementRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import java.time.LocalDate;
import java.util.Optional;
import java.util.function.Predicate;

import static ch.admin.seco.jobs.services.jobadservice.infrastructure.messagebroker.MessageBrokerChannels.CREATE_FROM_X28_CONDITION;
import static ch.admin.seco.jobs.services.jobadservice.infrastructure.messagebroker.MessageBrokerChannels.JOB_AD_ACTION_CHANNEL;

@Service
public class X28Adapter {

    private static final Logger LOG = LoggerFactory.getLogger(X28Adapter.class);

    private final JobAdvertisementApplicationService jobAdvertisementApplicationService;

    private final JobAdvertisementRepository jobAdvertisementRepository;

    private final TransactionTemplate transactionTemplate;

    private final X28MessageLogRepository x28MessageLogRepository;

    public X28Adapter(JobAdvertisementApplicationService jobAdvertisementApplicationService,
                      JobAdvertisementRepository jobAdvertisementRepository,
                      TransactionTemplate transactionTemplate,
                      X28MessageLogRepository x28MessageLogRepository) {
        this.jobAdvertisementApplicationService = jobAdvertisementApplicationService;
        this.jobAdvertisementRepository = jobAdvertisementRepository;
        this.transactionTemplate = transactionTemplate;
        this.x28MessageLogRepository = x28MessageLogRepository;
    }

    @Scheduled(cron = "${jobAdvertisement.checkExternalJobAdExpiration.cron}")
    public void scheduledArchiveExternalJobAds() {
        final LocalDate today = TimeMachine.now().toLocalDate();

        Predicate<JobAdvertisement> shouldArchive = jobAdvertisement ->
                this.x28MessageLogRepository.findById(jobAdvertisement.getFingerprint())
                        .map(X28MessageLog::getLastMessageDate)
                        .map(lastMessageDate -> lastMessageDate.isBefore(today))
                        .orElse(Boolean.TRUE);

        this.jobAdvertisementRepository.findAllPublishedExtern()
                .filter(shouldArchive)
                .forEach(JobAdvertisement::expirePublication);
    }

    @StreamListener(target = JOB_AD_ACTION_CHANNEL, condition = CREATE_FROM_X28_CONDITION)
    public void handleCreateFromX28Action(CreateJobAdvertisementFromX28Dto createFromX28) {
        try {
            logLastX28MessageDate(createFromX28.getFingerprint());

            if (createFromX28.getStellennummerEgov() != null && exists(createFromX28.getStellennummerEgov())) {
                jobAdvertisementApplicationService.updateFromX28(prepareUpdateJobAdvertisementFromX28Dto(createFromX28));
            } else {
                jobAdvertisementApplicationService.createFromX28(createFromX28);
            }
        } catch (JobAdvertisementAlreadyExistsException e) {
            LOG.debug(e.getMessage());

            jobAdvertisementApplicationService.republishIfArchived(e.getJobAdvertisementId());
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

    private void logLastX28MessageDate(String fingerprint) {
        X28MessageLog x28MessageLog = new X28MessageLog(fingerprint, TimeMachine.now().toLocalDate());
        x28MessageLogRepository.save(x28MessageLog);
    }
}
