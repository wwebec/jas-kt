package ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement;

import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.SourceSystem.JOBROOM;
import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.SourceSystem.RAV;
import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.fixture.JobAdvertisementTestFixture.testJobAdvertisement;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.verification.VerificationMode;

import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisement;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisementRepository;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.SourceSystem;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.events.JobAdvertisementCreatedEvent;

@RunWith(MockitoJUnitRunner.class)
public class JobAdvertisementEventListenerTest {

    @InjectMocks
    private JobAdvertisementEventListener testedObject;

    @Mock
    private JobAdvertisementRepository jobAdvertisementRepository;

    @Mock
    private JobAdvertisementApplicationService jobAdvertisementApplicationService;

    @Test
    public void shouldInspectJob() {
        verifyRefiningAndInspectOfJobAdvertisement(JOBROOM, true, never(), times(1));
    }

    @Test
    public void shouldRefineJob() {
        verifyRefiningAndInspectOfJobAdvertisement(RAV, false, times(1), never());
    }

    private void verifyRefiningAndInspectOfJobAdvertisement(SourceSystem sourceSystem, boolean reportToAvam, VerificationMode refiningVerificationMode, VerificationMode inspectVerificationMode) {
        // given
        JobAdvertisement jobAdvertisement = testJobAdvertisement(sourceSystem, reportToAvam);
        JobAdvertisementCreatedEvent event = new JobAdvertisementCreatedEvent(jobAdvertisement);
        given(jobAdvertisementRepository.findById(jobAdvertisement.getId())).willReturn(Optional.of(jobAdvertisement));

        // when
        testedObject.onCreated(event);

        //then
        verify(jobAdvertisementApplicationService, refiningVerificationMode).refining(jobAdvertisement.getId());
        verify(jobAdvertisementApplicationService, inspectVerificationMode).inspect(jobAdvertisement.getId());
    }
}