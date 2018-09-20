package ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement;

import static ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.fixture.ApprovalDtoTestFixture.testApprovalDto;
import static ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.fixture.CreateJobAdvertisementFromAvamDtoTestFixture.testCreateJobAdvertisementDto;
import static ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.fixture.CreateJobAdvertisementFromAvamDtoTestFixture.testCreateJobAdvertisementDtoWithCompanyAnonymous;
import static ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.fixture.RejectionDtoTestFixture.testRejectionDto;
import static ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.fixture.UpdateJobAdvertisementFromAvamDtoTestFixture.testUpdateJobAdvertisementFromAvamDto;
import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.fixture.CompanyTestFixture.testCompany;
import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.fixture.JobAdvertisementIdTestFixture.job01;
import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.fixture.JobAdvertisementTestFixture.testJobAdvertisementWithInspectingStatus;
import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.fixture.LocationTestFixture.testLocation;
import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.events.JobAdvertisementEvents.JOB_ADVERTISEMENT_APPROVED;
import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.events.JobAdvertisementEvents.JOB_ADVERTISEMENT_UPDATED;
import static ch.admin.seco.jobs.services.jobadservice.domain.jobcenter.fixture.JobCenterTestFixture.testJobCenter;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDate;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.jdbc.support.incrementer.DataFieldMaxValueIncrementer;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import ch.admin.seco.jobs.services.jobadservice.application.JobCenterService;
import ch.admin.seco.jobs.services.jobadservice.application.LocationService;
import ch.admin.seco.jobs.services.jobadservice.application.ProfessionService;
import ch.admin.seco.jobs.services.jobadservice.application.ReportingObligationService;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.create.CreateJobAdvertisementFromAvamDto;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.update.ApprovalDto;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.update.RejectionDto;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.update.UpdateJobAdvertisementFromAvamDto;
import ch.admin.seco.jobs.services.jobadservice.core.domain.events.DomainEventMockUtils;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisement;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisementId;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisementRepository;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisementStatus;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.SourceSystem;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.events.JobAdvertisementEvents;

@SpringBootTest
@RunWith(SpringRunner.class)
@Transactional
public class JobAdvertisementApplicationServiceForAvamTest {

    private static final String TEST_STELLEN_NUMMER_EGOV = "1000000";
    public static final String STELLENNUMMER_AVAM = "avam";

    @MockBean
    private DomainEventMockUtils domainEventMockUtils;

    @MockBean
    private DataFieldMaxValueIncrementer egovNumberGenerator;

    @MockBean
    private LocationService locationService;

    @MockBean
    private ProfessionService professionService;

    @MockBean
    private JobCenterService jobCenterService;

    @MockBean
    private ReportingObligationService reportingObligationService;

    @Autowired
    private JobAdvertisementRepository jobAdvertisementRepository;

    @Autowired
    private JobAdvertisementApplicationService sut; //System Under Test

    @Before
    public void setUp() {
        domainEventMockUtils = new DomainEventMockUtils();
        when(locationService.enrichCodes(any())).thenReturn(testLocation());
        when(locationService.isLocationValid(any())).thenReturn(Boolean.TRUE);
        when(jobCenterService.findJobCenterByCode(any())).thenReturn(testJobCenter());
        when(egovNumberGenerator.nextStringValue()).thenReturn(TEST_STELLEN_NUMMER_EGOV);
    }

    @After
    public void tearDown() {
        domainEventMockUtils.clearEvents();
    }

    @Test
    public void createFromAvam() {
        //given
        CreateJobAdvertisementFromAvamDto createJobAdvertisementDto = testCreateJobAdvertisementDto();

        //when
        JobAdvertisementId jobAdvertisementId = sut.createFromAvam(createJobAdvertisementDto);

        //then
        JobAdvertisement jobAdvertisement = jobAdvertisementRepository.getOne(jobAdvertisementId);
        assertThat(jobAdvertisement).isNotNull();
        assertThat(jobAdvertisement.getStatus()).isEqualTo(JobAdvertisementStatus.APPROVED);
        assertThat(jobAdvertisement.getSourceSystem()).isEqualTo(SourceSystem.RAV);
        assertThat(jobAdvertisement.getStellennummerAvam()).isEqualTo(STELLENNUMMER_AVAM);
        assertThat(jobAdvertisement.getPublication().isEuresAnonymous()).isFalse();
        assertThat(jobAdvertisement.getPublication().isCompanyAnonymous()).isFalse();

        assertThat(jobAdvertisement.isReportingObligation()).isTrue();
        assertThat(jobAdvertisement.getJobContent().getDisplayCompany()).isEqualTo(testCompany());

        domainEventMockUtils.assertSingleDomainEventPublished(JOB_ADVERTISEMENT_APPROVED.getDomainEventType());
    }

    @Test
    public void createFromAvamWithCompanyAnonymous() {
        //given
        CreateJobAdvertisementFromAvamDto createJobAdvertisementDto = testCreateJobAdvertisementDtoWithCompanyAnonymous();

        //when
        //when
        JobAdvertisementId jobAdvertisementId = sut.createFromAvam(createJobAdvertisementDto);

        //then
        JobAdvertisement jobAdvertisement = jobAdvertisementRepository.getOne(jobAdvertisementId);
        assertThat(jobAdvertisement).isNotNull();
        assertThat(jobAdvertisement.getStatus()).isEqualTo(JobAdvertisementStatus.APPROVED);
        assertThat(jobAdvertisement.getSourceSystem()).isEqualTo(SourceSystem.RAV);
        assertThat(jobAdvertisement.getStellennummerAvam()).isEqualTo(STELLENNUMMER_AVAM);
        assertThat(jobAdvertisement.getPublication().isEuresAnonymous()).isFalse();
        assertThat(jobAdvertisement.getPublication().isCompanyAnonymous()).isTrue();

        assertThat(jobAdvertisement.isReportingObligation()).isTrue();
        assertThat(jobAdvertisement.getJobContent().getDisplayCompany()).isEqualTo(testCompany(true));

        domainEventMockUtils.assertSingleDomainEventPublished(JOB_ADVERTISEMENT_APPROVED.getDomainEventType());
    }

    @Test
    public void shouldApprove() {
        // given
        JobAdvertisement jobAdvertisementWithInspectingStatus = jobAdvertisementRepository.save(testJobAdvertisementWithInspectingStatus());
        UpdateJobAdvertisementFromAvamDto updateJobAdvertisementFromAvamDto = testUpdateJobAdvertisementFromAvamDto(jobAdvertisementWithInspectingStatus);
        ApprovalDto approvalDto = testApprovalDto(updateJobAdvertisementFromAvamDto);

        // when
        sut.approve(approvalDto);

        // then
        JobAdvertisement jobAdvertisement = jobAdvertisementRepository.getOne(job01.id());
        assertThat(jobAdvertisement.getStellennummerAvam()).isEqualTo(STELLENNUMMER_AVAM);
        assertThat(jobAdvertisement.getApprovalDate()).isEqualTo(LocalDate.of(2018, 1, 1));
        assertThat(jobAdvertisement.isReportingObligation()).isTrue();
        assertThat(jobAdvertisement.getReportingObligationEndDate()).isEqualTo(LocalDate.of(2018, 10, 1));
        assertThat(jobAdvertisement.getStatus()).isEqualTo(JobAdvertisementStatus.APPROVED);
        domainEventMockUtils.assertSingleDomainEventPublished(JOB_ADVERTISEMENT_APPROVED.getDomainEventType());
    }


    @Test
    public void shouldApproveWithUpdate() {
        // given
        JobAdvertisement jobAdvertisementWithInspectingStatus = jobAdvertisementRepository.save(testJobAdvertisementWithInspectingStatus());
        UpdateJobAdvertisementFromAvamDto updateJobAdvertisementFromAvamDto = testUpdateJobAdvertisementFromAvamDto(jobAdvertisementWithInspectingStatus);
        updateJobAdvertisementFromAvamDto.setDescription("OTHER VALUE");
        ApprovalDto approvalDto = testApprovalDto(updateJobAdvertisementFromAvamDto);

        // when
        sut.approve(approvalDto);

        // then
        JobAdvertisement jobAdvertisement = jobAdvertisementRepository.getOne(jobAdvertisementWithInspectingStatus.getId());
        assertThat(jobAdvertisement.getStellennummerAvam()).isEqualTo(STELLENNUMMER_AVAM);
        assertThat(jobAdvertisement.getApprovalDate()).isEqualTo(LocalDate.of(2018, 1, 1));
        assertThat(jobAdvertisement.isReportingObligation()).isTrue();
        assertThat(jobAdvertisement.getReportingObligationEndDate()).isEqualTo(LocalDate.of(2018, 10, 1));
        assertThat(jobAdvertisement.getStatus()).isEqualTo(JobAdvertisementStatus.APPROVED);
        assertThat(jobAdvertisement.getJobContent().getJobDescriptions().get(0).getDescription()).isEqualTo("OTHER VALUE");
        domainEventMockUtils.assertMultipleDomainEventPublished(2, JOB_ADVERTISEMENT_UPDATED.getDomainEventType());
    }

    @Test
    public void shouldReject() {
        // given
        jobAdvertisementRepository.save(testJobAdvertisementWithInspectingStatus());
        RejectionDto rejectionDto = testRejectionDto();

        // when
        sut.reject(rejectionDto);

        // then
        JobAdvertisement jobAdvertisement = jobAdvertisementRepository.getOne(job01.id());
        assertThat(jobAdvertisement.getStellennummerAvam()).isEqualTo(STELLENNUMMER_AVAM);
        assertThat(jobAdvertisement.getRejectionDate()).isEqualTo(LocalDate.of(2018, 1, 1));
        assertThat(jobAdvertisement.getRejectionCode()).isEqualTo("code");
        assertThat(jobAdvertisement.getRejectionReason()).isEqualTo("reason");
        assertThat(jobAdvertisement.getJobCenterCode()).isEqualTo("jobcenterid");
        assertThat(jobAdvertisement.getStatus()).isEqualTo(JobAdvertisementStatus.REJECTED);
        domainEventMockUtils.assertSingleDomainEventPublished(JobAdvertisementEvents.JOB_ADVERTISEMENT_REJECTED.getDomainEventType());
    }
}