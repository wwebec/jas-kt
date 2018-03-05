package ch.admin.seco.jobs.services.jobadservice.infrastructure.web.controller;

import javax.validation.Valid;

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
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.api.CreateJobAdvertisementApiDto;
import ch.admin.seco.jobs.services.jobadservice.core.domain.AggregateNotFoundException;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisementId;

@RestController
@RequestMapping("/api/jobAdvertisement/api")
public class JobAdvertisementApiRestController {

	private final JobAdvertisementApplicationService jobAdvertisementApplicationService;

	@Autowired
	public JobAdvertisementApiRestController(JobAdvertisementApplicationService jobAdvertisementApplicationService) {
		this.jobAdvertisementApplicationService = jobAdvertisementApplicationService;
	}

	@PostMapping
	public JobAdvertisementDto createFromApi(@RequestBody @Valid CreateJobAdvertisementApiDto createJobAdvertisementApiDto) throws AggregateNotFoundException {
		JobAdvertisementId jobAdvertisementId = jobAdvertisementApplicationService.createFromApi(createJobAdvertisementApiDto);
		return jobAdvertisementApplicationService.findById(jobAdvertisementId);
	}

	@PostMapping(path = "/{id}/cancel")
	public ResponseEntity<?> cancelFromApi(@PathVariable Integer id) {
		// TODO: implement
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}
