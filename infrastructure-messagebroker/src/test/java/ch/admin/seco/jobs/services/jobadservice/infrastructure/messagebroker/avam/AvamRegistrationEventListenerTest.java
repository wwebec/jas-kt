package ch.admin.seco.jobs.services.jobadservice.infrastructure.messagebroker.avam;

import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisement;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisementId;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisementStatus;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.SourceSystem;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.events.JobAdvertisementCancelledEvent;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisementTestDataProvider.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class AvamRegistrationEventListenerTest {

    private static JobAdvertisementId ID = new JobAdvertisementId("id");
    private JobAdvertisement JOBADVERTISEMENT = new JobAdvertisement.Builder()
            .setId(ID)
            .setStatus(JobAdvertisementStatus.CREATED)
            .setSourceSystem(SourceSystem.JOBROOM)
            .setJobContent(createJobContent(ID))
            .setOwner(createOwner(ID))
            .setPublication(createPublication())
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