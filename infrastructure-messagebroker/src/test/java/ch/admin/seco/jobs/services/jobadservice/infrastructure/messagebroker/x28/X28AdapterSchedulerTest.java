package ch.admin.seco.jobs.services.jobadservice.infrastructure.messagebroker.x28;

import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisementStatus.CREATED;
import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisementStatus.PUBLISHED_PUBLIC;
import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.events.JobAdvertisementEvents.JOB_ADVERTISEMENT_PUBLISH_EXPIRED;
import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.fixture.JobAdvertisementIdFixture.job01;
import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.fixture.JobAdvertisementIdFixture.job02;
import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.fixture.JobAdvertisementIdFixture.job03;
import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.fixture.JobAdvertisementTestFixture.testJobAdvertisementWithExternalSourceSystemAndStatus;
import static java.time.LocalDate.now;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.support.TransactionTemplate;

import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.JobAdvertisementApplicationService;
import ch.admin.seco.jobs.services.jobadservice.core.domain.events.DomainEvent;
import ch.admin.seco.jobs.services.jobadservice.core.domain.events.DomainEventMockUtils;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisementRepository;

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
        jobAdvertisementRepository.save(testJobAdvertisementWithExternalSourceSystemAndStatus(job01.id(), FINGERPRINT_1, CREATED));
        jobAdvertisementRepository.save(testJobAdvertisementWithExternalSourceSystemAndStatus(job02.id(), FINGERPRINT_2, PUBLISHED_PUBLIC));
        jobAdvertisementRepository.save(testJobAdvertisementWithExternalSourceSystemAndStatus(job03.id(), FINGERPRINT_3, PUBLISHED_PUBLIC));
        x28MessageLogRepository.save(new X28MessageLog(FINGERPRINT_2, now().minusDays(1)));
        x28MessageLogRepository.save(new X28MessageLog(FINGERPRINT_3, now()));

        // when
        this.sut.scheduledArchiveExternalJobAds();

        // then
        DomainEvent domainEvent = domainEventMockUtils.assertSingleDomainEventPublished(JOB_ADVERTISEMENT_PUBLISH_EXPIRED.getDomainEventType());
        assertThat(domainEvent.getAggregateId()).isEqualTo(job02.id());
    }

    @Test
    public void shouldNotArchiveExternalJobAdsIfNoMessageReceivedOnTheSameDay() {
        // given
        jobAdvertisementRepository.save(testJobAdvertisementWithExternalSourceSystemAndStatus(job01.id(), FINGERPRINT_1, CREATED));
        jobAdvertisementRepository.save(testJobAdvertisementWithExternalSourceSystemAndStatus(job02.id(), FINGERPRINT_2, PUBLISHED_PUBLIC));
        jobAdvertisementRepository.save(testJobAdvertisementWithExternalSourceSystemAndStatus(job03.id(), FINGERPRINT_3, PUBLISHED_PUBLIC));
        x28MessageLogRepository.save(new X28MessageLog(FINGERPRINT_2, now().minusDays(1)));

        // when
        this.sut.scheduledArchiveExternalJobAds();

        // then
        domainEventMockUtils.verifyNoEventsPublished();
    }
}
