package ch.admin.seco.jobs.services.jobadservice.application.security;

import ch.admin.seco.jobs.services.jobadservice.core.domain.events.AuditUser;

import java.util.Collection;
import java.util.Collections;

public class TestingCurrentUserContext implements CurrentUserContext {

    private final String userExternalId;

    private final Collection<String> authorities;

    public TestingCurrentUserContext() {
        this("JUNIT-TESTING");
    }

    public TestingCurrentUserContext(String userExternalId) {
        this(userExternalId, null);
    }

    public TestingCurrentUserContext(String userExternalId, Collection<String> authorities) {
        this.userExternalId = userExternalId;
        this.authorities = authorities;
    }

    @Override
    public CurrentUser getCurrentUser() {
        return new CurrentUser(
                "userId",
                userExternalId,
                "companyId",
                "Junit",
                "Junit",
                "junit@example.com",
                (authorities == null) ? Collections.emptyList() : authorities
        );
    }

    @Override
    public AuditUser getAuditUser() {
        return new AuditUser(
                "userId",
                userExternalId,
                "companyId",
                "Junit",
                "Junit",
                "junit@example.com"
        );
    }

    @Override
    public boolean hasRole(Role role) {
        return hasAnyRoles(role);
    }

    @Override
    public boolean hasAnyRoles(Role... roles) {
        if (authorities == null) {
            return true;
        }
        for (Role role : roles) {
            if (authorities.contains(role.getValue())) {
                return true;
            }
        }
        return false;
    }

}
