package ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement;

import ch.admin.seco.jobs.services.jobadservice.application.LocationService;
import ch.admin.seco.jobs.services.jobadservice.application.ProfessionService;
import ch.admin.seco.jobs.services.jobadservice.application.RavRegistrationService;
import ch.admin.seco.jobs.services.jobadservice.application.ReportingObligationService;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.*;
import ch.admin.seco.jobs.services.jobadservice.core.domain.events.DomainEvent;
import ch.admin.seco.jobs.services.jobadservice.core.domain.events.DomainEventMockUtils;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.*;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.events.JobAdvertisementEvents;
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
import java.util.Locale;

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

    private DomainEventMockUtils domainEventMockUtils;

    @MockBean
    private RavRegistrationService ravRegistrationService;

    @MockBean
    private ReportingObligationService reportingObligationService;

    @MockBean
    private LocationService locationService;

    @MockBean
    private ProfessionService professionService;

    @Autowired
    private JobAdvertisementRepository jobAdvertisementRepository;

    @MockBean
    private DataFieldMaxValueIncrementer egovNumberGenerator;

    @Autowired
    private JobAdvertisementApplicationService sut; //System Under Test

    @Before
    public void setUp() {
        domainEventMockUtils = new DomainEventMockUtils();

        when(locationService.enrichCodes(any())).thenReturn(new Location("remarks", "ctiy", "postalCode", null, null, "BE", "CH", null));
        when(egovNumberGenerator.nextStringValue()).thenReturn(TEST_STELLEN_NUMMER_EGOV);
    }

    @After
    public void tearDown() {
        domainEventMockUtils.clearEvents();
    }

    @Test
    public void createFromWebForm() {
        //Prepare
        CreateJobAdvertisementWebFormDto createJobAdvertisementWebFormDto = new CreateJobAdvertisementWebFormDto(
                true,
                "de",
                "title",
                "description",
                new EmploymentDto(LocalDate.of(2018, 1, 1), LocalDate.of(2018, 12, 31), 365, true, false, 80, 100),
                "drivingLicenseLevel",
                new ApplyChannelDto("mailAddress", "emailAddress", "phoneNumber", "formUrl", "additionalInfo"),
                new CompanyDto("name", "stree", "houseNumber", "postalCode", "city", "CH", null, null, null, "phone", "email", "website"),
                new ContactDto(Salutation.MR, "firstName", "lastName", "phone", "email", "de"),
                new CreateLocationDto("remarks", "ctiy", "postalCode", "CH"),
                new OccupationDto("avamCode", WorkExperience.MORE_THAN_1_YEAR, "educationCode"),
                Collections.singletonList(new LanguageSkillDto("de", LanguageLevel.PROFICIENT, LanguageLevel.PROFICIENT))
        );

        //Execute
        JobAdvertisementId jobAdvertisementId = sut.createFromWebForm(createJobAdvertisementWebFormDto);

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
        CreateJobAdvertisementApiDto jobAdvertisementApiDto = new CreateJobAdvertisementApiDto(
                false,
                LocalDate.of(2018, 1, 1),
                LocalDate.of(2018, 12, 31),
                "ref",
                "http://url",
                new ApplyChannelDto("mailAddress", "emailAddress", "phoneNumber", "formUrl", "additionalInfo"),
                new JobApiDto("de", "title", "descriptioin", 10, 90,
                        LocalDate.of(2018, 1, 1),
                        LocalDate.of(2018, 12, 31), 30, true, true,
                        new CreateLocationDto("remarks", "ctiy", "postalCode", "CH"),
                        Collections.emptyList()),
                new CompanyDto("name", "stree", "houseNumber", "postalCode", "city", "CH", null, null, null, "phone", "email", "website"),
                new ContactDto(Salutation.MR, "firstName", "lastName", "phone", "email", "de"),
                new OccupationDto("avamCode", WorkExperience.MORE_THAN_1_YEAR, "educationCode")
        );

        //Execute
        JobAdvertisementId jobAdvertisementId = sut.createFromApi(jobAdvertisementApiDto);

        //Validate
        JobAdvertisement jobAdvertisement = jobAdvertisementRepository.getOne(jobAdvertisementId);
        assertThat(jobAdvertisement).isNotNull();
        assertThat(jobAdvertisement.getStatus()).isEqualTo(JobAdvertisementStatus.CREATED);
        assertThat(jobAdvertisement.getSourceSystem()).isEqualTo(SourceSystem.API);

        domainEventMockUtils.assertSingleDomainEventPublished(JobAdvertisementEvents.JOB_ADVERTISEMENT_CREATED.getDomainEventType());
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
        this.sut.checkBlackoutPolicyExpiration();

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
        this.sut.checkPublicationExpiration();

        // then
        DomainEvent domainEvent = domainEventMockUtils.assertSingleDomainEventPublished(JobAdvertisementEvents.JOB_ADVERTISEMENT_PUBLISH_EXPIRED.getDomainEventType());
        assertThat(domainEvent.getAggregateId()).isEqualTo(JOB_ADVERTISEMENT_ID_04);
    }


    private JobAdvertisement createJobWithStatusAndReportingObligationEndDate(JobAdvertisementId jobAdvertisementId, JobAdvertisementStatus status, LocalDate reportingObligationEndDate) {
        return new JobAdvertisement.Builder()
                .setOwner(createOwner(jobAdvertisementId))
                .setContact(createContact(jobAdvertisementId))
                .setId(jobAdvertisementId)
                .setSourceSystem(SourceSystem.JOBROOM)
                .setJobContent(createJobContent(jobAdvertisementId))
                .setStellennummerEgov(jobAdvertisementId.getValue())
                .setStatus(status)
                .setReportingObligationEndDate(reportingObligationEndDate)
                .setPublication(new Publication.Builder().build())
                .build();
    }

    private JobAdvertisement createJobWithStatusAndPublicationEndDate(JobAdvertisementId jobAdvertisementId, JobAdvertisementStatus status, LocalDate publicationEndDate) {
        return new JobAdvertisement.Builder()
                .setOwner(createOwner(jobAdvertisementId))
                .setContact(createContact(jobAdvertisementId))
                .setId(jobAdvertisementId)
                .setSourceSystem(SourceSystem.JOBROOM)
                .setJobContent(createJobContent(jobAdvertisementId))
                .setStellennummerEgov(jobAdvertisementId.getValue())
                .setStatus(status)
                .setPublication(new Publication.Builder().setEndDate(publicationEndDate).build())
                .build();
    }

    private Contact createContact(JobAdvertisementId jobAdvertisementId) {
        return new Contact.Builder()
                .setLanguage(Locale.GERMAN)
                .setSalutation(Salutation.MR)
                .setEmail(String.format("mail-%s@mail.com", jobAdvertisementId.getValue()))
                .setPhone(String.format("+41 %s", jobAdvertisementId.getValue()))
                .setFirstName(String.format("first-name-%s", jobAdvertisementId.getValue()))
                .setLastName(String.format("last-name-%s", jobAdvertisementId.getValue()))
                .build();
    }

    private Owner createOwner(JobAdvertisementId jobAdvertisementId) {
        return new Owner.Builder()
                .setAccessToken(String.format("access-token-%s", jobAdvertisementId.getValue()))
                .setAvgId(String.format("avg-id-%s", jobAdvertisementId.getValue()))
                .setUserId(String.format("user-id-%s", jobAdvertisementId.getValue()))
                .build();
    }

    private JobContent createJobContent(JobAdvertisementId jobAdvertisementId) {
        JobDescription jobDescription = new JobDescription(
                Locale.GERMAN,
                String.format("title-%s", jobAdvertisementId.getValue()),
                String.format("description-%s", jobAdvertisementId.getValue())
        );
        return new JobContent.Builder()
                .setJobDescriptions(Collections.singletonList(jobDescription))
                .build();
    }

}
