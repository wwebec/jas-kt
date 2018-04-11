package ch.admin.seco.jobs.services.jobadservice.application.security;

import ch.admin.seco.jobs.services.jobadservice.core.domain.events.AuditUser;

public interface UserContext {

    CurrentUser getCurrentUser();

    AuditUser getAuditUser();

}
