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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/jobAdvertisement")
public class JobAdvertisementRestController {

    private final JobAdvertisementApplicationService jobAdvertisementApplicationService;
    private final EventStore eventStore;

    @Autowired
    public JobAdvertisementRestController(JobAdvertisementApplicationService jobAdvertisementApplicationService, EventStore eventStore) {
        this.jobAdvertisementApplicationService = jobAdvertisementApplicationService;
        this.eventStore = eventStore;
    }

    @GetMapping(path = "/testauth")
    public void testAuth() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            System.out.println(authentication.getDetails());
            System.out.println(authentication.getPrincipal());
            System.out.println(authentication.getPrincipal());
        }
    }

    @PostMapping()
    public JobAdvertisementDto createFromWebform(@RequestBody CreateJobAdvertisementDto createJobAdvertisementDto) throws AggregateNotFoundException {
        JobAdvertisementId jobAdvertisementId = jobAdvertisementApplicationService.createFromWebForm(createJobAdvertisementDto);
        return jobAdvertisementApplicationService.getById(jobAdvertisementId);
    }

    @GetMapping(params = {"page", "size"})
    public PageResource<JobAdvertisementDto> getAll(@RequestParam(name = "page", defaultValue = "0") int page, @RequestParam(name = "size", defaultValue = "25") int size) {
        return PageResource.of(jobAdvertisementApplicationService.findAllPaginated(PageRequest.of(page, size)));
    }

    @GetMapping("/{id}")
    public JobAdvertisementDto getOne(@PathVariable String id) throws AggregateNotFoundException {
        return jobAdvertisementApplicationService.getById(new JobAdvertisementId(id));
    }

    @GetMapping(params = {"token"})
    public JobAdvertisementDto getOneByAccessToken(@RequestParam(name = "token") String accessToken) throws AggregateNotFoundException {
        return jobAdvertisementApplicationService.getByAccessToken(accessToken);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PatchMapping(path = "/{id}/cancel")
    public void cancel(@PathVariable String id, CancellationResource cancellation) {
        jobAdvertisementApplicationService.cancel(new JobAdvertisementId(id), TimeMachine.now().toLocalDate(), cancellation.getReasonCode());
    }

    @GetMapping("/{id}/events")
    public PageResource<EventData> getEventsOfJobAdvertisement(@PathVariable String id) throws AggregateNotFoundException {
        return PageResource.of(eventStore.findByAggregateId(id, JobAdvertisement.class.getSimpleName(), 0, 100));
    }

}
