package ch.admin.seco.jobs.services.jobadservice.infrastructure.web.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.JobAdvertisementApplicationService;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.CancellationDto;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.CreateJobAdvertisementWebFormDto;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.JobAdvertisementDto;
import ch.admin.seco.jobs.services.jobadservice.core.domain.AggregateNotFoundException;
import ch.admin.seco.jobs.services.jobadservice.core.domain.events.EventData;
import ch.admin.seco.jobs.services.jobadservice.core.domain.events.EventStore;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisement;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisementId;

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

    @GetMapping()
    public List<JobAdvertisementDto> getJobAdvertisements() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            System.out.println(authentication.getDetails());
            System.out.println(authentication.getPrincipal());
            System.out.println(authentication.getPrincipal());
        }
        return jobAdvertisementApplicationService.findAll();
    }

    @PostMapping()
    public JobAdvertisementDto createFromWebform(@RequestBody CreateJobAdvertisementWebFormDto createJobAdvertisementWebFormDto) throws AggregateNotFoundException {
        JobAdvertisementId jobAdvertisementId = jobAdvertisementApplicationService.createFromWebForm(createJobAdvertisementWebFormDto);
        return jobAdvertisementApplicationService.findById(jobAdvertisementId);
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping(path = "/cancel")
    public void cancelFromApi(@RequestBody @Valid CancellationDto cancellationDto) {
        jobAdvertisementApplicationService.cancel(cancellationDto);
    }

    @GetMapping("/{id}")
    public JobAdvertisementDto getJobAdvertisement(@PathVariable String id) throws AggregateNotFoundException {
        return jobAdvertisementApplicationService.findById(new JobAdvertisementId(id));
    }

    @GetMapping("/{id}/events")
    public Page<EventData> getEventsOfJobAdvertisement(@PathVariable String id) throws AggregateNotFoundException {
        return eventStore.findByAggregateId(id, JobAdvertisement.class.getSimpleName(), 0, 100);
    }

}
