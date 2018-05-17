package ch.admin.seco.jobs.services.jobadservice.application.apiuser;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import ch.admin.seco.jobs.services.jobadservice.application.apiuser.dto.ApiUserDto;
import ch.admin.seco.jobs.services.jobadservice.application.apiuser.dto.CreateApiUserDto;
import ch.admin.seco.jobs.services.jobadservice.application.apiuser.dto.UpdateApiUserDto;
import ch.admin.seco.jobs.services.jobadservice.core.domain.AggregateNotFoundException;
import ch.admin.seco.jobs.services.jobadservice.core.domain.events.DomainEventPublisher;
import ch.admin.seco.jobs.services.jobadservice.core.time.TimeMachine;
import ch.admin.seco.jobs.services.jobadservice.domain.apiuser.ApiUser;
import ch.admin.seco.jobs.services.jobadservice.domain.apiuser.ApiUserId;
import ch.admin.seco.jobs.services.jobadservice.domain.apiuser.ApiUserRepository;
import ch.admin.seco.jobs.services.jobadservice.domain.apiuser.events.ApiUserSavedEvent;

@Service
@Transactional
public class ApiUserService {

	private ApiUserRepository apiUserRepository;

	@Autowired
	public ApiUserService(ApiUserRepository apiUserRepository) {
		this.apiUserRepository = apiUserRepository;
	}

	public ApiUserId createApiUser(CreateApiUserDto createApiUserDto) {
		ApiUser.Builder builder = new ApiUser.Builder();
		builder.setId(new ApiUserId());
		builder.setUsername(createApiUserDto.getUsername());
		builder.setPassword(createApiUserDto.getPassword());
		builder.setEmail(createApiUserDto.getEmail());
		builder.setActive(createApiUserDto.getActive());
		builder.setCompanyName(createApiUserDto.getCompanyName());
		builder.setContactName(createApiUserDto.getContactName());
		builder.setContactEmail(createApiUserDto.getContactEmail());
		builder.setCreateDate(TimeMachine.now().toLocalDate());

		ApiUser apiUser = apiUserRepository.save(new ApiUser(builder));
		DomainEventPublisher.publish(new ApiUserSavedEvent(apiUser));
		return apiUser.getId();
	}

	public void changeApiUserStatus(ApiUserId id, boolean activeStatus) {
		ApiUser apiUser = getById(id);
		apiUser.setActive(activeStatus);
		DomainEventPublisher.publish(new ApiUserSavedEvent(apiUser));
	}

	private ApiUser getById(ApiUserId id) {
		return apiUserRepository.findById(id)
				.orElseThrow(() -> new AggregateNotFoundException(ApiUser.class, id.getValue()));
	}

	public ApiUserDto findById(ApiUserId apiUserId) {
		return apiUserRepository.findById(apiUserId)
				.map(ApiUserDto::toDto)
				.orElse(null);
	}

	public Page<ApiUserDto> findAll(Pageable pageable) {
		return apiUserRepository.findAll(pageable)
				.map(ApiUserDto::toDto);
	}

	public ApiUserDto update(UpdateApiUserDto updateApiUserDto) {
		ApiUser.Builder builder = new ApiUser.Builder();
		builder.setUsername(updateApiUserDto.getUsername());
		builder.setPassword(updateApiUserDto.getPassword());
		builder.setEmail(updateApiUserDto.getEmail());
		builder.setActive(updateApiUserDto.getActive());
		builder.setCompanyName(updateApiUserDto.getCompanyName());
		builder.setContactName(updateApiUserDto.getContactName());
		builder.setContactEmail(updateApiUserDto.getContactEmail());

		ApiUser apiUser = getById(new ApiUserId(updateApiUserDto.getId()));
		apiUser.update(builder);
		DomainEventPublisher.publish(new ApiUserSavedEvent(apiUser));
		return ApiUserDto.toDto(apiUser);
	}

}
