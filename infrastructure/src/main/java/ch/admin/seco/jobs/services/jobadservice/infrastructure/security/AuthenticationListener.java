package ch.admin.seco.jobs.services.jobadservice.infrastructure.security;

import java.util.Optional;

import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AbstractAuthenticationEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import ch.admin.seco.jobs.services.jobadservice.core.time.TimeMachine;
import ch.admin.seco.jobs.services.jobadservice.domain.apiuser.ApiUserId;
import ch.admin.seco.jobs.services.jobadservice.domain.apiuser.ApiUserRepository;
import ch.admin.seco.jobs.services.jobadservice.infrastructure.web.security.UserDetailsToCurrentUserAdapter;

@Component
public class AuthenticationListener implements ApplicationListener<AbstractAuthenticationEvent> {

    private final ApiUserRepository apiUserRepository;

    public AuthenticationListener(ApiUserRepository apiUserRepository) {
        this.apiUserRepository = apiUserRepository;
    }

    @Override
    @Transactional
    public void onApplicationEvent(AbstractAuthenticationEvent event) {
        if (event instanceof AuthenticationSuccessEvent) {
            extractApiUserId((AuthenticationSuccessEvent) event)
                    .flatMap(apiUserRepository::findById)
                    .ifPresent(apiUser -> apiUser.changeLastAccessDate(TimeMachine.now().toLocalDate()));
        }
    }

    private Optional<ApiUserId> extractApiUserId(AuthenticationSuccessEvent authenticationSuccessEvent) {
        Authentication authentication = authenticationSuccessEvent.getAuthentication();
        if (authentication.getPrincipal() instanceof UserDetailsToCurrentUserAdapter) {
            String userId = ((UserDetailsToCurrentUserAdapter) authentication.getPrincipal())
                    .getCurrentUser().getUserId();
            return Optional.of(new ApiUserId(userId));
        }
        return Optional.empty();
    }
}
