package ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement;

import ch.admin.seco.jobs.services.jobadservice.core.domain.events.AuditUser;
import ch.admin.seco.jobs.services.jobadservice.core.domain.events.DomainEventMockUtils;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.events.JobAdvertisementEvent;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.events.JobAdvertisementEvents;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.support.incrementer.DataFieldMaxValueIncrementer;

import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisementTestDataProvider.JOB_ADVERTISEMENT_ID_01;
import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisementTestDataProvider.createContact;
import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisementTestDataProvider.createJobContent;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

public class JobAdvertisementFactoryTest {

    private static final String TEST_STELLEN_NUMMER_EGOV = "1000000";

    private DomainEventMockUtils domainEventMockUtils;
    private JobAdvertisementFactory jobAdvertisementFactory;

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
        //Prepare
        JobAdvertisementCreator creator = new JobAdvertisementCreator.Builder(createAuditUser())
                .setContact(createContact(JOB_ADVERTISEMENT_ID_01))
                .setJobContent(createJobContent(JOB_ADVERTISEMENT_ID_01))
                .setPublication(new Publication.Builder().build())
                .build();

        //Execute
        JobAdvertisement jobAdvertisement = jobAdvertisementFactory.createFromWebForm(creator);

        //Validate
        assertThat(jobAdvertisement.getStatus()).isEqualTo(JobAdvertisementStatus.CREATED);
        assertThat(jobAdvertisement.getSourceSystem()).isEqualTo(SourceSystem.JOBROOM);
        assertThat(jobAdvertisement.getStellennummerEgov()).isEqualTo(TEST_STELLEN_NUMMER_EGOV);

        JobAdvertisementEvent jobAdvertisementEvent = domainEventMockUtils.assertSingleDomainEventPublished(JobAdvertisementEvents.JOB_ADVERTISEMENT_CREATED.getDomainEventType());
        assertThat(jobAdvertisementEvent.getAggregateId()).isEqualTo(jobAdvertisement.getId());
    }

    @Test
    public void createFromApi() {
        //Prepare
        JobAdvertisementCreator creator = new JobAdvertisementCreator.Builder(createAuditUser())
                .setContact(createContact(JOB_ADVERTISEMENT_ID_01))
                .setJobContent(createJobContent(JOB_ADVERTISEMENT_ID_01))
                .setPublication(new Publication.Builder().build())
                .build();

        //Execute
        JobAdvertisement jobAdvertisement = jobAdvertisementFactory.createFromApi(creator);

        //Validate
        assertThat(jobAdvertisement.getStatus()).isEqualTo(JobAdvertisementStatus.CREATED);
        assertThat(jobAdvertisement.getSourceSystem()).isEqualTo(SourceSystem.API);
        assertThat(jobAdvertisement.getStellennummerEgov()).isEqualTo(TEST_STELLEN_NUMMER_EGOV);

        JobAdvertisementEvent jobAdvertisementEvent = domainEventMockUtils.assertSingleDomainEventPublished(JobAdvertisementEvents.JOB_ADVERTISEMENT_CREATED.getDomainEventType());
        assertThat(jobAdvertisementEvent.getAggregateId()).isEqualTo(jobAdvertisement.getId());
    }

    @SuppressWarnings("unchecked")
    static abstract class TestJobAdvertisementRepository implements JobAdvertisementRepository {

        @Override
        public JobAdvertisement save(JobAdvertisement jobAdvertisement) {
            return jobAdvertisement;
        }
    }

    private AuditUser createAuditUser() {
        return new AuditUser("extern-1", "My", "User", "my.user@example.org");
    }
}
