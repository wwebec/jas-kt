package ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement;

import ch.admin.seco.jobs.services.jobadservice.core.domain.events.DomainEventMockUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.spy;

public class JobAdvertisementFactoryTest {

    private DomainEventMockUtils domainEventMockUtils;
    private JobAdvertisementRepository jobAdvertisementRepository;
    private JobAdvertisementFactory jobAdvertisementFactory;

    @Before
    public void setUp() {
        domainEventMockUtils = new DomainEventMockUtils();
        jobAdvertisementRepository = spy(TestJobAdvertisementRepository.class);
        jobAdvertisementFactory = new JobAdvertisementFactory(jobAdvertisementRepository);
    }

    @After
    public void tearDown() {
        domainEventMockUtils.clearEvents();
    }

    @Test
    public void testCreateFromWebForm() {
        //Prepare

        //Execute
        JobAdvertisement jobAdvertisement = jobAdvertisementFactory.createFromWebForm(
                "title",
                "description",
                new JobAdvertisementUpdater.Builder(null).build()
        );

        //Validate
        assertThat(jobAdvertisement.getStatus()).isEqualTo(JobAdvertisementStatus.CREATED);
        assertThat(jobAdvertisement.getSourceSystem()).isEqualTo(SourceSystem.JOBROOM);

        JobAdvertisementEvent jobAdvertisementEvent = domainEventMockUtils.assertSingleDomainEventPublished(JobAdvertisementEvents.JOB_ADVERTISEMENT_CREATED.getDomainEventType());
        assertThat(jobAdvertisementEvent.getJobAdvertisementId()).isEqualTo(jobAdvertisement.getId());
    }

    @Test
    public void createFromApi() {
        //Prepare

        //Execute
        JobAdvertisement jobAdvertisement = jobAdvertisementFactory.createFromApi(
                "title",
                "description",
                new JobAdvertisementUpdater.Builder(null).build()
        );

        //Validate
        assertThat(jobAdvertisement.getStatus()).isEqualTo(JobAdvertisementStatus.CREATED);
        assertThat(jobAdvertisement.getSourceSystem()).isEqualTo(SourceSystem.API);

        JobAdvertisementEvent jobAdvertisementEvent = domainEventMockUtils.assertSingleDomainEventPublished(JobAdvertisementEvents.JOB_ADVERTISEMENT_CREATED.getDomainEventType());
        assertThat(jobAdvertisementEvent.getJobAdvertisementId()).isEqualTo(jobAdvertisement.getId());
    }

    static abstract class TestJobAdvertisementRepository implements JobAdvertisementRepository {

        @Override
        public JobAdvertisement save(JobAdvertisement jobAdvertisement) {
            return jobAdvertisement;
        }
    }

}