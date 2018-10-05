package ch.admin.seco.jobs.services.jobadservice.infrastructure.web.security;

import ch.admin.seco.jobs.services.jobadservice.core.time.TimeMachine;
import ch.admin.seco.jobs.services.jobadservice.domain.apiuser.ApiUserId;
import ch.admin.seco.jobs.services.jobadservice.domain.apiuser.ApiUserRepository;
import ch.admin.seco.jobs.services.jobadservice.infrastructure.web.config.JobAdServiceSecurityProperties;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AbstractAuthenticationEvent;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
public class AuthenticationListener implements ApplicationListener<AbstractAuthenticationEvent> {

    private final ApiUserRepository apiUserRepository;

    private final JobAdServiceSecurityProperties jobAdServiceSecurityProperties;

    public AuthenticationListener(ApiUserRepository apiUserRepository, JobAdServiceSecurityProperties jobAdServiceSecurityProperties) {
        this.apiUserRepository = apiUserRepository;
        this.jobAdServiceSecurityProperties = jobAdServiceSecurityProperties;
    }

    @Override
    @Transactional
    public void onApplicationEvent(AbstractAuthenticationEvent event) {
        if (event instanceof AuthenticationFailureBadCredentialsEvent) {
            onAuthenticationFailureBadCredentialsEvent(event);
        } else if (event instanceof AuthenticationSuccessEvent) {
            onAuthenticationSuccessEvent(event);
        }
    }

    private void onAuthenticationFailureBadCredentialsEvent(AbstractAuthenticationEvent event) {
        extractApiUserId(event)
                .flatMap(apiUserRepository::findById)
                .ifPresent(apiUser -> {
                    apiUser.changeLastAccessDate(TimeMachine.now().toLocalDate());
                    apiUser.incrementCountLoginFailure();
                    if (apiUser.getCountLoginFailure() >= jobAdServiceSecurityProperties.getApiUserMaxLoginAttempts()) {
                        apiUser.changeStatus(false);
                    }
                });
    }

    private void onAuthenticationSuccessEvent(AbstractAuthenticationEvent event) {
        extractApiUserId(event)
                .flatMap(apiUserRepository::findById)
                .ifPresent(apiUser -> {
                    apiUser.changeLastAccessDate(TimeMachine.now().toLocalDate());
                    apiUser.resetCountLoginFailure();
                });
    }

    private Optional<ApiUserId> extractApiUserId(AbstractAuthenticationEvent event) {
        Authentication authentication = event.getAuthentication();
        if (authentication.getPrincipal() instanceof UserDetailsToCurrentUserAdapter) {
            String userId = ((UserDetailsToCurrentUserAdapter) authentication.getPrincipal())
                    .getCurrentUser().getUserId();
            return Optional.of(new ApiUserId(userId));
        }
        return Optional.empty();
    }
}
