package ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement;

import ch.admin.seco.jobs.services.jobadservice.application.LocalityService;
import ch.admin.seco.jobs.services.jobadservice.application.ProfessionService;
import ch.admin.seco.jobs.services.jobadservice.application.RavRegistrationService;
import ch.admin.seco.jobs.services.jobadservice.application.ReportingObligationService;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.*;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.api.CreateJobAdvertisementApiDto;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.api.JobDto;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.api.LocationDto;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.api.PostboxDto;
import ch.admin.seco.jobs.services.jobadservice.core.domain.events.DomainEventMockUtils;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@RunWith(SpringRunner.class)
@DataJpaTest
public class JobAdvertisementApplicationServiceTest {

    private DomainEventMockUtils domainEventMockUtils;

    @MockBean
    private RavRegistrationService ravRegistrationService;

    @MockBean
    private ReportingObligationService reportingObligationService;

    @MockBean
    private LocalityService localityService;

    @MockBean
    private ProfessionService professionService;

    @Autowired
    private JobAdvertisementRepository jobAdvertisementRepository;

    @Autowired
    private JobAdvertisementApplicationService jobAdvertisementApplicationService;

    @Before
    public void setUp() {
        domainEventMockUtils = new DomainEventMockUtils();

        when(localityService.enrichCodes(any())).thenReturn(new Locality("remarks", "ctiy", "zipCode", null, null, "BE", "CH", null));
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
                "title",
                "description",
                new EmploymentDto(LocalDate.of(2018, 1, 1), LocalDate.of(2018, 12, 31), 365, true, false, 80, 100),
                "drivingLicenseLevel",
                new ApplyChannelDto("mailAddress", "emailAddress", "phoneNumber", "formUrl", "additionalInfo"),
                new CompanyDto("name", "stree", "houseNumber", "zipCode", "city", "CH", null, null, null, "phone", "email", "website"),
                new ContactDto("MR", "firstName", "lastName", "phone", "email"),
                new LocalityDto("remarks", "ctiy", "zipCode", null, null, "BE", "CH", null),
                new OccupationDto("avamCode", WorkExperience.MORE_THAN_1_YEAR, "educationCode"),
                Collections.singletonList(new LanguageSkillDto("de", LanguageLevel.PROFICIENT, LanguageLevel.PROFICIENT))
        );

        //Execute
        JobAdvertisementId jobAdvertisementId = jobAdvertisementApplicationService.createFromWebForm(createJobAdvertisementWebFormDto);

        //Validate
        JobAdvertisement jobAdvertisement = jobAdvertisementRepository.getOne(jobAdvertisementId);
        assertThat(jobAdvertisement).isNotNull();
        assertThat(jobAdvertisement.getStatus()).isEqualTo(JobAdvertisementStatus.CREATED);
        assertThat(jobAdvertisement.getSourceSystem()).isEqualTo(SourceSystem.JOBROOM);

        domainEventMockUtils.assertSingleDomainEventPublished(JobAdvertisementEvents.JOB_ADVERTISEMENT_CREATED.getDomainEventType());
    }

    @Test
    public void createFromApi() {
        //Prepare
        CreateJobAdvertisementApiDto jobAdvertisementApiDto = new CreateJobAdvertisementApiDto(
                LocalDate.of(2018, 1, 1),
                LocalDate.of(2018, 12, 31),
                "ref",
                "http://url",
                "http://url",
                new JobDto("title", "descriptioin", 10, 90, LocalDate.of(2018, 1, 1),
                        LocalDate.of(2018, 12, 31), 30, true, true,
                        new LocationDto("remarks", "ctiy", "zipCode", "BE", "details"), Collections.emptyList()),
                new ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.api.CompanyDto(
                        "name", "CH", "street", "12", "Bern", "2222", "number", "email", "website",
                        new PostboxDto("sdf", "Bern", "code")),
                new ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.api.ContactDto(
                        ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.api.ContactDto.Title.mister,
                        "first", "last", "number", "email"),
                new OccupationDto("avamCode", WorkExperience.MORE_THAN_1_YEAR, "educationCode")
        );

        //Execute
        JobAdvertisementId jobAdvertisementId = jobAdvertisementApplicationService.createFromApi(jobAdvertisementApiDto);

        //Validate
        JobAdvertisement jobAdvertisement = jobAdvertisementRepository.getOne(jobAdvertisementId);
        assertThat(jobAdvertisement).isNotNull();
        assertThat(jobAdvertisement.getStatus()).isEqualTo(JobAdvertisementStatus.CREATED);
        assertThat(jobAdvertisement.getSourceSystem()).isEqualTo(SourceSystem.API);

        domainEventMockUtils.assertSingleDomainEventPublished(JobAdvertisementEvents.JOB_ADVERTISEMENT_CREATED.getDomainEventType());
    }

}