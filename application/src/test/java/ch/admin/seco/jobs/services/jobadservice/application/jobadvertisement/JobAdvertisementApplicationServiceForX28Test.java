package ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement;

import static ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.JobAdvertisementApplicationService.COUNTRY_ISO_CODE_SWITZERLAND;
import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisementTestDataProvider.JOB_ADVERTISEMENT_ID_01;
import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisementTestDataProvider.createContact;
import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisementTestDataProvider.createJobContent;
import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisementTestDataProvider.createOwner;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Collections;

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
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.EmploymentDto;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.update.UpdateJobAdvertisementFromX28Dto;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.x28.CreateJobAdvertisementFromX28Dto;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.x28.X28CompanyDto;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.x28.X28LanguageSkillDto;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.x28.X28LocationDto;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.x28.X28OccupationDto;
import ch.admin.seco.jobs.services.jobadservice.core.domain.events.DomainEventMockUtils;
import ch.admin.seco.jobs.services.jobadservice.core.time.TimeMachine;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisement;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisementId;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisementRepository;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisementStatus;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.LanguageLevel;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.Location;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.Publication;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.SourceSystem;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.WorkExperience;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.events.JobAdvertisementEvents;

@SpringBootTest
@RunWith(SpringRunner.class)
@Transactional
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
        X28CompanyDto x28CompanyDto = new X28CompanyDto()
                .setName("name")
                .setStreet("street")
                .setHouseNumber("houseNumber")
                .setPostalCode("postalCode")
                .setCity("city")
                .setCountryIsoCode("CH")
                .setPhone("phone")
                .setEmail("email")
                .setWebsite("website");

        CreateJobAdvertisementFromX28Dto createJobAdvertisementDto = new CreateJobAdvertisementFromX28Dto()
                .setStellennummerEgov(null)
                .setStellennummerAvam(null)
                .setTitle("title")
                .setDescription("description")
                .setNumberOfJobs(null)
                .setFingerprint("fingerprint")
                .setExternalUrl("url")
                .setJobCenterCode(null)
                .setContact(null)
                .setEmployment(new EmploymentDto(LocalDate.of(2018, 1, 1), LocalDate.of(2018, 12, 31), false, false, false, 80, 100, null))
                .setCompany(x28CompanyDto)
                .setLocation(new X28LocationDto("remarks", "city", "postalCode", "CH"))
                .setOccupations(Collections.singletonList(new X28OccupationDto("avamCode", WorkExperience.MORE_THAN_1_YEAR, "educationCode")))
                .setProfessionCodes("1,2")
                .setLanguageSkills(Collections.singletonList(new X28LanguageSkillDto("de", LanguageLevel.PROFICIENT, LanguageLevel.PROFICIENT)))
                .setPublicationStartDate(TimeMachine.now().toLocalDate())
                .setPublicationEndDate(null)
                .setCompanyAnonymous(false);

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
        assertThat(jobAdvertisement.getJobContent().getDisplayCompany()).isNotNull();
        assertThat(jobAdvertisement.getJobContent().getDisplayCompany().getName()).isEqualTo(x28CompanyDto.getName());
        assertThat(jobAdvertisement.getJobContent().getDisplayCompany().getCity()).isEqualTo(x28CompanyDto.getCity());
        assertThat(jobAdvertisement.getJobContent().getDisplayCompany().getStreet()).isEqualTo(x28CompanyDto.getStreet());

        domainEventMockUtils.assertSingleDomainEventPublished(JobAdvertisementEvents.JOB_ADVERTISEMENT_PUBLISH_PUBLIC.getDomainEventType());
    }

    @Test
    public void createFromX28WithEmptyCountry() {
        //Prepare
        CreateJobAdvertisementFromX28Dto createJobAdvertisementDto = new CreateJobAdvertisementFromX28Dto()
                .setStellennummerEgov(null)
                .setStellennummerAvam(null)
                .setTitle("title")
                .setDescription("description")
                .setNumberOfJobs(null)
                .setFingerprint("fingerprint")
                .setExternalUrl("url")
                .setJobCenterCode(null)
                .setContact(null)
                .setEmployment(new EmploymentDto(LocalDate.of(2018, 1, 1), LocalDate.of(2018, 12, 31), false, false, false, 80, 100, null))
                .setCompany(new X28CompanyDto("name", "street", "houseNumber", "postalCode", "city", "CH", null, null, null, "phone", "email", "website", false))
                .setLocation(new X28LocationDto(null, "city", "postalCode", null))
                .setOccupations(Collections.singletonList(new X28OccupationDto("avamCode", WorkExperience.MORE_THAN_1_YEAR, "educationCode")))
                .setProfessionCodes("1,2")
                .setLanguageSkills(Collections.singletonList(new X28LanguageSkillDto("de", LanguageLevel.PROFICIENT, LanguageLevel.PROFICIENT)))
                .setPublicationStartDate(TimeMachine.now().toLocalDate())
                .setPublicationEndDate(null)
                .setCompanyAnonymous(false);

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