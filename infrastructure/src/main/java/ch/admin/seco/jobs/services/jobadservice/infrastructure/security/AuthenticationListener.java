package ch.admin.seco.jobs.services.jobadservice.infrastructure.security;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AbstractAuthenticationEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import ch.admin.seco.jobs.services.jobadservice.domain.apiuser.ApiUserRepository;

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
            extractUserName((AuthenticationSuccessEvent) event)
                    .map(apiUserRepository::findByUsername)
                    .ifPresent(apiUser -> {
                        apiUser.changeLastAccessDate(LocalDate.now());
                        apiUserRepository.save(apiUser);
                    });
        }
    }

    private Optional<String> extractUserName(AuthenticationSuccessEvent authenticationSuccessEvent) {
        Authentication authentication = authenticationSuccessEvent.getAuthentication();
        if (authentication.getPrincipal() instanceof UserDetails) {
            return Optional.ofNullable(((UserDetails) authentication.getPrincipal()).getUsername());
        }
        return Optional.empty();
    }
}
