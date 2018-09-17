package ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement;

import static ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.JobAdvertisementApplicationService.EXTERN_JOB_AD_REACTIVATION_DAY_NUM;
import static ch.admin.seco.jobs.services.jobadservice.core.time.TimeMachine.now;
import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisementTestDataProvider.JOB_ADVERTISEMENT_ID_01;
import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisementTestDataProvider.JOB_ADVERTISEMENT_ID_02;
import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisementTestDataProvider.JOB_ADVERTISEMENT_ID_03;
import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisementTestDataProvider.JOB_ADVERTISEMENT_ID_04;
import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisementTestDataProvider.JOB_ADVERTISEMENT_ID_05;
import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisementTestDataProvider.createContact;
import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisementTestDataProvider.createJobContent;
import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisementTestDataProvider.createOwner;
import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisementTestDataProvider.prepareJobContentBuilder;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

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
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.ApplyChannelDto;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.CompanyDto;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.ContactDto;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.EmploymentDto;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.JobDescriptionDto;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.LanguageSkillDto;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.OccupationDto;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.PublicContactDto;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.PublicationDto;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.create.CreateJobAdvertisementDto;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.create.CreateLocationDto;
import ch.admin.seco.jobs.services.jobadservice.core.domain.events.DomainEvent;
import ch.admin.seco.jobs.services.jobadservice.core.domain.events.DomainEventMockUtils;
import ch.admin.seco.jobs.services.jobadservice.core.time.TimeMachine;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.CancellationCode;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.Company;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisement;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisementId;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisementRepository;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisementStatus;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobDescription;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.LanguageLevel;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.LanguageSkill;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.Location;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.Publication;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.Salutation;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.SourceSystem;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.WorkExperience;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.events.JobAdvertisementEvents;
import ch.admin.seco.jobs.services.jobadservice.domain.jobcenter.JobCenter;
import ch.admin.seco.jobs.services.jobadservice.domain.jobcenter.JobCenterAddress;
import ch.admin.seco.jobs.services.jobadservice.domain.profession.ProfessionCodeType;

@SpringBootTest
@RunWith(SpringRunner.class)
@Transactional
public class JobAdvertisementApplicationServiceTest {

    private static final String TEST_STELLEN_NUMMER_EGOV = "1000000";

    private static final String STELLENNUMMER_AVAM = "avam";

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

        when(locationService.enrichCodes(any())).thenReturn(
                new Location.Builder()
                        .setRemarks("remarks")
                        .setCity("city")
                        .setPostalCode("postalCode")
                        .setCantonCode("BE")
                        .setCountryIsoCode("CH")
                        .build()
        );
        when(locationService.isLocationValid(any())).thenReturn(Boolean.TRUE);
        when(egovNumberGenerator.nextStringValue()).thenReturn(TEST_STELLEN_NUMMER_EGOV);
    }

    @After
    public void tearDown() {
        this.jobAdvertisementRepository.deleteAll();
        this.domainEventMockUtils.clearEvents();
    }

    private CreateJobAdvertisementDto createDefaultJobAdvertisementDto() {
        return new CreateJobAdvertisementDto(
                true,
                null,
                null,
                new ContactDto(Salutation.MR, "firstName", "lastName", "phone", "email", "de"),
                new PublicationDto(LocalDate.of(2018, 1, 1), LocalDate.of(2018, 3, 1), false, false, false, false, false),
                null,
                Collections.singletonList(new JobDescriptionDto("de", "title", "description")),
                new CompanyDto("name", "street", "houseNumber", "postalCode", "city", "CH", null, null, null, "phone", "email", "website", false),
                null,
                new EmploymentDto(LocalDate.of(2018, 1, 1), LocalDate.of(2018, 12, 31), false, false, false, 80, 100, null),
                new CreateLocationDto("remarks", "city", "postalCode", "CH"),
                new OccupationDto("avamCode", WorkExperience.MORE_THAN_1_YEAR, "educationCode"),
                Collections.singletonList(new LanguageSkillDto("de", LanguageLevel.PROFICIENT, LanguageLevel.PROFICIENT)),
                new ApplyChannelDto("mailAddress", "emailAddress", "phoneNumber", "formUrl", "additionalInfo"),
                new PublicContactDto(Salutation.MR, "firstName", "lastName", "phone", "email")
        );
    }

    @Test
    public void shouldSetReportingObligationToFalseWhenShortEmployment() {
        //Prepare
        CreateJobAdvertisementDto createJobAdvertisementDto = createDefaultJobAdvertisementDto();
        createJobAdvertisementDto.setEmployment(new EmploymentDto(LocalDate.of(2018, 1, 1), LocalDate.of(2018, 1, 10), true, false, false, 80, 100, null));
        when(reportingObligationService.hasReportingObligation(any(), any(), any())).thenReturn(true);

        checkReportingObligation(createJobAdvertisementDto, false);
    }

    private void checkReportingObligation(CreateJobAdvertisementDto createJobAdvertisementDto, boolean expectedValue) {
        //Execute
        JobAdvertisementId jobAdvertisementId = sut.createFromWebForm(createJobAdvertisementDto);

        //Validate
        JobAdvertisement jobAdvertisement = jobAdvertisementRepository.findById(jobAdvertisementId).get();
        assertThat(jobAdvertisement.isReportingObligation()).isEqualTo(expectedValue);
    }

    @Test
    public void shouldSetReportingObligationToFalseWhenLocationIsGermany() {
        //Prepare
        CreateJobAdvertisementDto createJobAdvertisementDto = createDefaultJobAdvertisementDto();
        createJobAdvertisementDto.setLocation(new CreateLocationDto("remarks", "city", "postalCode", "DE"));
        when(locationService.enrichCodes(any(Location.class))).thenAnswer(invocation -> invocation.getArgument(0));

        checkReportingObligation(createJobAdvertisementDto, false);
    }

    @Test
    public void shouldSetReportingObligationToTrueWhenLocationIsSwiss() {
        //Prepare
        CreateJobAdvertisementDto createJobAdvertisementDto = createDefaultJobAdvertisementDto();
        when(reportingObligationService.hasReportingObligation(ProfessionCodeType.AVAM, "avamCode", "BE")).thenReturn(true);

        checkReportingObligation(createJobAdvertisementDto, true);
    }

    @Test
    public void shouldInspect() {
        // given
        jobAdvertisementRepository.save(createJobWithStatusAndPublicationEndDate(
                JOB_ADVERTISEMENT_ID_01, JobAdvertisementStatus.CREATED, null));

        // when
        sut.inspect(JOB_ADVERTISEMENT_ID_01);

        // then
        JobAdvertisement jobAdvertisement = jobAdvertisementRepository.findById(JOB_ADVERTISEMENT_ID_01).get();
        assertThat(jobAdvertisement.getStatus()).isEqualTo(JobAdvertisementStatus.INSPECTING);
        domainEventMockUtils.assertSingleDomainEventPublished(JobAdvertisementEvents.JOB_ADVERTISEMENT_INSPECTING.getDomainEventType());
    }

    @Test
    public void shouldCancel() {
        // given
        jobAdvertisementRepository.save(createJobWithStatusAndPublicationEndDate(
                JOB_ADVERTISEMENT_ID_01, JobAdvertisementStatus.CREATED, null));

        // when
        sut.cancel(JOB_ADVERTISEMENT_ID_01, LocalDate.of(2018, 1, 1), CancellationCode.OCCUPIED_OTHER, SourceSystem.JOBROOM, null);

        // then
        JobAdvertisement jobAdvertisement = jobAdvertisementRepository.findById(JOB_ADVERTISEMENT_ID_01).get();
        assertThat(jobAdvertisement.getCancellationCode()).isEqualTo(CancellationCode.OCCUPIED_OTHER);
        assertThat(jobAdvertisement.getCancellationDate()).isEqualTo(LocalDate.of(2018, 1, 1));
        assertThat(jobAdvertisement.getStatus()).isEqualTo(JobAdvertisementStatus.CANCELLED);
        domainEventMockUtils.assertSingleDomainEventPublished(JobAdvertisementEvents.JOB_ADVERTISEMENT_CANCELLED.getDomainEventType());
    }

    @Test
    public void shouldRefine() {
        // given
        jobAdvertisementRepository.save(createJobWithStatusAndPublicationEndDate(
                JOB_ADVERTISEMENT_ID_01, JobAdvertisementStatus.CREATED, null));

        // when
        sut.refining(JOB_ADVERTISEMENT_ID_01);

        // then
        JobAdvertisement jobAdvertisement = jobAdvertisementRepository.findById(JOB_ADVERTISEMENT_ID_01).get();
        assertThat(jobAdvertisement.getStatus()).isEqualTo(JobAdvertisementStatus.REFINING);
        // TODO: uncomment once we have x28-api
//        domainEventMockUtils.assertSingleDomainEventPublished(JobAdvertisementEvents.JOB_ADVERTISEMENT_REFINING.getDomainEventType());
    }

    @Test
    public void shouldPublish() {
        // given
        jobAdvertisementRepository.save(createJobWithStatusAndPublicationStartDate(JOB_ADVERTISEMENT_ID_05, JobAdvertisementStatus.REFINING, now().toLocalDate()));

        // when
        this.sut.publish(JOB_ADVERTISEMENT_ID_05);

        // then
        DomainEvent domainEvent = domainEventMockUtils.assertSingleDomainEventPublished(JobAdvertisementEvents.JOB_ADVERTISEMENT_PUBLISH_PUBLIC.getDomainEventType());
        assertThat(domainEvent.getAggregateId()).isEqualTo(JOB_ADVERTISEMENT_ID_05);

        JobAdvertisement jobAdvertisementPublishNow = jobAdvertisementRepository.findById(JOB_ADVERTISEMENT_ID_05).get();
        assertThat(jobAdvertisementPublishNow.getStatus()).isEqualTo(JobAdvertisementStatus.PUBLISHED_PUBLIC);
    }

    @Test
    public void shouldNotPublish() {
        // given
        jobAdvertisementRepository.save(createJobWithStatusAndPublicationStartDate(JOB_ADVERTISEMENT_ID_03, JobAdvertisementStatus.REFINING, now().toLocalDate().plusDays(1)));

        // when
        this.sut.publish(JOB_ADVERTISEMENT_ID_03);

        // then
        JobAdvertisement jobAdvertisementPublishLater = jobAdvertisementRepository.findById(JOB_ADVERTISEMENT_ID_03).get();
        assertThat(jobAdvertisementPublishLater.getStatus()).isEqualTo(JobAdvertisementStatus.REFINING);
    }

    @Test
    public void shouldRepublish() {
        // given
        TimeMachine.useFixedClockAt(LocalDateTime.now().minusDays(EXTERN_JOB_AD_REACTIVATION_DAY_NUM));
        jobAdvertisementRepository.save(createJobWithStatusAndPublicationEndDate(JOB_ADVERTISEMENT_ID_01, JobAdvertisementStatus.ARCHIVED, now().toLocalDate().plusDays(60)));
        TimeMachine.reset();

        // when
        sut.republishIfArchived(JOB_ADVERTISEMENT_ID_01);

        // then
        JobAdvertisement jobAdvertisement = jobAdvertisementRepository.findById(JOB_ADVERTISEMENT_ID_01).get();
        assertThat(jobAdvertisement.getStatus()).isEqualTo(JobAdvertisementStatus.PUBLISHED_PUBLIC);
    }

    @Test
    public void shouldNotRepublish() {
        // given
        TimeMachine.useFixedClockAt(LocalDateTime.now().minusDays(EXTERN_JOB_AD_REACTIVATION_DAY_NUM + 1));
        jobAdvertisementRepository.save(createJobWithStatusAndPublicationEndDate(JOB_ADVERTISEMENT_ID_01, JobAdvertisementStatus.ARCHIVED, null));
        TimeMachine.reset();

        // when
        sut.republishIfArchived(JOB_ADVERTISEMENT_ID_01);

        // then
        JobAdvertisement jobAdvertisement = jobAdvertisementRepository.findById(JOB_ADVERTISEMENT_ID_01).get();
        assertThat(jobAdvertisement.getStatus()).isEqualTo(JobAdvertisementStatus.ARCHIVED);
    }

    @Test
    public void scheduledCheckPublicationStarts() {
        // given
        jobAdvertisementRepository.save(createJobWithStatusAndPublicationStartDate(JOB_ADVERTISEMENT_ID_03, JobAdvertisementStatus.REFINING, now().toLocalDate().plusDays(1)));
        jobAdvertisementRepository.save(createJobWithStatusAndPublicationStartDate(JOB_ADVERTISEMENT_ID_04, JobAdvertisementStatus.REFINING, now().toLocalDate().minusDays(1)));
        jobAdvertisementRepository.save(createJobWithStatusAndPublicationStartDate(JOB_ADVERTISEMENT_ID_05, JobAdvertisementStatus.REFINING, now().toLocalDate()));

        // when
        this.sut.scheduledCheckPublicationStarts();

        // then
        DomainEvent domainEvent = domainEventMockUtils.assertMultipleDomainEventPublished(2, JobAdvertisementEvents.JOB_ADVERTISEMENT_PUBLISH_PUBLIC.getDomainEventType());
    }

    @Test
    public void scheduledCheckBlackoutPolicyExpiration() {
        // given
        jobAdvertisementRepository.save(createJobWithStatusAndReportingObligationEndDate(JOB_ADVERTISEMENT_ID_01, JobAdvertisementStatus.CREATED, null));
        jobAdvertisementRepository.save(createJobWithStatusAndReportingObligationEndDate(JOB_ADVERTISEMENT_ID_02, JobAdvertisementStatus.CANCELLED, null));
        jobAdvertisementRepository.save(createJobWithStatusAndReportingObligationEndDate(JOB_ADVERTISEMENT_ID_03, JobAdvertisementStatus.PUBLISHED_RESTRICTED, now().toLocalDate().plusDays(1)));
        jobAdvertisementRepository.save(createJobWithStatusAndReportingObligationEndDate(JOB_ADVERTISEMENT_ID_04, JobAdvertisementStatus.PUBLISHED_RESTRICTED, now().toLocalDate().minusDays(1)));
        jobAdvertisementRepository.save(createJobWithStatusAndReportingObligationEndDate(JOB_ADVERTISEMENT_ID_05, JobAdvertisementStatus.PUBLISHED_RESTRICTED, now().toLocalDate()));

        // when
        this.sut.scheduledCheckBlackoutPolicyExpiration();

        // then
        DomainEvent domainEvent = domainEventMockUtils.assertSingleDomainEventPublished(JobAdvertisementEvents.JOB_ADVERTISEMENT_BLACKOUT_EXPIRED.getDomainEventType());
        assertThat(domainEvent.getAggregateId()).isEqualTo(JOB_ADVERTISEMENT_ID_04);
    }

    @Test
    public void scheduledCheckPublicationExpiration() {
        // given
        jobAdvertisementRepository.save(createJobWithStatusAndPublicationEndDate(JOB_ADVERTISEMENT_ID_01, JobAdvertisementStatus.CREATED, null));
        jobAdvertisementRepository.save(createJobWithStatusAndPublicationEndDate(JOB_ADVERTISEMENT_ID_02, JobAdvertisementStatus.CANCELLED, null));
        jobAdvertisementRepository.save(createJobWithStatusAndPublicationEndDate(JOB_ADVERTISEMENT_ID_03, JobAdvertisementStatus.PUBLISHED_PUBLIC, now().toLocalDate().plusDays(1)));
        jobAdvertisementRepository.save(createJobWithStatusAndPublicationEndDate(JOB_ADVERTISEMENT_ID_04, JobAdvertisementStatus.PUBLISHED_PUBLIC, now().toLocalDate().minusDays(1)));
        jobAdvertisementRepository.save(createJobWithStatusAndPublicationEndDate(JOB_ADVERTISEMENT_ID_05, JobAdvertisementStatus.PUBLISHED_PUBLIC, now().toLocalDate()));

        // when
        this.sut.scheduledCheckPublicationExpiration();

        // then
        DomainEvent domainEvent = domainEventMockUtils.assertSingleDomainEventPublished(JobAdvertisementEvents.JOB_ADVERTISEMENT_PUBLISH_EXPIRED.getDomainEventType());
        assertThat(domainEvent.getAggregateId()).isEqualTo(JOB_ADVERTISEMENT_ID_04);
    }

    @Test
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public void updateJobCenter() {
        final String jobCenterCode = "JOB_CENTER_CODE_AAAA";
        for (int i = 0; i < 100; i++) {
            JobAdvertisement jobAdvertisement = createJobWithAnonymCompanyAndJobCenterCodeAndDisplayCompany(new JobAdvertisementId("Job-Add-Test" + i), JobAdvertisementStatus.CREATED, jobCenterCode);
            jobAdvertisementRepository.saveAndFlush(jobAdvertisement);
        }

        List<JobCenter> jobCenters = new ArrayList<>();
        JobCenter testRav = new JobCenter()
                .setId("1")
                .setCode(jobCenterCode)
                .setShowContactDetailsToPublic(true)
                .setAddress(new JobCenterAddress()
                        .setName("TEST RAV")
                        .setStreet("Test-Street")
                        .setCity("Test-City")
                );
        jobCenters.add(testRav);

        when(this.jobCenterService.findAllJobCenters()).thenReturn(jobCenters);

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

    private JobAdvertisement createJobWithStatusAndPublicationStartDate(JobAdvertisementId jobAdvertisementId, JobAdvertisementStatus status, LocalDate publicationStartDate) {
        return new JobAdvertisement.Builder()
                .setId(jobAdvertisementId)
                .setOwner(createOwner(jobAdvertisementId))
                .setContact(createContact(jobAdvertisementId))
                .setJobContent(createJobContent(jobAdvertisementId))
                .setPublication(new Publication.Builder().setStartDate(publicationStartDate).setEndDate(publicationStartDate.plusMonths(1)).build())
                .setSourceSystem(SourceSystem.JOBROOM)
                .setStellennummerEgov(jobAdvertisementId.getValue())
                .setStatus(status)
                .build();
    }

    private JobAdvertisement createJobWithStatusAndReportingObligationEndDate(JobAdvertisementId jobAdvertisementId, JobAdvertisementStatus status, LocalDate reportingObligationEndDate) {
        return new JobAdvertisement.Builder()
                .setId(jobAdvertisementId)
                .setOwner(createOwner(jobAdvertisementId))
                .setContact(createContact(jobAdvertisementId))
                .setJobContent(createJobContent(jobAdvertisementId))
                .setPublication(new Publication.Builder().build())
                .setSourceSystem(SourceSystem.JOBROOM)
                .setStellennummerEgov(jobAdvertisementId.getValue())
                .setStatus(status)
                .setReportingObligationEndDate(reportingObligationEndDate)
                .build();
    }

    private JobAdvertisement createJobWithStatusAndPublicationEndDate(JobAdvertisementId jobAdvertisementId, JobAdvertisementStatus status, LocalDate publicationEndDate) {
        return new JobAdvertisement.Builder()
                .setId(jobAdvertisementId)
                .setOwner(createOwner(jobAdvertisementId))
                .setContact(createContact(jobAdvertisementId))
                .setJobContent(createJobContent(jobAdvertisementId))
                .setPublication(new Publication.Builder().setEndDate(publicationEndDate).build())
                .setSourceSystem(SourceSystem.JOBROOM)
                .setStellennummerEgov(jobAdvertisementId.getValue())
                .setStellennummerAvam(STELLENNUMMER_AVAM)
                .setStatus(status)
                .build();
    }

    private JobAdvertisement createJobWithAnonymCompanyAndJobCenterCodeAndDisplayCompany(JobAdvertisementId jobAdvertisementId, JobAdvertisementStatus status, String jobCenterCode) {
        return new JobAdvertisement.Builder()
                .setId(jobAdvertisementId)
                .setOwner(createOwner(jobAdvertisementId))
                .setContact(createContact(jobAdvertisementId))
                .setJobContent(prepareJobContentBuilder(jobAdvertisementId)
                        .setJobDescriptions(Collections.singletonList(new JobDescription.Builder()
                                .setTitle("Test TITLE")
                                .setDescription("TEST JOB DESC")
                                .setLanguage(Locale.GERMAN)
                                .build()))
                        .setLanguageSkills(Collections.singletonList(new LanguageSkill.Builder().setLanguageIsoCode("de").build()))
                        .setDisplayCompany(new Company.Builder<>().setName("Test-Company").setCity("Test-Cizy").setPostalCode("1234").build())
                        .build())
                .setPublication(new Publication.Builder().setCompanyAnonymous(true).build())
                .setSourceSystem(SourceSystem.JOBROOM)
                .setStellennummerEgov(jobAdvertisementId.getValue())
                .setStellennummerAvam(STELLENNUMMER_AVAM)
                .setStatus(status)
                .setJobCenterCode(jobCenterCode)
                .build();
    }

}
