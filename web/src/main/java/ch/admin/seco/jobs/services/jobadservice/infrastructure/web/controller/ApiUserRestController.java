package ch.admin.seco.jobs.services.jobadservice.infrastructure.web.controller;

import ch.admin.seco.jobs.services.jobadservice.application.apiuser.ApiUserApplicationService;
import ch.admin.seco.jobs.services.jobadservice.application.apiuser.dto.*;
import ch.admin.seco.jobs.services.jobadservice.core.domain.AggregateNotFoundException;
import ch.admin.seco.jobs.services.jobadservice.core.domain.events.EventData;
import ch.admin.seco.jobs.services.jobadservice.core.domain.events.EventStore;
import ch.admin.seco.jobs.services.jobadservice.domain.apiuser.ApiUser;
import ch.admin.seco.jobs.services.jobadservice.domain.apiuser.ApiUserId;
import ch.admin.seco.jobs.services.jobadservice.infrastructure.elasticsearch.read.apiuser.ApiUserSearchRequest;
import ch.admin.seco.jobs.services.jobadservice.infrastructure.elasticsearch.read.apiuser.ApiUserSearchService;
import ch.admin.seco.jobs.services.jobadservice.infrastructure.web.controller.resources.PageResource;
import ch.admin.seco.jobs.services.jobadservice.infrastructure.web.util.PaginationUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/apiUsers")
public class ApiUserRestController {

    private final ApiUserApplicationService apiUserApplicationService;
    private final ApiUserSearchService apiUserSearchService;
    private final EventStore eventStore;

    public ApiUserRestController(ApiUserApplicationService apiUserApplicationService, ApiUserSearchService apiUserSearchService, EventStore eventStore) {
        this.apiUserApplicationService = apiUserApplicationService;
        this.apiUserSearchService = apiUserSearchService;
        this.eventStore = eventStore;
    }

    @PostMapping
    public ApiUserDto createApiUser(@Valid @RequestBody CreateApiUserDto createApiUserDto) {
        ApiUserId apiUserId = apiUserApplicationService.create(createApiUserDto);
        return apiUserApplicationService.findById(apiUserId);
    }

    @GetMapping
    public Page<ApiUserDto> findAll(Pageable pageable) {
        return apiUserApplicationService.findAll(pageable);
    }

    @PostMapping("/_search")
    public ResponseEntity<List<ApiUserDto>> search(@Valid @RequestBody ApiUserSearchRequest searchRequest, Pageable pageable) {
        Page<ApiUserDto> resultPage = apiUserSearchService.search(searchRequest, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(resultPage, "/api/apiUsers/_search");
        return new ResponseEntity<>(resultPage.getContent(), headers, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ApiUserDto findById(@PathVariable String id) {
        return apiUserApplicationService.findById(new ApiUserId(id));
    }

    @PutMapping("/{id}")
    public ApiUserDto changeDetails(@PathVariable String id, @Valid @RequestBody UpdateDetailsApiUserDto updateDetailsApiUserDto) {
        return apiUserApplicationService.changeDetails(new ApiUserId(id), updateDetailsApiUserDto);
    }

    @PutMapping("/{id}/password")
    public void changePassword(@PathVariable String id, @Valid @RequestBody UpdatePasswordApiUserDto updatePasswordApiUserDto) {
        apiUserApplicationService.changePassword(new ApiUserId(id), updatePasswordApiUserDto);
    }

    @PutMapping("/{id}/active")
    public void changeStatus(@PathVariable String id, @Valid @RequestBody UpdateStatusApiUserDto updateStatusApiUserDto) {
        apiUserApplicationService.changeStatus(new ApiUserId(id), updateStatusApiUserDto);
    }

    @GetMapping("/{id}/events")
    public PageResource<EventData> getEventsOfApiUser(@PathVariable String id) throws AggregateNotFoundException {
        return PageResource.of(eventStore.findByAggregateId(id, ApiUser.class.getSimpleName(), 0, 100));
    }
}
