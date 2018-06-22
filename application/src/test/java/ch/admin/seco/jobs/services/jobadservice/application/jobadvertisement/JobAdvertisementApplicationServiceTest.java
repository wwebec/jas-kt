package ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement;

import ch.admin.seco.jobs.services.jobadservice.application.JobCenterService;
import ch.admin.seco.jobs.services.jobadservice.application.LocationService;
import ch.admin.seco.jobs.services.jobadservice.application.ProfessionService;
import ch.admin.seco.jobs.services.jobadservice.application.ReportingObligationService;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.*;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.create.CreateJobAdvertisementDto;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.create.CreateJobAdvertisementFromAvamDto;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.create.CreateJobAdvertisementFromX28Dto;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.create.CreateLocationDto;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.update.ApprovalDto;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.update.RejectionDto;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.update.UpdateJobAdvertisementFromX28Dto;
import ch.admin.seco.jobs.services.jobadservice.core.domain.events.DomainEvent;
import ch.admin.seco.jobs.services.jobadservice.core.domain.events.DomainEventMockUtils;
import ch.admin.seco.jobs.services.jobadservice.core.time.TimeMachine;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.*;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.events.JobAdvertisementEvents;
import org.assertj.core.util.Sets;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.jdbc.support.incrementer.DataFieldMaxValueIncrementer;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.Collections;

import static ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.JobAdvertisementApplicationService.COUNTRY_ISO_CODE_SWITZERLAND;
import static ch.admin.seco.jobs.services.jobadservice.core.time.TimeMachine.now;
import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisementTestDataProvider.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@RunWith(SpringRunner.class)
@DataJpaTest
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
        domainEventMockUtils = new DomainEventMockUtils();

        when(locationService.enrichCodes(any())).thenReturn(
                new Location.Builder()
                        .setRemarks("remarks")
                        .setCity("ctiy")
                        .setPostalCode("postalCode")
                        .setCantonCode("BE")
                        .setCountryIsoCode("CH")
                        .build()
        );
        when(egovNumberGenerator.nextStringValue()).thenReturn(TEST_STELLEN_NUMMER_EGOV);
    }

    @After
    public void tearDown() {
        domainEventMockUtils.clearEvents();
    }

    @Test
    public void createFromWebForm() {
        //Prepare
        CreateJobAdvertisementDto createJobAdvertisementDto = new CreateJobAdvertisementDto(
                true,
                null,
                null,
                new ContactDto(Salutation.MR, "firstName", "lastName", "phone", "email", "de"),
                new PublicationDto(LocalDate.of(2018, 1, 1), LocalDate.of(2018, 3, 1), false, false, false, false, false, false),
                Collections.singletonList(new JobDescriptionDto("de", "title", "description")),
                new CompanyDto("name", "stree", "houseNumber", "postalCode", "city", "CH", null, null, null, "phone", "email", "website", false),
                null,
                new EmploymentDto(LocalDate.of(2018, 1, 1), LocalDate.of(2018, 12, 31), false, false, false, 80, 100, null),
                new CreateLocationDto("remarks", "ctiy", "postalCode", "CH"),
                new OccupationDto("avamCode", WorkExperience.MORE_THAN_1_YEAR, "educationCode"),
                Collections.singletonList(new LanguageSkillDto("de", LanguageLevel.PROFICIENT, LanguageLevel.PROFICIENT)),
                new ApplyChannelDto("mailAddress", "emailAddress", "phoneNumber", "formUrl", "additionalInfo"),
                new PublicContactDto(Salutation.MR, "firstName", "lastName", "phone", "email")
        );

        //Execute
        JobAdvertisementId jobAdvertisementId = sut.createFromWebForm(createJobAdvertisementDto);

        //Validate
        JobAdvertisement jobAdvertisement = jobAdvertisementRepository.getOne(jobAdvertisementId);
        assertThat(jobAdvertisement).isNotNull();
        assertThat(jobAdvertisement.getStatus()).isEqualTo(JobAdvertisementStatus.CREATED);
        assertThat(jobAdvertisement.getSourceSystem()).isEqualTo(SourceSystem.JOBROOM);
        assertThat(jobAdvertisement.getStellennummerEgov()).isEqualTo(TEST_STELLEN_NUMMER_EGOV);

        domainEventMockUtils.assertSingleDomainEventPublished(JobAdvertisementEvents.JOB_ADVERTISEMENT_CREATED.getDomainEventType());
    }

    @Test
    public void createFromApi() {
        //Prepare
        CreateJobAdvertisementDto createJobAdvertisementDto = new CreateJobAdvertisementDto(
                false,
                null,
                null,
                new ContactDto(Salutation.MR, "firstName", "lastName", "phone", "email", "de"),
                new PublicationDto(LocalDate.of(2018, 1, 1), LocalDate.of(2018, 3, 1), false, false, false, false, false, false),
                Collections.singletonList(new JobDescriptionDto("de", "title", "description")),
                new CompanyDto("name", "stree", "houseNumber", "postalCode", "city", "CH", null, null, null, "phone", "email", "website", false),
                null,
                new EmploymentDto(LocalDate.of(2018, 1, 1), LocalDate.of(2018, 12, 31), false, false, false, 80, 100, null),
                new CreateLocationDto("remarks", "ctiy", "postalCode", "CH"),
                new OccupationDto("avamCode", WorkExperience.MORE_THAN_1_YEAR, "educationCode"),
                Collections.singletonList(new LanguageSkillDto("de", LanguageLevel.PROFICIENT, LanguageLevel.PROFICIENT)),
                new ApplyChannelDto("mailAddress", "emailAddress", "phoneNumber", "formUrl", "additionalInfo"),
                new PublicContactDto(Salutation.MR, "firstName", "lastName", "phone", "email")
        );

        //Execute
        JobAdvertisementId jobAdvertisementId = sut.createFromApi(createJobAdvertisementDto);

        //Validate
        JobAdvertisement jobAdvertisement = jobAdvertisementRepository.getOne(jobAdvertisementId);
        assertThat(jobAdvertisement).isNotNull();
        assertThat(jobAdvertisement.getStatus()).isEqualTo(JobAdvertisementStatus.CREATED);
        assertThat(jobAdvertisement.getSourceSystem()).isEqualTo(SourceSystem.API);

        domainEventMockUtils.assertSingleDomainEventPublished(JobAdvertisementEvents.JOB_ADVERTISEMENT_CREATED.getDomainEventType());
    }

    @Test
    public void createFromAvam() {
        //Prepare
        CreateJobAdvertisementFromAvamDto createJobAdvertisementDto = new CreateJobAdvertisementFromAvamDto(
                STELLENNUMMER_AVAM,
                "title",
                "description",
                "de",
                true,
                LocalDate.of(2018, 1, 1),
                "jobCenter",
                TimeMachine.now().toLocalDate(),
                new EmploymentDto(LocalDate.of(2018, 1, 1), LocalDate.of(2018, 12, 31), false, false, false, 80, 100, null),
                new ApplyChannelDto("mailAddress", "emailAddress", "phoneNumber", "formUrl", "additionalInfo"),
                new CompanyDto("name", "stree", "houseNumber", "postalCode", "city", "CH", null, null, null, "phone", "email", "website", false),
                new ContactDto(Salutation.MR, "firstName", "lastName", "phone", "email", "de"),
                new CreateLocationDto("remarks", "ctiy", "postalCode", "CH"),
                Collections.singletonList(new OccupationDto("avamCode", WorkExperience.MORE_THAN_1_YEAR, "educationCode")),
                Collections.singletonList(new LanguageSkillDto("de", LanguageLevel.PROFICIENT, LanguageLevel.PROFICIENT)),
                new PublicationDto(LocalDate.of(2018, 1, 1), LocalDate.of(2018, 3, 1), false, false, false, false, false, false),
                Sets.newHashSet()
        );

        //Execute
        JobAdvertisementId jobAdvertisementId = sut.createFromAvam(createJobAdvertisementDto);

        //Validate
        JobAdvertisement jobAdvertisement = jobAdvertisementRepository.getOne(jobAdvertisementId);
        assertThat(jobAdvertisement).isNotNull();
        assertThat(jobAdvertisement.getStatus()).isEqualTo(JobAdvertisementStatus.REFINING);
        assertThat(jobAdvertisement.getSourceSystem()).isEqualTo(SourceSystem.RAV);

        domainEventMockUtils.assertSingleDomainEventPublished(JobAdvertisementEvents.JOB_ADVERTISEMENT_REFINING.getDomainEventType());
    }

    @Test
    public void createFromX28() {
        //Prepare
        CreateJobAdvertisementFromX28Dto createJobAdvertisementDto = new CreateJobAdvertisementFromX28Dto(
                "title",
                "description",
                "fingerprint",
                "url",
                new EmploymentDto(LocalDate.of(2018, 1, 1), LocalDate.of(2018, 12, 31), false, false, false, 80, 100, null),
                new CompanyDto("name", "stree", "houseNumber", "postalCode", "city", "CH", null, null, null, "phone", "email", "website", false),
                new CreateLocationDto("remarks", "ctiy", "postalCode", "CH"),
                Collections.singletonList(new OccupationDto("avamCode", WorkExperience.MORE_THAN_1_YEAR, "educationCode")),
                "1,2",
                Collections.singletonList(new LanguageSkillDto("de", LanguageLevel.PROFICIENT, LanguageLevel.PROFICIENT)),
                TimeMachine.now().toLocalDate(),
                null
        );

        //Execute
        JobAdvertisementId jobAdvertisementId = sut.createFromX28(createJobAdvertisementDto);

        //Validate
        JobAdvertisement jobAdvertisement = jobAdvertisementRepository.getOne(jobAdvertisementId);
        assertThat(jobAdvertisement).isNotNull();
        assertThat(jobAdvertisement.getStatus()).isEqualTo(JobAdvertisementStatus.PUBLISHED_PUBLIC);
        assertThat(jobAdvertisement.getSourceSystem()).isEqualTo(SourceSystem.EXTERN);

        domainEventMockUtils.assertSingleDomainEventPublished(JobAdvertisementEvents.JOB_ADVERTISEMENT_PUBLISH_PUBLIC.getDomainEventType());
    }

    @Test
    public void createFromX28WithEmptyCountry() {
        //Prepare
        CreateJobAdvertisementFromX28Dto createJobAdvertisementDto = new CreateJobAdvertisementFromX28Dto(
                "title",
                "description",
                "fingerprint",
                "url",
                new EmploymentDto(LocalDate.of(2018, 1, 1), LocalDate.of(2018, 12, 31), false, false, false, 80, 100, null),
                new CompanyDto("name", "stree", "houseNumber", "postalCode", "city", "CH", null, null, null, "phone", "email", "website", false),
                new CreateLocationDto(null, "ctiy", "postalCode", null),
                Collections.singletonList(new OccupationDto("avamCode", WorkExperience.MORE_THAN_1_YEAR, "educationCode")),
                "1,2",
                Collections.singletonList(new LanguageSkillDto("de", LanguageLevel.PROFICIENT, LanguageLevel.PROFICIENT)),
                TimeMachine.now().toLocalDate(),
                null
        );

        //Execute
        JobAdvertisementId jobAdvertisementId = sut.createFromX28(createJobAdvertisementDto);

        //Validate
        JobAdvertisement jobAdvertisement = jobAdvertisementRepository.getOne(jobAdvertisementId);
        assertThat(jobAdvertisement).isNotNull();
        assertThat(jobAdvertisement.getJobContent().getLocation().getCountryIsoCode()).isEqualTo(COUNTRY_ISO_CODE_SWITZERLAND);
    }

    @Test
    public void updateFromX28() {
        // given
        jobAdvertisementRepository.save(createJobWithStatusAndPublicationEndDate(
                JOB_ADVERTISEMENT_ID_01, JobAdvertisementStatus.CREATED, null));
        UpdateJobAdvertisementFromX28Dto updateJobAdvertisementFromX28Dto = new UpdateJobAdvertisementFromX28Dto(
                JOB_ADVERTISEMENT_ID_01.getValue(), "fingerprint", "x28");

        // when
        sut.updateFromX28(updateJobAdvertisementFromX28Dto);

        // then
        JobAdvertisement jobAdvertisement = jobAdvertisementRepository.getOne(JOB_ADVERTISEMENT_ID_01);
        assertThat(jobAdvertisement.getFingerprint()).isEqualTo("fingerprint");
        assertThat(jobAdvertisement.getJobContent().getX28OccupationCodes()).isEqualTo("x28");
        domainEventMockUtils.assertSingleDomainEventPublished(JobAdvertisementEvents.JOB_ADVERTISEMENT_UPDATED.getDomainEventType());
    }

    @Test
    public void shouldInspect() {
        // given
        jobAdvertisementRepository.save(createJobWithStatusAndPublicationEndDate(
                JOB_ADVERTISEMENT_ID_01, JobAdvertisementStatus.CREATED, null));

        // when
        sut.inspect(JOB_ADVERTISEMENT_ID_01);

        // then
        JobAdvertisement jobAdvertisement = jobAdvertisementRepository.getOne(JOB_ADVERTISEMENT_ID_01);
        assertThat(jobAdvertisement.getStatus()).isEqualTo(JobAdvertisementStatus.INSPECTING);
        domainEventMockUtils.assertSingleDomainEventPublished(JobAdvertisementEvents.JOB_ADVERTISEMENT_INSPECTING.getDomainEventType());
    }

    @Test
    public void shouldApprove() {
        // given
        jobAdvertisementRepository.save(createJobWithStatusAndPublicationEndDate(
                JOB_ADVERTISEMENT_ID_01, JobAdvertisementStatus.INSPECTING, null));
        ApprovalDto approvalDto = new ApprovalDto(JOB_ADVERTISEMENT_ID_01.getValue(), STELLENNUMMER_AVAM, LocalDate.of(2018, 1, 1), true, LocalDate.of(2018, 10, 1));

        // when
        sut.approve(approvalDto);

        // then
        JobAdvertisement jobAdvertisement = jobAdvertisementRepository.getOne(JOB_ADVERTISEMENT_ID_01);
        assertThat(jobAdvertisement.getStellennummerAvam()).isEqualTo(STELLENNUMMER_AVAM);
        assertThat(jobAdvertisement.getApprovalDate()).isEqualTo(LocalDate.of(2018, 1, 1));
        assertThat(jobAdvertisement.isReportingObligation()).isTrue();
        assertThat(jobAdvertisement.getReportingObligationEndDate()).isEqualTo(LocalDate.of(2018, 10, 1));
        assertThat(jobAdvertisement.getStatus()).isEqualTo(JobAdvertisementStatus.APPROVED);
        domainEventMockUtils.assertSingleDomainEventPublished(JobAdvertisementEvents.JOB_ADVERTISEMENT_APPROVED.getDomainEventType());
    }

    @Test
    public void shouldReject() {
        // given
        jobAdvertisementRepository.save(createJobWithStatusAndPublicationEndDate(
                JOB_ADVERTISEMENT_ID_01, JobAdvertisementStatus.INSPECTING, null));
        RejectionDto rejectionDto = new RejectionDto(JOB_ADVERTISEMENT_ID_01.getValue(), STELLENNUMMER_AVAM, LocalDate.of(2018, 1, 1), "code", "reason");

        // when
        sut.reject(rejectionDto);

        // then
        JobAdvertisement jobAdvertisement = jobAdvertisementRepository.getOne(JOB_ADVERTISEMENT_ID_01);
        assertThat(jobAdvertisement.getStellennummerAvam()).isEqualTo(STELLENNUMMER_AVAM);
        assertThat(jobAdvertisement.getRejectionDate()).isEqualTo(LocalDate.of(2018, 1, 1));
        assertThat(jobAdvertisement.getRejectionCode()).isEqualTo("code");
        assertThat(jobAdvertisement.getRejectionReason()).isEqualTo("reason");
        assertThat(jobAdvertisement.getStatus()).isEqualTo(JobAdvertisementStatus.REJECTED);
        domainEventMockUtils.assertSingleDomainEventPublished(JobAdvertisementEvents.JOB_ADVERTISEMENT_REJECTED.getDomainEventType());
    }

    @Test
    public void shouldCancel() {
        // given
        jobAdvertisementRepository.save(createJobWithStatusAndPublicationEndDate(
                JOB_ADVERTISEMENT_ID_01, JobAdvertisementStatus.CREATED, null));

        // when
        sut.cancel(JOB_ADVERTISEMENT_ID_01, LocalDate.of(2018, 1, 1), CancellationCode.OCCUPIED_OTHER, null);

        // then
        JobAdvertisement jobAdvertisement = jobAdvertisementRepository.getOne(JOB_ADVERTISEMENT_ID_01);
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
        JobAdvertisement jobAdvertisement = jobAdvertisementRepository.getOne(JOB_ADVERTISEMENT_ID_01);
        assertThat(jobAdvertisement.getStatus()).isEqualTo(JobAdvertisementStatus.REFINING);
        // TODO: uncomment once we have x28-api
//        domainEventMockUtils.assertSingleDomainEventPublished(JobAdvertisementEvents.JOB_ADVERTISEMENT_REFINING.getDomainEventType());
    }

    @Test
    public void checkPublicationStarts() {
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
    public void checkBlackoutPolicyExpiration() {
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
    public void checkPublicationExpiration() {
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

}
