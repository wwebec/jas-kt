package ch.admin.seco.jobs.services.jobadservice.infrastructure.web.controller;

import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.JobAdvertisementApplicationService;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.JobAdvertisementDto;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.create.CreateJobAdvertisementDto;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.create.legacy.*;
import ch.admin.seco.jobs.services.jobadservice.core.domain.AggregateNotFoundException;
import ch.admin.seco.jobs.services.jobadservice.core.time.TimeMachine;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.CancellationCode;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisementId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Deprecated
@RestController
@RequestMapping("/api/public/jobAdvertisements/legacy")
public class JobAdvertisementLegacyApiRestController {

    private final JobAdvertisementApplicationService jobAdvertisementApplicationService;

    @Autowired
    public JobAdvertisementLegacyApiRestController(JobAdvertisementApplicationService jobAdvertisementApplicationService) {
        this.jobAdvertisementApplicationService = jobAdvertisementApplicationService;
    }

    @PostMapping
    public LegacyJobAdvertisementDto create(@RequestBody @Valid LegacyCreateJobAdvertisementDto legacyCreateJobAdvertisementDto) throws AggregateNotFoundException {
        CreateJobAdvertisementDto createJobAdvertisementDto = LegacyToCreateJobAdvertisementDtoConverter.convert(legacyCreateJobAdvertisementDto);
        JobAdvertisementId jobAdvertisementId = jobAdvertisementApplicationService.createFromApi(createJobAdvertisementDto);
        JobAdvertisementDto jobAdvertisementDto = jobAdvertisementApplicationService.getById(jobAdvertisementId);
        return LegacyFromJobAdvertisementDtoConverter.convert(jobAdvertisementDto);
    }

    @GetMapping(params = {"page", "size"})
    public LegacyPageDto getAll(@RequestParam("page") int page, @RequestParam("size") int size) {
        Page<JobAdvertisementDto> paginated = jobAdvertisementApplicationService.findOwnJobAdvertisements(PageRequest.of(page, size));
        return LegacyFromJobAdvertisementDtoConverter.convert(paginated);
    }

    @GetMapping(path = "/{id}")
    public LegacyJobAdvertisementDto getOne(@PathVariable String id) {
        JobAdvertisementDto jobAdvertisementDto = jobAdvertisementApplicationService.getById(new JobAdvertisementId(id));
        return LegacyFromJobAdvertisementDtoConverter.convert(jobAdvertisementDto);
    }

    @ResponseStatus(HttpStatus.NOT_IMPLEMENTED)
    @PatchMapping(path = "/{id}")
    public void update(@PathVariable String id) {
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PostMapping(path = "/{id}/cancel", consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    public void cancel(@PathVariable String id, LegacyCancellationDto cancellationDto) {
        jobAdvertisementApplicationService.cancel(new JobAdvertisementId(id), TimeMachine.now().toLocalDate(), determinCancellationCode(cancellationDto.getReasonCode()));
    }

    private CancellationCode determinCancellationCode(String reasonCode) {
        switch (reasonCode) {
            case "1": return CancellationCode.OCCUPIED_JOBROOM;
            case "2": return CancellationCode.OCCUPIED_JOBCENTER;
            default: return CancellationCode.OCCUPIED_OTHER;
        }
    }

}
