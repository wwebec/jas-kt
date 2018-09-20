package ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement;

import static ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.JobAdvertisementApplicationService.EXTERN_JOB_AD_REACTIVATION_DAY_NUM;
import static ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.fixture.CreateJobAdvertisementDtoTestFixture.testCreateJobAdvertisementDto;
import static ch.admin.seco.jobs.services.jobadservice.core.time.TimeMachine.reset;
import static ch.admin.seco.jobs.services.jobadservice.core.time.TimeMachine.useFixedClockAt;
import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.CancellationCode.OCCUPIED_OTHER;
import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisementStatus.ARCHIVED;
import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisementStatus.CANCELLED;
import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisementStatus.CREATED;
import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisementStatus.INSPECTING;
import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisementStatus.PUBLISHED_PUBLIC;
import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisementStatus.PUBLISHED_RESTRICTED;
import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisementStatus.REFINING;
import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.SourceSystem.JOBROOM;
import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.events.JobAdvertisementEvents.JOB_ADVERTISEMENT_BLACKOUT_EXPIRED;
import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.events.JobAdvertisementEvents.JOB_ADVERTISEMENT_CANCELLED;
import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.events.JobAdvertisementEvents.JOB_ADVERTISEMENT_INSPECTING;
import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.events.JobAdvertisementEvents.JOB_ADVERTISEMENT_PUBLISH_EXPIRED;
import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.events.JobAdvertisementEvents.JOB_ADVERTISEMENT_PUBLISH_PUBLIC;
import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.fixture.JobAdvertisementIdTestFixture.job01;
import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.fixture.JobAdvertisementIdTestFixture.job02;
import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.fixture.JobAdvertisementIdTestFixture.job03;
import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.fixture.JobAdvertisementIdTestFixture.job04;
import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.fixture.JobAdvertisementIdTestFixture.job05;
import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.fixture.JobAdvertisementTestFixture.testJobAdvertisementJobWithStatusAndReportingObligationEndDate;
import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.fixture.JobAdvertisementTestFixture.testJobAdvertisementWithAnonymousCompany;
import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.fixture.JobAdvertisementTestFixture.testJobAdvertisementWithStatusAndPublicationEndDate;
import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.fixture.JobAdvertisementTestFixture.testJobAdvertisementWithStatusAndPublicationStartDate;
import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.fixture.LocationTestFixture.testLocation;
import static ch.admin.seco.jobs.services.jobadservice.domain.jobcenter.fixture.JobCenterTestFixture.JOB_CENTER_CODE;
import static ch.admin.seco.jobs.services.jobadservice.domain.jobcenter.fixture.JobCenterTestFixture.testJobCenter;
import static ch.admin.seco.jobs.services.jobadservice.domain.profession.ProfessionCodeType.AVAM;
import static java.time.LocalDate.now;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.assertj.core.api.iterable.Extractor;
import org.assertj.core.data.Index;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.jdbc.support.incrementer.DataFieldMaxValueIncrementer;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import ch.admin.seco.jobs.services.jobadservice.application.JobCenterService;
import ch.admin.seco.jobs.services.jobadservice.application.LocationService;
import ch.admin.seco.jobs.services.jobadservice.application.ProfessionService;
import ch.admin.seco.jobs.services.jobadservice.application.ReportingObligationService;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.EmploymentDto;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.create.CreateJobAdvertisementDto;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.create.CreateLocationDto;
import ch.admin.seco.jobs.services.jobadservice.core.domain.events.DomainEvent;
import ch.admin.seco.jobs.services.jobadservice.core.domain.events.DomainEventMockUtils;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.Company;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisement;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisementId;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisementRepository;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.Location;
import ch.admin.seco.jobs.services.jobadservice.domain.jobcenter.JobCenter;

@SpringBootTest
@RunWith(SpringRunner.class)
@Transactional
public class JobAdvertisementApplicationServiceTest {

    private static final String TEST_STELLEN_NUMMER_EGOV = "1000000";

    @MockBean
    private DomainEventMockUtils domainEventMockUtils;

    @MockBean
    private ReportingObligationService reportingObligationService;

    @MockBean
    private LocationService locationService;

    @MockBean
    private ProfessionService professionService;

    @MockBean
    private JobCenterService jobCenterService;

    @Autowired
    private JobAdvertisementRepository jobAdvertisementRepository;

    @MockBean
    private DataFieldMaxValueIncrementer egovNumberGenerator;

    @Autowired
    private JobAdvertisementApplicationService sut; //System Under Test

    @Before
    public void setUp() {
        this.jobAdvertisementRepository.deleteAll();

        this.domainEventMockUtils = new DomainEventMockUtils();

        when(locationService.enrichCodes(any())).thenReturn(testLocation());
        when(locationService.isLocationValid(any())).thenReturn(Boolean.TRUE);
        when(egovNumberGenerator.nextStringValue()).thenReturn(TEST_STELLEN_NUMMER_EGOV);
    }

    @After
    public void tearDown() {
        this.jobAdvertisementRepository.deleteAll();
        this.domainEventMockUtils.clearEvents();
    }

    @Test
    public void shouldSetReportingObligationToFalseWhenShortEmployment() {
        //given
        CreateJobAdvertisementDto createJobAdvertisementDto = testCreateJobAdvertisementDto();
        createJobAdvertisementDto.setEmployment(new EmploymentDto(LocalDate.of(2018, 1, 1), LocalDate.of(2018, 1, 10), true, false, false, 80, 100, null));
        when(reportingObligationService.hasReportingObligation(any(), any(), any())).thenReturn(true);

        checkReportingObligation(createJobAdvertisementDto, false);
    }

    private void checkReportingObligation(CreateJobAdvertisementDto createJobAdvertisementDto, boolean expectedValue) {
        //when
        JobAdvertisementId jobAdvertisementId = sut.createFromWebForm(createJobAdvertisementDto);

        //then
        JobAdvertisement jobAdvertisement = jobAdvertisementRepository.findById(jobAdvertisementId).get();
        assertThat(jobAdvertisement.isReportingObligation()).isEqualTo(expectedValue);
    }

    @Test
    public void shouldSetReportingObligationToFalseWhenLocationIsGermany() {
        //Prepare
        CreateJobAdvertisementDto createJobAdvertisementDto = testCreateJobAdvertisementDto();
        createJobAdvertisementDto.setLocation(new CreateLocationDto("remarks", "city", "postalCode", "DE"));
        when(locationService.enrichCodes(any(Location.class))).thenAnswer(invocation -> invocation.getArgument(0));

        checkReportingObligation(createJobAdvertisementDto, false);
    }

    @Test
    public void shouldSetReportingObligationToTrueWhenLocationIsSwiss() {
        //Prepare
        CreateJobAdvertisementDto createJobAdvertisementDto = testCreateJobAdvertisementDto();
        when(reportingObligationService.hasReportingObligation(AVAM, "avamCode", "BE")).thenReturn(true);

        checkReportingObligation(createJobAdvertisementDto, true);
    }

    @Test
    public void shouldInspect() {
        // given
        jobAdvertisementRepository.save(
                testJobAdvertisementWithStatusAndPublicationEndDate(
                        job01.id(),
                        CREATED,
                        null
                )
        );

        // when
        sut.inspect(job01.id());

        // then
        JobAdvertisement jobAdvertisement = jobAdvertisementRepository.findById(job01.id()).get();
        assertThat(jobAdvertisement.getStatus()).isEqualTo(INSPECTING);
        domainEventMockUtils.assertSingleDomainEventPublished(JOB_ADVERTISEMENT_INSPECTING.getDomainEventType());
    }

    @Test
    public void shouldCancel() {
        // given
        jobAdvertisementRepository.save(testJobAdvertisementWithStatusAndPublicationEndDate(
                job01.id(), CREATED, null));

        // when
        sut.cancel(job01.id(), LocalDate.of(2018, 1, 1), OCCUPIED_OTHER, JOBROOM, null);

        // then
        JobAdvertisement jobAdvertisement = jobAdvertisementRepository.findById(job01.id()).get();
        assertThat(jobAdvertisement.getCancellationCode()).isEqualTo(OCCUPIED_OTHER);
        assertThat(jobAdvertisement.getCancellationDate()).isEqualTo(LocalDate.of(2018, 1, 1));
        assertThat(jobAdvertisement.getStatus()).isEqualTo(CANCELLED);
        domainEventMockUtils.assertSingleDomainEventPublished(JOB_ADVERTISEMENT_CANCELLED.getDomainEventType());
    }

    @Test
    public void shouldRefine() {
        // given
        jobAdvertisementRepository.save(testJobAdvertisementWithStatusAndPublicationEndDate(
                job01.id(), CREATED, null));

        // when
        sut.refining(job01.id());

        // then
        JobAdvertisement jobAdvertisement = jobAdvertisementRepository.findById(job01.id()).get();
        assertThat(jobAdvertisement.getStatus()).isEqualTo(REFINING);
        // TODO: uncomment once we have x28-api
//        domainEventMockUtils.assertSingleDomainEventPublished(JobAdvertisementEvents.JOB_ADVERTISEMENT_REFINING.getDomainEventType());
    }

    @Test
    public void shouldPublish() {
        // given
        jobAdvertisementRepository.save(testJobAdvertisementWithStatusAndPublicationStartDate(job05.id(), REFINING, now()));

        // when
        this.sut.publish(job05.id());

        // then
        DomainEvent domainEvent = domainEventMockUtils.assertSingleDomainEventPublished(JOB_ADVERTISEMENT_PUBLISH_PUBLIC.getDomainEventType());
        assertThat(domainEvent.getAggregateId()).isEqualTo(job05.id());

        JobAdvertisement jobAdvertisementPublishNow = jobAdvertisementRepository.findById(job05.id()).get();
        assertThat(jobAdvertisementPublishNow.getStatus()).isEqualTo(PUBLISHED_PUBLIC);
    }

    @Test
    public void shouldNotPublish() {
        // given
        jobAdvertisementRepository.save(testJobAdvertisementWithStatusAndPublicationStartDate(job03.id(), REFINING, now().plusDays(1)));

        // when
        this.sut.publish(job03.id());

        // then
        JobAdvertisement jobAdvertisementPublishLater = jobAdvertisementRepository.findById(job03.id()).get();
        assertThat(jobAdvertisementPublishLater.getStatus()).isEqualTo(REFINING);
    }

    @Test
    public void shouldRepublish() {
        // given
        useFixedClockAt(LocalDateTime.now().minusDays(EXTERN_JOB_AD_REACTIVATION_DAY_NUM));
        jobAdvertisementRepository.save(testJobAdvertisementWithStatusAndPublicationEndDate(job01.id(), ARCHIVED, now().plusDays(60)));
        reset();

                // when
        sut.republishIfArchived(job01.id());

        // then
        JobAdvertisement jobAdvertisement = jobAdvertisementRepository.findById(job01.id()).get();
        assertThat(jobAdvertisement.getStatus()).isEqualTo(PUBLISHED_PUBLIC);
    }

    @Test
    public void shouldNotRepublish() {
        // given
        useFixedClockAt(LocalDateTime.now().minusDays(EXTERN_JOB_AD_REACTIVATION_DAY_NUM + 1));
        jobAdvertisementRepository.save(testJobAdvertisementWithStatusAndPublicationEndDate(job01.id(), ARCHIVED, null));
        reset();

        // when
        sut.republishIfArchived(job01.id());

        // then
        JobAdvertisement jobAdvertisement = jobAdvertisementRepository.findById(job01.id()).get();
        assertThat(jobAdvertisement.getStatus()).isEqualTo(ARCHIVED);
    }

    @Test
    public void scheduledCheckPublicationStarts() {
        // given
        jobAdvertisementRepository.saveAll(
                asList(
                        testJobAdvertisementWithStatusAndPublicationStartDate(job03.id(), REFINING, now().plusDays(1)),
                        testJobAdvertisementWithStatusAndPublicationStartDate(job04.id(), REFINING, now().minusDays(1)),
                        testJobAdvertisementWithStatusAndPublicationStartDate(job05.id(), REFINING, now())
                )
        );

        // when
        this.sut.scheduledCheckPublicationStarts();

        // then
        domainEventMockUtils.assertMultipleDomainEventPublished(2, JOB_ADVERTISEMENT_PUBLISH_PUBLIC.getDomainEventType());
    }

    @Test
    public void scheduledCheckBlackoutPolicyExpiration() {
        // given
        jobAdvertisementRepository.saveAll(
                asList(
                        testJobAdvertisementJobWithStatusAndReportingObligationEndDate(job01.id(), CREATED, null),
                        testJobAdvertisementJobWithStatusAndReportingObligationEndDate(job02.id(), CANCELLED, null),
                        testJobAdvertisementJobWithStatusAndReportingObligationEndDate(job03.id(), PUBLISHED_RESTRICTED, now().plusDays(1)),
                        testJobAdvertisementJobWithStatusAndReportingObligationEndDate(job04.id(), PUBLISHED_RESTRICTED, now().minusDays(1)),
                        testJobAdvertisementJobWithStatusAndReportingObligationEndDate(job05.id(), PUBLISHED_RESTRICTED, now())
                )
        );

        // when
        this.sut.scheduledCheckBlackoutPolicyExpiration();

        // then
        DomainEvent domainEvent = domainEventMockUtils.assertSingleDomainEventPublished(JOB_ADVERTISEMENT_BLACKOUT_EXPIRED.getDomainEventType());
        assertThat(domainEvent.getAggregateId()).isEqualTo(job04.id());
    }

    @Test
    public void scheduledCheckPublicationExpiration() {
        // given
        jobAdvertisementRepository.saveAll(
                asList(
                        testJobAdvertisementWithStatusAndPublicationEndDate(job01.id(), CREATED, null),
                        testJobAdvertisementWithStatusAndPublicationEndDate(job02.id(), CANCELLED, null),
                        testJobAdvertisementWithStatusAndPublicationEndDate(job03.id(), PUBLISHED_PUBLIC, now().plusDays(1)),
                        testJobAdvertisementWithStatusAndPublicationEndDate(job04.id(), PUBLISHED_PUBLIC, now().minusDays(1)),
                        testJobAdvertisementWithStatusAndPublicationEndDate(job05.id(), PUBLISHED_PUBLIC, now())
                )
        );

        // when
        this.sut.scheduledCheckPublicationExpiration();

        // then
        DomainEvent domainEvent = domainEventMockUtils.assertSingleDomainEventPublished(JOB_ADVERTISEMENT_PUBLISH_EXPIRED.getDomainEventType());
        assertThat(domainEvent.getAggregateId()).isEqualTo(job04.id());
    }

    @Test
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public void updateJobCenter() {
        //given
        for (int i = 0; i < 100; i++) {
            jobAdvertisementRepository.saveAndFlush(
                    testJobAdvertisementWithAnonymousCompany(new JobAdvertisementId("Job-Add-Test" + i), CREATED, JOB_CENTER_CODE)
            );
        }
        JobCenter testRav = testJobCenter();
        when(this.jobCenterService.findAllJobCenters()).thenReturn(singletonList(testRav));

        // when
        sut.updateJobCenters();

        //then
        List<JobAdvertisement> jobAdvertisements = this.jobAdvertisementRepository.findAll();
        Company updatedDisplayCompany = new Company.Builder(testRav).build();
        assertThat(jobAdvertisements)
                .extracting((Extractor<JobAdvertisement, Company>) input -> input.getJobContent().getDisplayCompany())
                .contains(updatedDisplayCompany, Index.atIndex(0))
                .contains(updatedDisplayCompany, Index.atIndex(99));

    }
}
