package ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement;

import ch.admin.seco.jobs.services.jobadservice.core.domain.events.DomainEventMockUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.support.incrementer.DataFieldMaxValueIncrementer;

import java.util.Locale;

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
        when(stellennummerEgovGenerator.nextStringValue()).thenReturn(TEST_STELLEN_NUMMER_EGOV);

        jobAdvertisementFactory = new JobAdvertisementFactory(jobAdvertisementRepository, stellennummerEgovGenerator);
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
                Locale.GERMAN,
                "title",
                "description",
                new JobAdvertisementUpdater.Builder(null).build()
        );

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

        //Execute
        JobAdvertisement jobAdvertisement = jobAdvertisementFactory.createFromApi(
                Locale.GERMAN,
                "title",
                "description",
                new JobAdvertisementUpdater.Builder(null).build(),
                false
        );

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

}
