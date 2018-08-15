package ch.admin.seco.jobs.services.jobadservice.infrastructure.web.controller;

import ch.admin.seco.jobs.services.jobadservice.application.HtmlToMarkdownConverter;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.JobAdvertisementApplicationService;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.JobAdvertisementDto;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.api.ApiCreateJobAdvertisementDto;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.api.ApiToNormalCreateJobAdvertisementDtoConverter;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.create.CreateJobAdvertisementDto;
import ch.admin.seco.jobs.services.jobadservice.core.domain.AggregateNotFoundException;
import ch.admin.seco.jobs.services.jobadservice.core.time.TimeMachine;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisementId;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.SourceSystem;
import ch.admin.seco.jobs.services.jobadservice.infrastructure.web.controller.resources.CancellationResource;
import ch.admin.seco.jobs.services.jobadservice.infrastructure.web.controller.resources.PageResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/public/jobAdvertisements/v1")
public class JobAdvertisementApiRestController {

    private final JobAdvertisementApplicationService jobAdvertisementApplicationService;
    private final HtmlToMarkdownConverter htmlToMarkdownConverter;

    @Autowired
    public JobAdvertisementApiRestController(JobAdvertisementApplicationService jobAdvertisementApplicationService,
            HtmlToMarkdownConverter htmlToMarkdownConverter) {
        this.jobAdvertisementApplicationService = jobAdvertisementApplicationService;
        this.htmlToMarkdownConverter = htmlToMarkdownConverter;
    }

    @PostMapping
    public JobAdvertisementDto createFromApi(
            @RequestBody @Valid ApiCreateJobAdvertisementDto apiCreateJobAdvertisementDto
    ) throws AggregateNotFoundException {
        CreateJobAdvertisementDto createJobAdvertisementDto = new ApiToNormalCreateJobAdvertisementDtoConverter(htmlToMarkdownConverter)
                .convert(apiCreateJobAdvertisementDto);
        JobAdvertisementId jobAdvertisementId = jobAdvertisementApplicationService.createFromApi(createJobAdvertisementDto);
        return jobAdvertisementApplicationService.getById(jobAdvertisementId);
    }

    @GetMapping
    public PageResource<JobAdvertisementDto> getJobAdvertisements(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "25") int size
    ) {
        return PageResource.of(jobAdvertisementApplicationService.findOwnJobAdvertisements(PageRequest.of(page, size)));
    }

    @GetMapping("/{id}")
    public JobAdvertisementDto getJobAdvertisement(
            @PathVariable String id
    ) {
        return jobAdvertisementApplicationService.getById(new JobAdvertisementId(id));
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PatchMapping("/{id}/cancel")
    public void cancel(
            @PathVariable String id,
            @RequestBody CancellationResource cancellation
    ) {
        jobAdvertisementApplicationService.cancel(new JobAdvertisementId(id), TimeMachine.now().toLocalDate(), cancellation.getCode(), SourceSystem.API, null);
    }

}
