package ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement;

import ch.admin.seco.jobs.services.jobadservice.application.JobCenterService;
import ch.admin.seco.jobs.services.jobadservice.application.LocationService;
import ch.admin.seco.jobs.services.jobadservice.application.ProfessionService;
import ch.admin.seco.jobs.services.jobadservice.application.ReportingObligationService;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.*;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.create.CreateJobAdvertisementFromAvamDto;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.create.CreateLocationDto;
import ch.admin.seco.jobs.services.jobadservice.core.domain.events.DomainEventMockUtils;
import ch.admin.seco.jobs.services.jobadservice.core.time.TimeMachine;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.*;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.events.JobAdvertisementEvents;
import ch.admin.seco.jobs.services.jobadservice.domain.jobcenter.JobCenter;
import ch.admin.seco.jobs.services.jobadservice.domain.jobcenter.JobCenterAddress;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@RunWith(SpringRunner.class)
@DataJpaTest
public class JobAdvertisementApplicationServiceForAvamTest {

    private static final String TEST_STELLEN_NUMMER_EGOV = "1000000";
    private static final String STELLENNUMMER_AVAM = "avam";

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
        when(jobCenterService.findJobCenterByCode(any())).thenReturn(new JobCenter(
                "jobCenterId",
                "jobCenterCode",
                "jobCenterEmail",
                "jobCenterPhone",
                "jobCenterFax",
                true,
                new JobCenterAddress(
                        "jobCenterName",
                        "jobCenterCity",
                        "jobCenterStreet",
                        "jobCenterHouseNumber",
                        "jobCenterPostalCode"
                )
        ));
        when(egovNumberGenerator.nextStringValue()).thenReturn(TEST_STELLEN_NUMMER_EGOV);
    }

    @After
    public void tearDown() {
        domainEventMockUtils.clearEvents();
    }

    @Test
    public void createFromAvam() {
        //Prepare
        Company company = new Company.Builder()
                .setName("name")
                .setStreet("street")
                .setHouseNumber("houseNumber")
                .setPostalCode("postalCode")
                .setCity("city")
                .setCountryIsoCode("CH")
                .setPhone("phone")
                .setEmail("email")
                .setWebsite("website")
                .build();
        CreateJobAdvertisementFromAvamDto createJobAdvertisementDto = new CreateJobAdvertisementFromAvamDto(
                STELLENNUMMER_AVAM,
                "title",
                "description",
                "de",
                null,
                true,
                LocalDate.of(2018, 1, 1),
                "jobCenter",
                TimeMachine.now().toLocalDate(),
                new EmploymentDto(LocalDate.of(2018, 1, 1), LocalDate.of(2018, 12, 31), false, false, false, 80, 100, Sets.newHashSet()),
                new ApplyChannelDto("mailAddress", "emailAddress", "phoneNumber", "formUrl", "additionalInfo"),
                CompanyDto.toDto(company),
                new ContactDto(Salutation.MR, "firstName", "lastName", "phone", "email", "de"),
                new CreateLocationDto("remarks", "city", "postalCode", "CH"),
                Collections.singletonList(new OccupationDto("avamCode", WorkExperience.MORE_THAN_1_YEAR, "educationCode")),
                Collections.singletonList(new LanguageSkillDto("de", LanguageLevel.PROFICIENT, LanguageLevel.PROFICIENT)),
                new PublicationDto(LocalDate.of(2018, 1, 1), LocalDate.of(2018, 3, 1), false, false, false, false, false)
        );

        //Execute
        JobAdvertisementId jobAdvertisementId = sut.createFromAvam(createJobAdvertisementDto);

        //Validate
        JobAdvertisement jobAdvertisement = jobAdvertisementRepository.getOne(jobAdvertisementId);
        assertThat(jobAdvertisement).isNotNull();
        assertThat(jobAdvertisement.getStatus()).isEqualTo(JobAdvertisementStatus.APPROVED);
        assertThat(jobAdvertisement.getSourceSystem()).isEqualTo(SourceSystem.RAV);
        assertThat(jobAdvertisement.getStellennummerAvam()).isEqualTo(STELLENNUMMER_AVAM);
        assertThat(jobAdvertisement.getPublication().isEuresAnonymous()).isFalse();
        assertThat(jobAdvertisement.getPublication().isCompanyAnonymous()).isFalse();

        assertThat(jobAdvertisement.isReportingObligation()).isTrue();
        assertThat(jobAdvertisement.getJobContent().getDisplayCompany()).isEqualTo(company);

        domainEventMockUtils.assertSingleDomainEventPublished(JobAdvertisementEvents.JOB_ADVERTISEMENT_APPROVED.getDomainEventType());
    }

    @Test
    public void createFromAvamWithCompanyAnonymous() {
        //Prepare
        Company jobCenter = new Company.Builder()
                .setName("jobCenterName")
                .setStreet("jobCenterStreet")
                .setHouseNumber("jobCenterHouseNumber")
                .setPostalCode("jobCenterPostalCode")
                .setCity("jobCenterCity")
                .setCountryIsoCode("CH")
                .setPhone("jobCenterPhone")
                .setEmail("jobCenterEmail")
                .setSurrogate(true)
                .build();
        CreateJobAdvertisementFromAvamDto createJobAdvertisementDto = new CreateJobAdvertisementFromAvamDto(
                STELLENNUMMER_AVAM,
                "title",
                "description",
                "de",
                null,
                true,
                LocalDate.of(2018, 1, 1),
                "jobCenter",
                TimeMachine.now().toLocalDate(),
                new EmploymentDto(LocalDate.of(2018, 1, 1), LocalDate.of(2018, 12, 31), false, false, false, 80, 100, Sets.newHashSet()),
                new ApplyChannelDto("mailAddress", "emailAddress", "phoneNumber", "formUrl", "additionalInfo"),
                new CompanyDto("name", "street", "houseNumber", "postalCode", "city", "CH", null, null, null, "phone", "email", "website", false),
                new ContactDto(Salutation.MR, "firstName", "lastName", "phone", "email", "de"),
                new CreateLocationDto("remarks", "city", "postalCode", "CH"),
                Collections.singletonList(new OccupationDto("avamCode", WorkExperience.MORE_THAN_1_YEAR, "educationCode")),
                Collections.singletonList(new LanguageSkillDto("de", LanguageLevel.PROFICIENT, LanguageLevel.PROFICIENT)),
                new PublicationDto(LocalDate.of(2018, 1, 1), LocalDate.of(2018, 3, 1), false, false, false, false, true)
        );

        //Execute
        JobAdvertisementId jobAdvertisementId = sut.createFromAvam(createJobAdvertisementDto);

        //Validate
        JobAdvertisement jobAdvertisement = jobAdvertisementRepository.getOne(jobAdvertisementId);
        assertThat(jobAdvertisement).isNotNull();
        assertThat(jobAdvertisement.getStatus()).isEqualTo(JobAdvertisementStatus.APPROVED);
        assertThat(jobAdvertisement.getSourceSystem()).isEqualTo(SourceSystem.RAV);
        assertThat(jobAdvertisement.getStellennummerAvam()).isEqualTo(STELLENNUMMER_AVAM);
        assertThat(jobAdvertisement.getPublication().isEuresAnonymous()).isFalse();
        assertThat(jobAdvertisement.getPublication().isCompanyAnonymous()).isTrue();

        assertThat(jobAdvertisement.isReportingObligation()).isTrue();
        assertThat(jobAdvertisement.getJobContent().getDisplayCompany()).isEqualTo(jobCenter);

        domainEventMockUtils.assertSingleDomainEventPublished(JobAdvertisementEvents.JOB_ADVERTISEMENT_APPROVED.getDomainEventType());
    }

}