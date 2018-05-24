package ch.admin.seco.jobs.services.jobadservice.application.apiuser;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class ApiUserApplicationService {

    private ApiUserRepository apiUserRepository;

    @Autowired
    public ApiUserApplicationService(ApiUserRepository apiUserRepository) {
        this.apiUserRepository = apiUserRepository;
    }

    public ApiUserId create(CreateApiUserDto createApiUserDto) {
        ApiUser apiUser = new ApiUser.Builder()
                .setId(new ApiUserId())
                .setUsername(createApiUserDto.getUsername())
                .setPassword(createApiUserDto.getPassword())
                .setCompanyName(createApiUserDto.getCompanyName())
                .setCompanyEmail(createApiUserDto.getCompanyEmail())
                .setTechnicalContactName(createApiUserDto.getTechnicalContactName())
                .setTechnicalContactEmail(createApiUserDto.getTechnicalContactEmail())
                .setActive(createApiUserDto.isActive())
                .setCreateDate(TimeMachine.now().toLocalDate())
                .build();

        ApiUser newApiUser = apiUserRepository.save(apiUser);
        DomainEventPublisher.publish(new ApiUserSavedEvent(newApiUser));
        return newApiUser.getId();
    }

    public Page<ApiUserDto> findAll(Pageable pageable) {
        return apiUserRepository.findAll(pageable)
                .map(ApiUserDto::toDto);
    }

    public ApiUserDto findById(ApiUserId apiUserId) {
        return apiUserRepository.findById(apiUserId)
                .map(ApiUserDto::toDto)
                .orElse(null);
    }

    public ApiUserDto update(UpdateApiUserDto updateApiUserDto) {
        ApiUser.Builder updater = new ApiUser.Builder()
                .setUsername(updateApiUserDto.getUsername())
                .setPassword(updateApiUserDto.getPassword())
                .setCompanyEmail(updateApiUserDto.getCompanyEmail())
                .setActive(updateApiUserDto.isActive())
                .setCompanyName(updateApiUserDto.getCompanyName())
                .setTechnicalContactName(updateApiUserDto.getTechnicalContactName())
                .setTechnicalContactEmail(updateApiUserDto.getTechnicalContactEmail());

        ApiUser apiUser = getById(new ApiUserId(updateApiUserDto.getId()));
        apiUser.update(updater);
        DomainEventPublisher.publish(new ApiUserSavedEvent(apiUser));
        return ApiUserDto.toDto(apiUser);
    }

    public void changeStatus(ApiUserId id, boolean activeStatus) {
        ApiUser apiUser = getById(id);
        apiUser.setActive(activeStatus);
        DomainEventPublisher.publish(new ApiUserSavedEvent(apiUser));
    }

    private ApiUser getById(ApiUserId id) {
        return apiUserRepository.findById(id).orElseThrow(() -> new AggregateNotFoundException(ApiUser.class, id.getValue()));
    }

}
