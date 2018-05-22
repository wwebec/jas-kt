package ch.admin.seco.jobs.services.jobadservice.infrastructure.web.security;

import ch.admin.seco.jobs.services.jobadservice.application.security.CurrentUser;
import ch.admin.seco.jobs.services.jobadservice.application.security.CurrentUserContext;
import ch.admin.seco.jobs.services.jobadservice.application.security.Role;
import ch.admin.seco.jobs.services.jobadservice.core.conditions.Condition;
import ch.admin.seco.jobs.services.jobadservice.core.domain.events.AuditUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.util.StringUtils.hasText;

@Component
public class SpringSecurityBasedCurrentUserContext implements CurrentUserContext {

    public static final String USER_ANONYMOUS = "anonymousUser";

    @Override
    public CurrentUser getCurrentUser() {
        Authentication authentication = getAuthentication();
        Object principal = getPrincipal(authentication);
        if (principal instanceof UserDetailsToCurrentUserAdapter) {
            return ((UserDetailsToCurrentUserAdapter) principal).getCurrentUser();
        }
        if (principal instanceof String) {
            String username = (String) principal;
            if (hasText(username) && username.equals(USER_ANONYMOUS)) {
                return null;
            }
        }
        throw new IllegalStateException(String.format("Could not extract Current User from Principal since it's class is: %s", principal.getClass()));
    }

    @Override
    public AuditUser getAuditUser() {
        final CurrentUser currentUser = getCurrentUser();
        if (currentUser != null) {
            return new AuditUser(
                    currentUser.getUserId(),
                    currentUser.getExternalId(),
                    currentUser.getCompanyId(),
                    currentUser.getFirstName(),
                    currentUser.getLastName(),
                    currentUser.getEmail()
            );
        }
        return new AuditUser("Anonymous", "User");
    }

    @Override
    public boolean hasRole(Role role) {
        return hasAnyRoles(role);
    }

    @Override
    public boolean hasAnyRoles(Role... roles) {
        List<String> authorities = getAuthorities();
        if (authorities != null) {
            for (Role role : roles) {
                if (authorities.contains(role.getValue())) {
                    return true;
                }
            }
        }
        return false;
    }

    private Object getPrincipal(Authentication authentication) {
        Object principal = authentication.getPrincipal();
        Condition.notNull(principal, "No User has been found in the securityContext");
        return principal;
    }

    private Authentication getAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Condition.notNull(authentication, "No Authentication has been found in the securityContext");
        return authentication;
    }

    private List<String> getAuthorities() {
        Authentication auth = getAuthentication();
        if ((auth == null) || (auth.getPrincipal() == null)) {
            return null;
        }
        Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();
        if ((authorities == null) || authorities.isEmpty()) {
            return null;
        }
        return authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
    }
}
