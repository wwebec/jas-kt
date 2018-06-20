package ch.admin.seco.jobs.services.jobadservice.application.apiuser;

import ch.admin.seco.jobs.services.jobadservice.application.apiuser.dto.*;
import ch.admin.seco.jobs.services.jobadservice.core.domain.AggregateNotFoundException;
import ch.admin.seco.jobs.services.jobadservice.core.domain.events.DomainEventPublisher;
import ch.admin.seco.jobs.services.jobadservice.core.time.TimeMachine;
import ch.admin.seco.jobs.services.jobadservice.domain.apiuser.ApiUser;
import ch.admin.seco.jobs.services.jobadservice.domain.apiuser.ApiUserId;
import ch.admin.seco.jobs.services.jobadservice.domain.apiuser.ApiUserRepository;
import ch.admin.seco.jobs.services.jobadservice.domain.apiuser.events.ApiUserCreatedEvent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class ApiUserApplicationService {

    private final ApiUserRepository apiUserRepository;

    private final PasswordEncoder passwordEncoder;

    public ApiUserApplicationService(ApiUserRepository apiUserRepository) {
        this.apiUserRepository = apiUserRepository;
        this.passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    public ApiUserId create(CreateApiUserDto createApiUserDto) {
        ApiUser apiUser = new ApiUser.Builder()
                .setId(new ApiUserId())
                .setUsername(createApiUserDto.getUsername())
                .setPassword(passwordEncoder.encode(createApiUserDto.getPassword()))
                .setCompanyName(createApiUserDto.getCompanyName())
                .setCompanyEmail(createApiUserDto.getCompanyEmail())
                .setTechnicalContactName(createApiUserDto.getTechnicalContactName())
                .setTechnicalContactEmail(createApiUserDto.getTechnicalContactEmail())
                .setActive(createApiUserDto.isActive())
                .setCreateDate(TimeMachine.now().toLocalDate())
                .build();

        ApiUser newApiUser = apiUserRepository.save(apiUser);
        DomainEventPublisher.publish(new ApiUserCreatedEvent(newApiUser));
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

    public ApiUserDto changeDetails(ApiUserId apiUserId, UpdateDetailsApiUserDto updateDetailsApiUserDto) {
        ApiUser apiUser = getById(apiUserId);
        apiUser.updateDetails(new ApiUser.Builder()
                .setUsername(updateDetailsApiUserDto.getUsername())
                .setCompanyEmail(updateDetailsApiUserDto.getCompanyEmail())
                .setCompanyName(updateDetailsApiUserDto.getCompanyName())
                .setTechnicalContactName(updateDetailsApiUserDto.getTechnicalContactName())
                .setTechnicalContactEmail(updateDetailsApiUserDto.getTechnicalContactEmail())
        );
        return ApiUserDto.toDto(apiUser);
    }

    public void changePassword(ApiUserId apiUserId, UpdatePasswordApiUserDto updatePasswordApiUserDto) {
        ApiUser apiUser = getById(apiUserId);
        apiUser.changePassword(passwordEncoder.encode(updatePasswordApiUserDto.getPassword()));
    }

    public void changeStatus(ApiUserId apiUserId, UpdateStatusApiUserDto updateStatusApiUserDto) {
        ApiUser apiUser = getById(apiUserId);
        apiUser.changeStatus(updateStatusApiUserDto.isActive());
    }

    private ApiUser getById(ApiUserId id) {
        return apiUserRepository.findById(id).orElseThrow(() -> new AggregateNotFoundException(ApiUser.class, id.getValue()));
    }

}
