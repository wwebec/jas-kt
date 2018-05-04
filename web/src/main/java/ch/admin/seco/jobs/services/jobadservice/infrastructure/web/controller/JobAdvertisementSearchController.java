package ch.admin.seco.jobs.services.jobadservice.infrastructure.web.controller;

import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.JobAdvertisementDto;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.JobDescriptionDto;
import ch.admin.seco.jobs.services.jobadservice.application.security.AuthoritiesConstants;
import ch.admin.seco.jobs.services.jobadservice.infrastructure.elasticsearch.read.JobAdvertisementSearchRequest;
import ch.admin.seco.jobs.services.jobadservice.infrastructure.elasticsearch.read.JobAdvertisementSearchService;
import ch.admin.seco.jobs.services.jobadservice.infrastructure.elasticsearch.write.JobAdvertisementIndexerService;
import ch.admin.seco.jobs.services.jobadservice.infrastructure.web.util.HeaderUtil;
import ch.admin.seco.jobs.services.jobadservice.infrastructure.web.util.PaginationUtil;
import com.codahale.metrics.annotation.Timed;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Whitelist;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/jobAdvertisements")
public class JobAdvertisementSearchController {
    private final JobAdvertisementIndexerService jobAdvertisementIndexerService;
    private final JobAdvertisementSearchService jobAdvertisementSearchService;

    public JobAdvertisementSearchController(JobAdvertisementIndexerService jobAdvertisementIndexerService,
                                            JobAdvertisementSearchService jobAdvertisementSearchService) {
        this.jobAdvertisementIndexerService = jobAdvertisementIndexerService;
        this.jobAdvertisementSearchService = jobAdvertisementSearchService;
    }

    @PostMapping("/_search")
    @Timed
    public ResponseEntity<List<JobAdvertisementDto>> searchJobs(
            @RequestBody @Valid JobAdvertisementSearchRequest jobAdvertisementSearchRequest,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "20") int size,
            @RequestParam(defaultValue = "score") JobAdvertisementSearchService.SearchSort sort
    ) {

        Page<JobAdvertisementDto> resultPage = jobAdvertisementSearchService.search(jobAdvertisementSearchRequest, page, size, sort)
                //todo: Discuss where to put the HTML cleanup. This is suboptimal concerning performance
                .map(this::sanitizeJobDescription);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(resultPage, "/api/_search/jobs");
        return new ResponseEntity<>(resultPage.getContent(), headers, HttpStatus.OK);
    }

    @PostMapping("/_count")
    @Timed
    public ResponseEntity<Map<String, Long>> countJobs(
            @RequestBody @Valid JobAdvertisementSearchRequest jobAdvertisementSearchRequest) {

        long totalCount = jobAdvertisementSearchService.count(jobAdvertisementSearchRequest);
        return new ResponseEntity<>(Collections.singletonMap("totalCount", totalCount), HttpStatus.OK);
    }

    @PostMapping(value = "/elasticsearch/index", produces = MediaType.TEXT_PLAIN_VALUE)
    @Timed
    @PreAuthorize("hasAuthority('" + AuthoritiesConstants.ADMIN + "')")
    public ResponseEntity<Void> reindexAll() {
        jobAdvertisementIndexerService.reindexAll();
        return ResponseEntity.accepted()
                .headers(HeaderUtil.createAlert("elasticsearch.reindex.jobservice.accepted", ""))
                .build();
    }

    private JobAdvertisementDto sanitizeJobDescription(JobAdvertisementDto jobAdvertisementDto) {
        for (JobDescriptionDto jobDescriptionDto : jobAdvertisementDto.getJobContent().getJobDescriptions()) {
            String sanitizedDescription = Jsoup.clean(
                    jobDescriptionDto.getDescription(),
                    "",
                    new Whitelist().addTags("em"),
                    new Document.OutputSettings().prettyPrint(false));
            jobDescriptionDto.setDescription(sanitizedDescription);
        }

        return jobAdvertisementDto;
    }

}
