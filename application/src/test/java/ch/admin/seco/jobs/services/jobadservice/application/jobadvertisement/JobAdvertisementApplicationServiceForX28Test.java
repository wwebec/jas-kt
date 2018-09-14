package ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement;

import ch.admin.seco.jobs.services.jobadservice.application.JobCenterService;
import ch.admin.seco.jobs.services.jobadservice.application.LocationService;
import ch.admin.seco.jobs.services.jobadservice.application.ProfessionService;
import ch.admin.seco.jobs.services.jobadservice.application.ReportingObligationService;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.CompanyDto;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.EmploymentDto;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.LanguageSkillDto;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.OccupationDto;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.create.CreateJobAdvertisementFromX28Dto;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.create.CreateLocationDto;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.update.UpdateJobAdvertisementFromX28Dto;
import ch.admin.seco.jobs.services.jobadservice.core.domain.events.DomainEventMockUtils;
import ch.admin.seco.jobs.services.jobadservice.core.time.TimeMachine;
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

import static ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.JobAdvertisementApplicationService.COUNTRY_ISO_CODE_SWITZERLAND;
import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisementTestDataProvider.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@RunWith(SpringRunner.class)
@DataJpaTest
public class JobAdvertisementApplicationServiceForX28Test {

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
        when(egovNumberGenerator.nextStringValue()).thenReturn(TEST_STELLEN_NUMMER_EGOV);
    }

    @After
    public void tearDown() {
        domainEventMockUtils.clearEvents();
    }

    @Test
    public void createFromX28() {
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
        CreateJobAdvertisementFromX28Dto createJobAdvertisementDto = new CreateJobAdvertisementFromX28Dto(
                null,
                null,
                "title",
                "description",
                null,
                "fingerprint",
                "url",
                null,
                null,
                new EmploymentDto(LocalDate.of(2018, 1, 1), LocalDate.of(2018, 12, 31), false, false, false, 80, 100, null),
                CompanyDto.toDto(company),
                new CreateLocationDto("remarks", "city", "postalCode", "CH"),
                Collections.singletonList(new OccupationDto("avamCode", WorkExperience.MORE_THAN_1_YEAR, "educationCode")),
                "1,2",
                Collections.singletonList(new LanguageSkillDto("de", LanguageLevel.PROFICIENT, LanguageLevel.PROFICIENT)),
                TimeMachine.now().toLocalDate(),
                null,
                false
        );

        //Execute
        JobAdvertisementId jobAdvertisementId = sut.createFromX28(createJobAdvertisementDto);

        //Validate
        JobAdvertisement jobAdvertisement = jobAdvertisementRepository.getOne(jobAdvertisementId);
        assertThat(jobAdvertisement).isNotNull();
        assertThat(jobAdvertisement.getStatus()).isEqualTo(JobAdvertisementStatus.PUBLISHED_PUBLIC);
        assertThat(jobAdvertisement.getSourceSystem()).isEqualTo(SourceSystem.EXTERN);
        assertThat(jobAdvertisement.getStellennummerEgov()).isNull();
        assertThat(jobAdvertisement.getPublication().isEuresAnonymous()).isFalse();
        assertThat(jobAdvertisement.getPublication().isCompanyAnonymous()).isFalse();

        assertThat(jobAdvertisement.isReportingObligation()).isFalse();
        assertThat(jobAdvertisement.getJobContent().getDisplayCompany()).isEqualTo(company);

        domainEventMockUtils.assertSingleDomainEventPublished(JobAdvertisementEvents.JOB_ADVERTISEMENT_PUBLISH_PUBLIC.getDomainEventType());
    }

    @Test
    public void createFromX28WithEmptyCountry() {
        //Prepare
        CreateJobAdvertisementFromX28Dto createJobAdvertisementDto = new CreateJobAdvertisementFromX28Dto(
                null,
                null,
                "title",
                "description",
                null,
                "fingerprint",
                "url",
                null,
                null,
                new EmploymentDto(LocalDate.of(2018, 1, 1), LocalDate.of(2018, 12, 31), false, false, false, 80, 100, null),
                new CompanyDto("name", "street", "houseNumber", "postalCode", "city", "CH", null, null, null, "phone", "email", "website", false),
                new CreateLocationDto(null, "city", "postalCode", null),
                Collections.singletonList(new OccupationDto("avamCode", WorkExperience.MORE_THAN_1_YEAR, "educationCode")),
                "1,2",
                Collections.singletonList(new LanguageSkillDto("de", LanguageLevel.PROFICIENT, LanguageLevel.PROFICIENT)),
                TimeMachine.now().toLocalDate(),
                null,
                false
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
        jobAdvertisementRepository.save(new JobAdvertisement.Builder()
                .setId(JOB_ADVERTISEMENT_ID_01)
                .setOwner(createOwner(JOB_ADVERTISEMENT_ID_01))
                .setContact(createContact(JOB_ADVERTISEMENT_ID_01))
                .setJobContent(createJobContent(JOB_ADVERTISEMENT_ID_01))
                .setPublication(new Publication.Builder().build())
                .setSourceSystem(SourceSystem.JOBROOM)
                .setStellennummerEgov(JOB_ADVERTISEMENT_ID_01.getValue())
                .setStellennummerAvam(STELLENNUMMER_AVAM)
                .setStatus(JobAdvertisementStatus.CREATED)
                .build());
        UpdateJobAdvertisementFromX28Dto updateJobAdvertisementFromX28Dto = new UpdateJobAdvertisementFromX28Dto(
                JOB_ADVERTISEMENT_ID_01.getValue(),
                "fingerprint",
                "x28"
        );

        // when
        sut.updateFromX28(updateJobAdvertisementFromX28Dto);

        // then
        JobAdvertisement jobAdvertisement = jobAdvertisementRepository.getOne(JOB_ADVERTISEMENT_ID_01);
        assertThat(jobAdvertisement.getFingerprint()).isEqualTo("fingerprint");
        assertThat(jobAdvertisement.getJobContent().getX28OccupationCodes()).isEqualTo("x28");
        domainEventMockUtils.assertSingleDomainEventPublished(JobAdvertisementEvents.JOB_ADVERTISEMENT_UPDATED.getDomainEventType());
    }

}