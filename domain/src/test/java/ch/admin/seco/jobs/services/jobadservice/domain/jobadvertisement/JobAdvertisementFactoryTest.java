package ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement;

import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.fixture.JobAdvertisementIdFixture.job01;
import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.fixture.JobContentTestFixture.testJobContent;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import org.springframework.jdbc.support.incrementer.DataFieldMaxValueIncrementer;

import ch.admin.seco.jobs.services.jobadservice.core.domain.events.AuditUser;
import ch.admin.seco.jobs.services.jobadservice.core.domain.events.DomainEventMockUtils;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.events.JobAdvertisementEvent;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.events.JobAdvertisementEvents;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.fixture.ContactFixture;

public class JobAdvertisementFactoryTest {

    private static final String TEST_STELLEN_NUMMER_EGOV = "1000000";

    private DomainEventMockUtils domainEventMockUtils;
    private JobAdvertisementFactory jobAdvertisementFactory;

    @Mock
    private JobAdvertisementRepository repository;

    @Before
    public void setUp() {
        domainEventMockUtils = new DomainEventMockUtils();
        JobAdvertisementRepository jobAdvertisementRepository = spy(TestJobAdvertisementRepository.class);
        DataFieldMaxValueIncrementer stellennummerEgovGenerator = spy(DataFieldMaxValueIncrementer.class);
        AccessTokenGenerator accessTokenGenerator = spy(AccessTokenGenerator.class);
        when(stellennummerEgovGenerator.nextStringValue()).thenReturn(TEST_STELLEN_NUMMER_EGOV);
        jobAdvertisementFactory = new JobAdvertisementFactory(jobAdvertisementRepository, accessTokenGenerator, stellennummerEgovGenerator);
    }

    @After
    public void tearDown() {
        domainEventMockUtils.clearEvents();
    }

    @Test
    public void testCreateFromWebForm() {
        //given
        JobAdvertisementCreator creator = testJobAdvertisementCreator();

        //when
        JobAdvertisement jobAdvertisement = jobAdvertisementFactory.createFromWebForm(creator);

        //then
        assertThat(jobAdvertisement.getStatus()).isEqualTo(JobAdvertisementStatus.CREATED);
        assertThat(jobAdvertisement.getSourceSystem()).isEqualTo(SourceSystem.JOBROOM);
        assertThat(jobAdvertisement.getStellennummerEgov()).isEqualTo(TEST_STELLEN_NUMMER_EGOV);

        JobAdvertisementEvent jobAdvertisementEvent = domainEventMockUtils.assertSingleDomainEventPublished(JobAdvertisementEvents.JOB_ADVERTISEMENT_CREATED.getDomainEventType());
        assertThat(jobAdvertisementEvent.getAggregateId()).isEqualTo(jobAdvertisement.getId());
    }

    @Test
    public void createFromApi() {
        //Prepare
        JobAdvertisementCreator creator = testJobAdvertisementCreator();

        //Execute
        JobAdvertisement jobAdvertisement = jobAdvertisementFactory.createFromApi(creator);

        //Validate
        assertThat(jobAdvertisement.getStatus()).isEqualTo(JobAdvertisementStatus.CREATED);
        assertThat(jobAdvertisement.getSourceSystem()).isEqualTo(SourceSystem.API);
        assertThat(jobAdvertisement.getStellennummerEgov()).isEqualTo(TEST_STELLEN_NUMMER_EGOV);

        JobAdvertisementEvent jobAdvertisementEvent = domainEventMockUtils.assertSingleDomainEventPublished(JobAdvertisementEvents.JOB_ADVERTISEMENT_CREATED.getDomainEventType());
        assertThat(jobAdvertisementEvent.getAggregateId()).isEqualTo(jobAdvertisement.getId());
    }

    public static JobAdvertisementCreator testJobAdvertisementCreator() {
        return new JobAdvertisementCreator.Builder(createAuditUser())
                .setContact(ContactFixture.of(job01.id()).build())
                .setJobContent(testJobContent(job01.id()))
                .setPublication(new Publication.Builder().build())
                .build();
    }

    @SuppressWarnings("unchecked")
    static abstract class TestJobAdvertisementRepository implements JobAdvertisementRepository {

        @Override
        public JobAdvertisement save(JobAdvertisement jobAdvertisement) {
            return jobAdvertisement;
        }
    }

    private static AuditUser createAuditUser() {
        return new AuditUser("user-1","extern-1", "company-1", "My", "User", "my.user@example.org");
    }
}
