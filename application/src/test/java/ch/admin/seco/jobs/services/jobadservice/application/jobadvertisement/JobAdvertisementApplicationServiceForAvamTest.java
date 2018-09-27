package ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement;

import static ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.fixture.ApprovalDtoTestFixture.testApprovalDto;
import static ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.fixture.CreateJobAdvertisementFromAvamDtoTestFixture.testCreateJobAdvertisementDto;
import static ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.fixture.CreateJobAdvertisementFromAvamDtoTestFixture.testCreateJobAdvertisementDtoWithCompanyAnonymous;
import static ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.fixture.RejectionDtoTestFixture.testRejectionDto;
import static ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.fixture.UpdateJobAdvertisementFromAvamDtoTestFixture.testUpdateJobAdvertisementFromAvamDto;
import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisementStatus.APPROVED;
import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisementStatus.INSPECTING;
import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisementStatus.REJECTED;
import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.events.JobAdvertisementEvents.JOB_ADVERTISEMENT_APPROVED;
import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.events.JobAdvertisementEvents.JOB_ADVERTISEMENT_REJECTED;
import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.events.JobAdvertisementEvents.JOB_ADVERTISEMENT_UPDATED;
import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.fixture.CompanyFixture.testCompany;
import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.fixture.CompanyFixture.testDisplayCompany;
import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.fixture.JobAdvertisementFixture.testJobAdvertisement;
import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.fixture.JobAdvertisementIdFixture.job01;
import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.fixture.LocationFixture.testLocation;
import static ch.admin.seco.jobs.services.jobadservice.domain.jobcenter.fixture.JobCenterTestFixture.testJobCenter;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

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
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.create.AvamCreateJobAdvertisementDto;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.update.ApprovalDto;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.update.RejectionDto;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.update.UpdateJobAdvertisementFromAvamDto;
import ch.admin.seco.jobs.services.jobadservice.core.domain.events.DomainEventMockUtils;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisement;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisementId;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisementRepository;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.SourceSystem;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.fixture.JobContentFixture;

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
        when(locationService.enrichCodes(any())).thenReturn(testLocation().build());
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
        AvamCreateJobAdvertisementDto createJobAdvertisementDto = testCreateJobAdvertisementDto();

        //when
        JobAdvertisementId jobAdvertisementId = sut.createFromAvam(createJobAdvertisementDto);

        //then
        JobAdvertisement jobAdvertisement = jobAdvertisementRepository.getOne(jobAdvertisementId);
        assertThat(jobAdvertisement).isNotNull();
        assertThat(jobAdvertisement.getStatus()).isEqualTo(APPROVED);
        assertThat(jobAdvertisement.getSourceSystem()).isEqualTo(SourceSystem.RAV);
        assertThat(jobAdvertisement.getStellennummerAvam()).isEqualTo(STELLENNUMMER_AVAM);
        assertThat(jobAdvertisement.getPublication().isEuresAnonymous()).isFalse();
        assertThat(jobAdvertisement.getPublication().isCompanyAnonymous()).isFalse();

        assertThat(jobAdvertisement.isReportingObligation()).isTrue();
        assertThat(jobAdvertisement.getJobContent().getDisplayCompany()).isEqualTo(testCompany().build());

        domainEventMockUtils.assertSingleDomainEventPublished(JOB_ADVERTISEMENT_APPROVED.getDomainEventType());
    }

    @Test
    public void createFromAvamWithCompanyAnonymous() {
        //given
        AvamCreateJobAdvertisementDto createJobAdvertisementDto = testCreateJobAdvertisementDtoWithCompanyAnonymous();

        //when
        JobAdvertisementId jobAdvertisementId = sut.createFromAvam(createJobAdvertisementDto);

        //then
        JobAdvertisement jobAdvertisement = jobAdvertisementRepository.getOne(jobAdvertisementId);
        assertThat(jobAdvertisement).isNotNull();
        assertThat(jobAdvertisement.getStatus()).isEqualTo(APPROVED);
        assertThat(jobAdvertisement.getSourceSystem()).isEqualTo(SourceSystem.RAV);
        assertThat(jobAdvertisement.getStellennummerAvam()).isEqualTo(STELLENNUMMER_AVAM);
        assertThat(jobAdvertisement.getPublication().isEuresAnonymous()).isFalse();
        assertThat(jobAdvertisement.getPublication().isCompanyAnonymous()).isTrue();

        assertThat(jobAdvertisement.isReportingObligation()).isTrue();
        assertThat(jobAdvertisement.getJobContent().getDisplayCompany()).isEqualTo(testDisplayCompany(testJobCenter())
                .setSurrogate(true)
                .build()
        );

        domainEventMockUtils.assertSingleDomainEventPublished(JOB_ADVERTISEMENT_APPROVED.getDomainEventType());
    }

    @Test
    public void shouldApprove() {
        // given
        JobAdvertisement inspectingJobAd = jobAdvertisementRepository.save(
                testJobAdvertisement()
                        .setStatus(INSPECTING)
                        .setJobContent(JobContentFixture.of(job01.id()).build())
                        .build()
        );
        UpdateJobAdvertisementFromAvamDto updateJobAdvertisementFromAvamDto = testUpdateJobAdvertisementFromAvamDto(inspectingJobAd);
        ApprovalDto approvalDto = testApprovalDto(updateJobAdvertisementFromAvamDto);

        // when
        sut.approve(approvalDto);

        // then
        JobAdvertisement repoJobAd = jobAdvertisementRepository.getOne(job01.id());
        assertThat(repoJobAd.getStellennummerAvam()).isEqualTo(STELLENNUMMER_AVAM);
        assertThat(repoJobAd.getApprovalDate()).isEqualTo(approvalDto.getDate());
        assertThat(repoJobAd.isReportingObligation()).isTrue();
        assertThat(repoJobAd.getReportingObligationEndDate()).isEqualTo(approvalDto.getReportingObligationEndDate());
        assertThat(repoJobAd.getStatus()).isEqualTo(APPROVED);
        domainEventMockUtils.assertSingleDomainEventPublished(JOB_ADVERTISEMENT_APPROVED.getDomainEventType());
    }


    @Test
    public void shouldApproveWithUpdate() {
        // given
        JobAdvertisement jobAdvertisementWithInspectingStatus = jobAdvertisementRepository.save(
                testJobAdvertisement()
                        .setStatus(INSPECTING)
                        .setJobContent(JobContentFixture.of(job01.id()).build())
                        .build());
        UpdateJobAdvertisementFromAvamDto updateJobAdvertisementFromAvamDto = testUpdateJobAdvertisementFromAvamDto(jobAdvertisementWithInspectingStatus);
        updateJobAdvertisementFromAvamDto.setDescription("OTHER VALUE");
        ApprovalDto approvalDto = testApprovalDto(updateJobAdvertisementFromAvamDto);

        // when
        sut.approve(approvalDto);

        // then
        JobAdvertisement jobAdvertisement = jobAdvertisementRepository.getOne(jobAdvertisementWithInspectingStatus.getId());
        assertThat(jobAdvertisement.getStellennummerAvam()).isEqualTo(STELLENNUMMER_AVAM);
        assertThat(jobAdvertisement.getApprovalDate()).isEqualTo(approvalDto.getDate());
        assertThat(jobAdvertisement.isReportingObligation()).isTrue();
        assertThat(jobAdvertisement.getReportingObligationEndDate()).isEqualTo(approvalDto.getReportingObligationEndDate());
        assertThat(jobAdvertisement.getStatus()).isEqualTo(APPROVED);
        assertThat(jobAdvertisement.getJobContent().getJobDescriptions().get(0).getDescription()).isEqualTo("OTHER VALUE");
        domainEventMockUtils.assertMultipleDomainEventPublished(2, JOB_ADVERTISEMENT_UPDATED.getDomainEventType());
    }

    @Test
    public void shouldReject() {
        // given
        jobAdvertisementRepository.save(
                testJobAdvertisement()
                        .setStatus(INSPECTING)
                        .setJobContent(JobContentFixture.of(job01.id()).build())
                        .build());
        RejectionDto rejectionDto = testRejectionDto();

        // when
        sut.reject(rejectionDto);

        // then
        JobAdvertisement repoJobAd = jobAdvertisementRepository.getOne(job01.id());
        assertThat(repoJobAd.getStellennummerAvam()).isEqualTo(STELLENNUMMER_AVAM);
        assertThat(repoJobAd.getRejectionDate()).isEqualTo(rejectionDto.getDate());
        assertThat(repoJobAd.getRejectionCode()).isEqualTo("code");
        assertThat(repoJobAd.getRejectionReason()).isEqualTo("reason");
        assertThat(repoJobAd.getJobCenterCode()).isEqualTo("jobcenterid");
        assertThat(repoJobAd.getStatus()).isEqualTo(REJECTED);
        domainEventMockUtils.assertSingleDomainEventPublished(JOB_ADVERTISEMENT_REJECTED.getDomainEventType());
    }
}