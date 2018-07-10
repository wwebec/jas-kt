package ch.admin.seco.jobs.services.jobadservice.infrastructure.web.controller;

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
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/byFingerprint/{fingerprint}")
    public JobAdvertisementDto getOneByFingerprint(@PathVariable String fingerprint) {
        return jobAdvertisementApplicationService.findByFingerprint(fingerprint);
    }

    @PatchMapping("/{id}/cancel")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("@jobAdvertisementAuthorizationService.canCancel(#id, #token)")
    public void cancel(@PathVariable String id, @RequestParam(required = false) String token, @RequestBody CancellationResource cancellation) {
        jobAdvertisementApplicationService.cancel(new JobAdvertisementId(id), TimeMachine.now().toLocalDate(), cancellation.getCode(), token);
    }

    @GetMapping("/{id}/events")
    public PageResource<EventData> getEventsOfJobAdvertisement(@PathVariable String id) throws AggregateNotFoundException {
        return PageResource.of(eventStore.findByAggregateId(id, JobAdvertisement.class.getSimpleName(), 0, 100));
    }

}
