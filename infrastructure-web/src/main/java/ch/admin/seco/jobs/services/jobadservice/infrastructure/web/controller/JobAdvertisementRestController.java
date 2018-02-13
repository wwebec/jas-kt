package ch.admin.seco.jobs.services.jobadservice.infrastructure.web.controller;

import ch.admin.seco.jobs.services.jobadservice.application.JobAdvertisementApplicationService;
import ch.admin.seco.jobs.services.jobadservice.application.dto.CreateJobAdvertisementDto;
import ch.admin.seco.jobs.services.jobadservice.application.dto.JobAdvertisementDto;
import ch.admin.seco.jobs.services.jobadservice.core.domain.AggregateNotFoundException;
import ch.admin.seco.jobs.services.jobadservice.core.domain.events.EventData;
import ch.admin.seco.jobs.services.jobadservice.core.domain.events.EventStore;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisement;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisementId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
        return jobAdvertisementApplicationService.findAll();
    }

    @PostMapping()
    public JobAdvertisementDto createJobAdvertisement(@RequestBody CreateJobAdvertisementDto createJobAdvertisementDto) throws AggregateNotFoundException {
        JobAdvertisementId jobAdvertisementId = jobAdvertisementApplicationService.createJobAdvertisement(createJobAdvertisementDto);
        return jobAdvertisementApplicationService.findById(jobAdvertisementId);
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
