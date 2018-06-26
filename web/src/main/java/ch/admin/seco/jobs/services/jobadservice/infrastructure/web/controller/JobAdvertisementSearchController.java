package ch.admin.seco.jobs.services.jobadservice.infrastructure.web.controller;

import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.JobAdvertisementDto;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.JobDescriptionDto;
import ch.admin.seco.jobs.services.jobadservice.infrastructure.elasticsearch.read.jobadvertisement.JobAdvertisementSearchRequest;
import ch.admin.seco.jobs.services.jobadservice.infrastructure.elasticsearch.read.jobadvertisement.JobAdvertisementSearchService;
import ch.admin.seco.jobs.services.jobadservice.infrastructure.elasticsearch.read.jobadvertisement.PeaJobAdvertisementSearchRequest;
import ch.admin.seco.jobs.services.jobadservice.infrastructure.web.util.PaginationUtil;
import com.codahale.metrics.annotation.Timed;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Whitelist;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.springframework.util.StringUtils.hasText;

@RestController
@RequestMapping("/api/jobAdvertisements")
public class JobAdvertisementSearchController {
    private final JobAdvertisementSearchService jobAdvertisementSearchService;

    public JobAdvertisementSearchController(JobAdvertisementSearchService jobAdvertisementSearchService) {
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

    @PostMapping("/_search/pea")
    @Timed
    public ResponseEntity<List<JobAdvertisementDto>> searchPeaJobs(
            @RequestBody @Valid PeaJobAdvertisementSearchRequest searchRequest, Pageable pageable) {

        Page<JobAdvertisementDto> resultPage = jobAdvertisementSearchService.searchPeaJobAdvertisements(searchRequest, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(resultPage, "/api/_search/pea");
        return new ResponseEntity<>(resultPage.getContent(), headers, HttpStatus.OK);
    }

    @PostMapping("/_count")
    @Timed
    public ResponseEntity<Map<String, Long>> countJobs(
            @RequestBody @Valid JobAdvertisementSearchRequest jobAdvertisementSearchRequest) {

        long totalCount = jobAdvertisementSearchService.count(jobAdvertisementSearchRequest);
        return new ResponseEntity<>(Collections.singletonMap("totalCount", totalCount), HttpStatus.OK);
    }

    private JobAdvertisementDto sanitizeJobDescription(JobAdvertisementDto jobAdvertisementDto) {
        for (JobDescriptionDto jobDescriptionDto : jobAdvertisementDto.getJobContent().getJobDescriptions()) {
            String sanitizedDescription = "";
            if (hasText(jobDescriptionDto.getDescription())) {
                sanitizedDescription = Jsoup.clean(
                        jobDescriptionDto.getDescription(),
                        "",
                        new Whitelist().addTags("em"),
                        new Document.OutputSettings().prettyPrint(false));
            }
            jobDescriptionDto.setDescription(sanitizedDescription);
        }

        return jobAdvertisementDto;
    }

}
