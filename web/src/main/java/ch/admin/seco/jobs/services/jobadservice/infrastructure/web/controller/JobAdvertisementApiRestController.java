package ch.admin.seco.jobs.services.jobadservice.infrastructure.web.controller;

import javax.validation.Valid;

import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.create.legacy.LegacyJobAdvertisementDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.JobAdvertisementApplicationService;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.JobAdvertisementDto;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.create.CreateJobAdvertisementDto;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.create.legacy.LegacyToJobAdvertisementConverter;
import ch.admin.seco.jobs.services.jobadservice.core.domain.AggregateNotFoundException;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisementId;

@RestController
@RequestMapping("/api/jobAdvertisement")
public class JobAdvertisementApiRestController {

	private final JobAdvertisementApplicationService jobAdvertisementApplicationService;

	@Autowired
	public JobAdvertisementApiRestController(JobAdvertisementApplicationService jobAdvertisementApplicationService) {
		this.jobAdvertisementApplicationService = jobAdvertisementApplicationService;
	}

	@PostMapping(path = "/api")
	public JobAdvertisementDto createFromApi(@RequestBody @Valid CreateJobAdvertisementDto createJobAdvertisementDto) throws AggregateNotFoundException {
		JobAdvertisementId jobAdvertisementId = jobAdvertisementApplicationService.createFromApi(createJobAdvertisementDto);
		return jobAdvertisementApplicationService.findById(jobAdvertisementId);
	}

	@PostMapping(path = "/api-legacy")
	public JobAdvertisementDto createFromLegacyApi(@RequestBody @Valid LegacyJobAdvertisementDto legacyJobAdvertisementDto) throws AggregateNotFoundException {
		CreateJobAdvertisementDto createJobAdvertisementDto = LegacyToJobAdvertisementConverter.convert(legacyJobAdvertisementDto);
		return createFromApi(createJobAdvertisementDto);
	}

	@PostMapping(path = "/api/{id}/cancel")
	public ResponseEntity<?> cancelFromApi(@PathVariable Integer id) {
		// TODO: implement
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}
