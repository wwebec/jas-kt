package ch.admin.seco.jobs.services.jobadservice.infrastructure.web.controller;

import static ch.admin.seco.jobs.services.jobadservice.application.security.Role.PRIVATE_EMPLOYMENT_AGENT;
import static ch.admin.seco.jobs.services.jobadservice.application.security.Role.PUBLIC_EMPLOYMENT_SERVICE;

import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.JobAdvertisementApplicationService;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.JobAdvertisementDto;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.create.CreateJobAdvertisementDto;
import ch.admin.seco.jobs.services.jobadservice.core.domain.AggregateNotFoundException;
import ch.admin.seco.jobs.services.jobadservice.core.domain.events.EventData;
import ch.admin.seco.jobs.services.jobadservice.core.domain.events.EventStore;
import ch.admin.seco.jobs.services.jobadservice.core.time.TimeMachine;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisement;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisementId;
import ch.admin.seco.jobs.services.jobadservice.infrastructure.web.controller.resources.CancellationResource;
import ch.admin.seco.jobs.services.jobadservice.infrastructure.web.controller.resources.PageResource;

@RestController
@RequestMapping("/api/jobAdvertisements")
public class JobAdvertisementRestController {

    private final JobAdvertisementApplicationService jobAdvertisementApplicationService;
    private final EventStore eventStore;

    public JobAdvertisementRestController(JobAdvertisementApplicationService jobAdvertisementApplicationService, EventStore eventStore) {
        this.jobAdvertisementApplicationService = jobAdvertisementApplicationService;
        this.eventStore = eventStore;
    }

    @PostMapping()
    public JobAdvertisementDto createFromWebform(@RequestBody CreateJobAdvertisementDto createJobAdvertisementDto) throws AggregateNotFoundException {
        JobAdvertisementId jobAdvertisementId = jobAdvertisementApplicationService.createFromWebForm(createJobAdvertisementDto);
        return jobAdvertisementApplicationService.getById(jobAdvertisementId);
    }

    @GetMapping
    public PageResource<JobAdvertisementDto> getAll(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "25") int size
    ) {
        return PageResource.of(jobAdvertisementApplicationService.findAllPaginated(PageRequest.of(page, size)));
    }

    @GetMapping("/{id}")
    public JobAdvertisementDto getOne(@PathVariable String id) throws AggregateNotFoundException {
        return jobAdvertisementApplicationService.getById(new JobAdvertisementId(id));
    }

    @GetMapping("/token/{accessToken}")
    public JobAdvertisementDto getOneByAccessToken(@PathVariable String accessToken) throws AggregateNotFoundException {
        return jobAdvertisementApplicationService.getByAccessToken(accessToken);
    }

    @GetMapping("/byStellennummerEgov/{stellennummerEgov}")
    public JobAdvertisementDto getOneByStellennummerEgov(@PathVariable String stellennummerEgov) throws AggregateNotFoundException {
        return jobAdvertisementApplicationService.findByStellennummerEgov(stellennummerEgov);
    }

    @GetMapping("/byStellennummerAvam/{stellennummerAvam}")
    public JobAdvertisementDto getOneByStellennummerAvam(@PathVariable String stellennummerAvam) throws AggregateNotFoundException {
        return jobAdvertisementApplicationService.findByStellennummerAvam(stellennummerAvam);
    }

	@PreAuthorize("hasAnyAuthority('ROLE_PRIVATE_EMPLOYMENT_AGENT','ROLE_PUBLIC_EMPLOYMENT_SERVICE')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PatchMapping("/{id}/cancel")
    public void cancel(@PathVariable String id, @RequestBody CancellationResource cancellation) {
        jobAdvertisementApplicationService.cancel(new JobAdvertisementId(id), TimeMachine.now().toLocalDate(), cancellation.getCode());
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PatchMapping(path = "/cancel/token/{token}")
    public void cancelByToken(@PathVariable String token, @RequestBody CancellationResource cancellation) {
        jobAdvertisementApplicationService.cancelByToken(token, TimeMachine.now().toLocalDate(), cancellation.getCode());
    }

    @GetMapping("/{id}/events")
    public PageResource<EventData> getEventsOfJobAdvertisement(@PathVariable String id) throws AggregateNotFoundException {
        return PageResource.of(eventStore.findByAggregateId(id, JobAdvertisement.class.getSimpleName(), 0, 100));
    }

}
