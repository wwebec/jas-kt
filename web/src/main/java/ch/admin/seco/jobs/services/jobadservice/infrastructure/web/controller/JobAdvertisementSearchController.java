package ch.admin.seco.jobs.services.jobadservice.infrastructure.web.controller;

import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.JobAdvertisementDto;
import ch.admin.seco.jobs.services.jobadservice.application.security.AuthoritiesConstants;
import ch.admin.seco.jobs.services.jobadservice.infrastructure.elasticsearch.read.JobAdvertisementSearchRequest;
import ch.admin.seco.jobs.services.jobadservice.infrastructure.elasticsearch.read.JobAdvertisementSearchService;
import ch.admin.seco.jobs.services.jobadservice.infrastructure.elasticsearch.write.JobAdvertisementIndexerService;
import ch.admin.seco.jobs.services.jobadservice.infrastructure.web.util.HeaderUtil;
import ch.admin.seco.jobs.services.jobadservice.infrastructure.web.util.PaginationUtil;
import com.codahale.metrics.annotation.Timed;
import io.swagger.annotations.ApiParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
@RequestMapping("/api/jobAdvertisement")
public class JobAdvertisementSearchController {
    private final JobAdvertisementIndexerService jobAdvertisementIndexerService;
    private final JobAdvertisementSearchService jobAdvertisementSearchService;

    public JobAdvertisementSearchController(JobAdvertisementIndexerService jobAdvertisementIndexerService,
                                            JobAdvertisementSearchService jobAdvertisementSearchService) {
        this.jobAdvertisementIndexerService = jobAdvertisementIndexerService;
        this.jobAdvertisementSearchService = jobAdvertisementSearchService;
    }

    @PostMapping("/_search/jobAdvertisement")
    @Timed
    public ResponseEntity<List<JobAdvertisementDto>> searchJobs(
            @RequestBody @Valid JobAdvertisementSearchRequest jobAdvertisementSearchRequest,
            @ApiParam Pageable pageable) {

        Page<JobAdvertisementDto> page = jobAdvertisementSearchService.search(jobAdvertisementSearchRequest, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/_search/jobs");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @PostMapping("/_count/jobAdvertisement")
    @Timed
    public ResponseEntity<Map<String, Long>> countJobs(
            @RequestBody @Valid JobAdvertisementSearchRequest jobAdvertisementSearchRequest) {

        long totalCount = jobAdvertisementSearchService.count(jobAdvertisementSearchRequest);
        return new ResponseEntity<>(Collections.singletonMap("totalCount", totalCount), HttpStatus.OK);
    }

    @RequestMapping(value = "/elasticsearch/index",
            method = RequestMethod.POST,
            produces = MediaType.TEXT_PLAIN_VALUE)
    @Timed
    @PreAuthorize("hasAuthority('" + AuthoritiesConstants.ADMIN + "')")
    public ResponseEntity<Void> reindexAll() {
        jobAdvertisementIndexerService.reindexAll();
        return ResponseEntity.accepted()
                .headers(HeaderUtil.createAlert("elasticsearch.reindex.jobservice.accepted", ""))
                .build();
    }

}
