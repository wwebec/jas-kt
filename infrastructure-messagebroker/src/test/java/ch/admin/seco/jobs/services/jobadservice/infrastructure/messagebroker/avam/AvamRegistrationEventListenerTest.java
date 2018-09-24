package ch.admin.seco.jobs.services.jobadservice.infrastructure.messagebroker.avam;

import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.fixture.JobContentTestFixture.testJobContent;
import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.fixture.PublicationFixture.testPublication;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisement;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisementId;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisementStatus;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.SourceSystem;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.events.JobAdvertisementCancelledEvent;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.fixture.OwnerFixture;

@RunWith(MockitoJUnitRunner.class)
public class AvamRegistrationEventListenerTest {

    private static JobAdvertisementId ID = new JobAdvertisementId("id");
    private JobAdvertisement JOBADVERTISEMENT = new JobAdvertisement.Builder()
            .setId(ID)
            .setStatus(JobAdvertisementStatus.CREATED)
            .setSourceSystem(SourceSystem.JOBROOM)
            .setJobContent(testJobContent(ID))
            .setOwner(OwnerFixture.of(ID)
                    .build())
            .setPublication(testPublication()
                    .setPublicDisplay(true)
                    .build())
            .setReportToAvam(true)
            .setReportingObligation(true)
            .build();

    @Mock
    private AvamTaskRepository avamTaskRepository;

    @InjectMocks
    private AvamRegistrationEventListener sut; //System Under Test

    @Test
    public void shouldHandleCancel() {
        // GIVEN
        JobAdvertisementCancelledEvent event = new JobAdvertisementCancelledEvent(JOBADVERTISEMENT, SourceSystem.JOBROOM);

        // WHEN
        sut.handle(event);

        // THEN
        verify(avamTaskRepository).save(any());
    }

    @Test
    public void shouldNotHandleCancel() {
        // GIVEN
        JobAdvertisementCancelledEvent event = new JobAdvertisementCancelledEvent(JOBADVERTISEMENT, SourceSystem.RAV);

        // WHEN
        sut.handle(event);

        // THEN
        verify(avamTaskRepository, never()).save(any());
    }

}
