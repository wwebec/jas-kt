package ch.admin.seco.jobs.services.jobadservice.application.security;

import java.util.Optional;

// TODO: Replace with CurrentUserContext implementation
public interface UserService {

    Optional<String> getCurrentUserLogin();

    boolean isCurrentUserInRole(String authority);
}
