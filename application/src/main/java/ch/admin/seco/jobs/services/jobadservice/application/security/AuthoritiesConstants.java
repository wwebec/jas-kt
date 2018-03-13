package ch.admin.seco.jobs.services.jobadservice.application.security;

/**
 * Constants for Spring Security authorities.
 */
public final class AuthoritiesConstants {

    public static final String ADMIN = "ROLE_ADMIN";

    public static final String USER = "ROLE_USER";

    public static final String PRIVATE_EMPLOYMENT_AGENT = "ROLE_PRIVATE_EMPLOYMENT_AGENT";
    public static final String PUBLIC_EMPLOYMENT_SERVICE = "ROLE_PUBLIC_EMPLOYMENT_SERVICE";

    public static final String ANONYMOUS = "ROLE_ANONYMOUS";

    public static final String SYSTEM_ACCOUNT = "system";

    private AuthoritiesConstants() {
    }
}
