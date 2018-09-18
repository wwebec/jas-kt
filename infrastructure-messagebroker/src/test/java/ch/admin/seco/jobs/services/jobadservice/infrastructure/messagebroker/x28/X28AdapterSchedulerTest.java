package ch.admin.seco.jobs.services.jobadservice.infrastructure.messagebroker.x28;

import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.JobAdvertisementApplicationService;
import ch.admin.seco.jobs.services.jobadservice.core.domain.events.DomainEvent;
import ch.admin.seco.jobs.services.jobadservice.core.domain.events.DomainEventMockUtils;
import ch.admin.seco.jobs.services.jobadservice.core.time.TimeMachine;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.*;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.events.JobAdvertisementEvents;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.support.TransactionTemplate;

import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisementTestDataProvider.*;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class X28AdapterSchedulerTest {
    private static final String FINGERPRINT_1 = "fingerprint1";
    private static final String FINGERPRINT_2 = "fingerprint2";
    private static final String FINGERPRINT_3 = "fingerprint3";

    @Autowired
    private X28MessageLogRepository x28MessageLogRepository;

    @Autowired
    private JobAdvertisementRepository jobAdvertisementRepository;

    @Autowired
    private TransactionTemplate transactionTemplate;

    private DomainEventMockUtils domainEventMockUtils;

    @MockBean
    private JobAdvertisementApplicationService jobAdvertisementApplicationService;

    private X28Adapter sut; //System Under Test

    @Before
    public void setUp() {
        this.sut = new X28Adapter(jobAdvertisementApplicationService, jobAdvertisementRepository, transactionTemplate, x28MessageLogRepository);
        domainEventMockUtils = new DomainEventMockUtils();
    }

    @Test
    public void scheduledArchiveExternalJobAds() {
        // given
        jobAdvertisementRepository.save(createExternalJobWithStatus(JOB_ADVERTISEMENT_ID_01, FINGERPRINT_1, JobAdvertisementStatus.CREATED));
        jobAdvertisementRepository.save(createExternalJobWithStatus(JOB_ADVERTISEMENT_ID_02, FINGERPRINT_2, JobAdvertisementStatus.PUBLISHED_PUBLIC));
        jobAdvertisementRepository.save(createExternalJobWithStatus(JOB_ADVERTISEMENT_ID_03, FINGERPRINT_3, JobAdvertisementStatus.PUBLISHED_PUBLIC));
        x28MessageLogRepository.save(new X28MessageLog(FINGERPRINT_2, TimeMachine.now().toLocalDate().minusDays(1)));
        x28MessageLogRepository.save(new X28MessageLog(FINGERPRINT_3, TimeMachine.now().toLocalDate()));

        // when
        this.sut.scheduledArchiveExternalJobAds();

        // then
        DomainEvent domainEvent = domainEventMockUtils.assertSingleDomainEventPublished(JobAdvertisementEvents.JOB_ADVERTISEMENT_PUBLISH_EXPIRED.getDomainEventType());
        assertThat(domainEvent.getAggregateId()).isEqualTo(JOB_ADVERTISEMENT_ID_02);
    }

    @Test
    public void shouldNotArchiveExternalJobAdsIfNoMessageReceivedOnTheSameDay() {
        // given
        jobAdvertisementRepository.save(createExternalJobWithStatus(JOB_ADVERTISEMENT_ID_01, FINGERPRINT_1, JobAdvertisementStatus.CREATED));
        jobAdvertisementRepository.save(createExternalJobWithStatus(JOB_ADVERTISEMENT_ID_02, FINGERPRINT_2, JobAdvertisementStatus.PUBLISHED_PUBLIC));
        jobAdvertisementRepository.save(createExternalJobWithStatus(JOB_ADVERTISEMENT_ID_03, FINGERPRINT_3, JobAdvertisementStatus.PUBLISHED_PUBLIC));
        x28MessageLogRepository.save(new X28MessageLog(FINGERPRINT_2, TimeMachine.now().toLocalDate().minusDays(1)));

        // when
        this.sut.scheduledArchiveExternalJobAds();

        // then
        domainEventMockUtils.verifyNoEventsPublished();
    }


    private JobAdvertisement createExternalJobWithStatus(JobAdvertisementId jobAdvertisementId, String fingerprint, JobAdvertisementStatus status) {
        return new JobAdvertisement.Builder()
                .setId(jobAdvertisementId)
                .setFingerprint(fingerprint)
                .setOwner(createOwner(jobAdvertisementId))
                .setContact(createContact(jobAdvertisementId))
                .setJobContent(createJobContent(jobAdvertisementId))
                .setPublication(new Publication.Builder().setEndDate(TimeMachine.now().toLocalDate()).build())
                .setSourceSystem(SourceSystem.EXTERN)
                .setStellennummerEgov(jobAdvertisementId.getValue())
                .setStellennummerAvam(null)
                .setStatus(status)
                .build();
    }
}
