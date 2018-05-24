package ch.admin.seco.jobs.services.jobadservice.infrastructure.web.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ch.admin.seco.jobs.services.jobadservice.application.apiuser.ApiUserApplicationService;
import ch.admin.seco.jobs.services.jobadservice.application.apiuser.dto.ApiUserDto;
import ch.admin.seco.jobs.services.jobadservice.application.apiuser.dto.ChangeApiUserStatusDto;
import ch.admin.seco.jobs.services.jobadservice.application.apiuser.dto.CreateApiUserDto;
import ch.admin.seco.jobs.services.jobadservice.application.apiuser.dto.UpdateApiUserDto;
import ch.admin.seco.jobs.services.jobadservice.domain.apiuser.ApiUserId;
import ch.admin.seco.jobs.services.jobadservice.infrastructure.elasticsearch.read.apiuser.ApiUserSearchRequest;
import ch.admin.seco.jobs.services.jobadservice.infrastructure.elasticsearch.read.apiuser.ApiUserSearchService;
import ch.admin.seco.jobs.services.jobadservice.infrastructure.web.util.PaginationUtil;

@RestController
@RequestMapping("/api/apiUsers")
public class ApiUserRestController {

	private ApiUserApplicationService apiUserApplicationService;
	private ApiUserSearchService apiUserSearchService;

	@Autowired
	public ApiUserRestController(ApiUserApplicationService apiUserApplicationService, ApiUserSearchService apiUserSearchService) {
		this.apiUserApplicationService = apiUserApplicationService;
		this.apiUserSearchService = apiUserSearchService;
	}

	@PostMapping
	public ApiUserDto createApiUser(@Valid @RequestBody CreateApiUserDto createApiUserDto) {
		ApiUserId apiUserId = apiUserApplicationService.create(createApiUserDto);
		return apiUserApplicationService.findById(apiUserId);
	}

	@PutMapping
	public ApiUserDto update(@Valid @RequestBody UpdateApiUserDto updateApiUserDto) {
		return apiUserApplicationService.update(updateApiUserDto);
	}

	@GetMapping("/{id}")
	public ApiUserDto findById(@PathVariable String id) {
		return apiUserApplicationService.findById(new ApiUserId(id));
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

	@PutMapping("/{id}/active")
	public void changeApiUserStatus(@PathVariable String id, @Valid @RequestBody ChangeApiUserStatusDto statusDto) {
		apiUserApplicationService.changeStatus(new ApiUserId(id), statusDto.getActive());
	}
}
