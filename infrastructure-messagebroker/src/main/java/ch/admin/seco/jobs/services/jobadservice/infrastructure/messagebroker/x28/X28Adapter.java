package ch.admin.seco.jobs.services.jobadservice.infrastructure.messagebroker.x28;

import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.JobAdvertisementAlreadyExistsException;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.JobAdvertisementApplicationService;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.x28.CreateJobAdvertisementFromX28Dto;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.update.UpdateJobAdvertisementFromX28Dto;
import ch.admin.seco.jobs.services.jobadservice.core.time.TimeMachine;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisement;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisementId;
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
        LOG.info("Starting scheduledArchiveExternalJobAds");

        if (isX28MessageReceived(today())) {
            Long archivedJobCount = this.transactionTemplate.execute(status -> archiveExternalJobAds());

            LOG.info("Archived external jobs: {}", archivedJobCount);
        } else {
            LOG.info("Archiving skipped because no x28 message was received. Please check the x28 import job!");
        }
    }

    @StreamListener(target = JOB_AD_ACTION_CHANNEL, condition = CREATE_FROM_X28_CONDITION)
    public void handleCreateFromX28Action(CreateJobAdvertisementFromX28Dto createFromX28) {
        try {
            logLastX28MessageDate(createFromX28.getFingerprint());
            Optional<JobAdvertisementId> jobAdvertisementId = determineJobAdvertisementId(createFromX28);
            if (jobAdvertisementId.isPresent()) {
                jobAdvertisementApplicationService.updateFromX28(new UpdateJobAdvertisementFromX28Dto(
                        jobAdvertisementId.get().getValue(),
                        createFromX28.getFingerprint(),
                        createFromX28.getProfessionCodes()
                ));
            } else {
                jobAdvertisementApplicationService.createFromX28(createFromX28);
            }
        } catch (JobAdvertisementAlreadyExistsException e) {
            LOG.debug(e.getMessage());

            jobAdvertisementApplicationService.republishIfArchived(e.getJobAdvertisementId());
        }
    }

    private Optional<JobAdvertisementId> determineJobAdvertisementId(CreateJobAdvertisementFromX28Dto createFromX28) {
        if (createFromX28.getStellennummerEgov() != null) {
            Optional<JobAdvertisementId> jobAdvertisementId = transactionTemplate.execute(status -> findByStellennummerEgov(createFromX28))
                    .map(JobAdvertisement::getId);
            if (jobAdvertisementId.isPresent()) {
                return jobAdvertisementId;
            }
        }
        if (createFromX28.getStellennummerAvam() != null) {
            return transactionTemplate.execute(status -> findByStellennummerAvam(createFromX28)
                    .map(JobAdvertisement::getId));
        }
        return Optional.empty();
    }

    private Optional<JobAdvertisement> findByStellennummerEgov(CreateJobAdvertisementFromX28Dto createFromX28) {
        return jobAdvertisementRepository.findByStellennummerEgov(createFromX28.getStellennummerEgov());
    }

    private Optional<JobAdvertisement> findByStellennummerAvam(CreateJobAdvertisementFromX28Dto createFromX28) {
        return jobAdvertisementRepository.findByStellennummerAvam(createFromX28.getStellennummerAvam());
    }

    private long archiveExternalJobAds() {
        final LocalDate today = today();

        Predicate<JobAdvertisement> shouldArchive = jobAdvertisement ->
                this.x28MessageLogRepository.findById(jobAdvertisement.getFingerprint())
                        .map(X28MessageLog::getLastMessageDate)
                        .map(lastMessageDate -> lastMessageDate.isBefore(today))
                        .orElse(Boolean.TRUE);

        return this.jobAdvertisementRepository.findAllPublishedExtern()
                .filter(shouldArchive)
                .map(jobAdvertisement -> {
                    jobAdvertisement.expirePublication();
                    return jobAdvertisement.getId();
                })
                .count();
    }

    private void logLastX28MessageDate(String fingerprint) {
        X28MessageLog x28MessageLog = new X28MessageLog(fingerprint, today());
        x28MessageLogRepository.save(x28MessageLog);
    }

    private boolean isX28MessageReceived(LocalDate date) {
        return x28MessageLogRepository.countByLastMessageDateEquals(date) > 0;
    }

    private LocalDate today() {
        return TimeMachine.now().toLocalDate();
    }
}
