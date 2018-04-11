package ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement;

import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisementTestDataProvider.createJobContent;
import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisementTestDataProvider.createOwner;
import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisementTestDataProvider.createPublication;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisement;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisementId;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisementRepository;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisementStatus;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.SourceSystem;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.events.JobAdvertisementCreatedEvent;

@RunWith(MockitoJUnitRunner.class)
public class JobAdvertisementEventListenerTest {

	private static final JobAdvertisementId ID = new JobAdvertisementId("id");

	@InjectMocks
	private JobAdvertisementEventListener testedObject;

	@Mock
	private JobAdvertisementRepository jobAdvertisementRepository;

	@Mock
	private JobAdvertisementApplicationService jobAdvertisementApplicationService;

	@Test
	public void shouldInspectJob() {
		// GIVEN
		JobAdvertisementId id = new JobAdvertisementId("id");
		JobAdvertisement jobAdvertisement = new JobAdvertisement.Builder()
				.setId(id)
				.setStatus(JobAdvertisementStatus.CREATED)
				.setSourceSystem(SourceSystem.JOBROOM)
				.setJobContent(createJobContent(id))
				.setOwner(createOwner(id))
				.setPublication(createPublication())
				.setReportToAvam(true)
				.setReportingObligation(true)
				.build();
		JobAdvertisementCreatedEvent event = new JobAdvertisementCreatedEvent(jobAdvertisement);
		given(jobAdvertisementRepository.findById(id)).willReturn(Optional.of(jobAdvertisement));

		// WHEN
		testedObject.onCreated(event);

		// THEN
		verify(jobAdvertisementApplicationService).inspect(id);
		verify(jobAdvertisementApplicationService, never()).refining(id);
	}

	@Test
	public void shouldRefineJob() {
		// GIVEN
		JobAdvertisement jobAdvertisement = new JobAdvertisement.Builder()
				.setId(ID)
				.setStatus(JobAdvertisementStatus.CREATED)
				.setSourceSystem(SourceSystem.RAV)
				.setJobContent(createJobContent(ID))
				.setOwner(createOwner(ID))
				.setPublication(createPublication())
				.setReportToAvam(false)
				.setReportingObligation(false)
				.build();
		JobAdvertisementCreatedEvent event = new JobAdvertisementCreatedEvent(jobAdvertisement);
		given(jobAdvertisementRepository.findById(ID)).willReturn(Optional.of(jobAdvertisement));

		// WHEN
		testedObject.onCreated(event);

		// THEN
		verify(jobAdvertisementApplicationService).refining(ID);
		verify(jobAdvertisementApplicationService, never()).inspect(ID);
	}
}