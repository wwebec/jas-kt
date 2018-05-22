package ch.admin.seco.jobs.services.jobadservice.application.security;

import ch.admin.seco.jobs.services.jobadservice.core.domain.events.AuditUser;

public interface CurrentUserContext {

    String PREFIX_ROLE = "ROLE_";

    CurrentUser getCurrentUser();

    AuditUser getAuditUser();

    boolean hasRole(Role role);

    boolean hasAnyRoles(Role... roles);

}
