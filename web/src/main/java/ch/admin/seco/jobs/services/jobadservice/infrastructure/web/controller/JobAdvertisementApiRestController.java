package ch.admin.seco.jobs.services.jobadservice.infrastructure.web.controller;

import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.JobAdvertisementApplicationService;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.JobAdvertisementDto;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.create.CreateJobAdvertisementDto;
import ch.admin.seco.jobs.services.jobadservice.core.domain.AggregateNotFoundException;
import ch.admin.seco.jobs.services.jobadservice.core.time.TimeMachine;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisementId;
import ch.admin.seco.jobs.services.jobadservice.infrastructure.web.controller.resources.CancellationResource;
import ch.admin.seco.jobs.services.jobadservice.infrastructure.web.controller.resources.PageResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/jobAdvertisements/api/v1")
public class JobAdvertisementApiRestController {

    private final JobAdvertisementApplicationService jobAdvertisementApplicationService;

    @Autowired
    public JobAdvertisementApiRestController(JobAdvertisementApplicationService jobAdvertisementApplicationService) {
        this.jobAdvertisementApplicationService = jobAdvertisementApplicationService;
    }

    @PostMapping
    public JobAdvertisementDto createFromApi(@RequestBody @Valid CreateJobAdvertisementDto createJobAdvertisementDto) throws AggregateNotFoundException {
        JobAdvertisementId jobAdvertisementId = jobAdvertisementApplicationService.createFromApi(createJobAdvertisementDto);
        return jobAdvertisementApplicationService.getById(jobAdvertisementId);
    }

    @GetMapping(params = {"page", "size"})
    public PageResource<JobAdvertisementDto> getJobAdvertisements(@RequestParam(name = "page", defaultValue = "0") int page, @RequestParam(name = "size", defaultValue = "25") int size) {
        return PageResource.of(jobAdvertisementApplicationService.findAllPaginated(PageRequest.of(page, size)));
    }

    @GetMapping(path = "/{id}")
    public JobAdvertisementDto getJobAdvertisement(@PathVariable String id) {
        return jobAdvertisementApplicationService.getById(new JobAdvertisementId(id));
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PatchMapping(path = "/{id}/cancel")
    public void cancel(@PathVariable String id, @RequestBody CancellationResource cancellation) {
        jobAdvertisementApplicationService.cancel(new JobAdvertisementId(id), TimeMachine.now().toLocalDate(), cancellation.getCode());
    }

}
