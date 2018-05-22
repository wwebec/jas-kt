package ch.admin.seco.jobs.services.jobadservice.application.security;

import ch.admin.seco.jobs.services.jobadservice.core.domain.events.AuditUser;

import java.util.Collections;

public class TestingCurrentUserContext implements CurrentUserContext {

    private final String externalId;

    public TestingCurrentUserContext() {
        this("JUNIT-TESTING");
    }

    public TestingCurrentUserContext(String externalId) {
        this.externalId = externalId;
    }

    @Override
    public CurrentUser getCurrentUser() {
        return new CurrentUser(
                "userId",
                externalId,
                "companyId",
                "Junit",
                "Junit",
                "junit@example.com",
                Collections.emptyList()
        );
    }

    @Override
    public AuditUser getAuditUser() {
        return new AuditUser(
                "userId",
                externalId,
                "companyId",
                "Junit",
                "Junit",
                "junit@example.com"
        );
    }

    @Override
    public boolean hasRole(Role role) {
        return true;
    }

    @Override
    public boolean hasAnyRoles(Role... roles) {
        return true;
    }

}
