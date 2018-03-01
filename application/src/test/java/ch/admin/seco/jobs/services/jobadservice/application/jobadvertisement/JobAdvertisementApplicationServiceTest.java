package ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement;

import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.*;
import ch.admin.seco.jobs.services.jobadservice.core.domain.events.DomainEventMockUtils;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@RunWith(SpringRunner.class)
@DataJpaTest
public class JobAdvertisementApplicationServiceTest {

    private DomainEventMockUtils domainEventMockUtils;

    @Autowired
    private JobAdvertisementRepository jobAdvertisementRepository;

    @Autowired
    private JobAdvertisementApplicationService jobAdvertisementApplicationService;

    @Before
    public void setUp() {
        domainEventMockUtils = new DomainEventMockUtils();
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
                LocalDate.of(2018, 1, 1),
                LocalDate.of(2018, 12, 31),
                365,
                true,
                false,
                80,
                100,
                "drivingLicenseLevel",
                new ApplyChannelDto("mailAddress", "emailAddress", "phoneNumber", "formUrl", "additionalInfo"),
                new CompanyDto("name", "stree", "houseNumber", "zipCode", "city", "CH", null, null, null, "phone", "email", "website"),
                new ContactDto("MR", "firstName", "lastName", "phone", "email"),
                Collections.singletonList(new LocalityDto("remarks", "ctiy", "zipCode", null, null, "BE", "CH", null)),
                new OccupationDto("prefessionId", WorkExperience.MORE_THAN_1_YEAR),
                null,
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

}