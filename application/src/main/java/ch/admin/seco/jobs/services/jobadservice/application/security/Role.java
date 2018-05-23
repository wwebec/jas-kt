package ch.admin.seco.jobs.services.jobadservice.application.security;

public enum Role {

    SYSADMIN("ROLE_SYSADMIN"),
    ADMIN("ROLE_ADMIN"),

    PRIVATE_EMPLOYMENT_AGENT("ROLE_PRIVATE_EMPLOYMENT_AGENT"),
    PUBLIC_EMPLOYMENT_SERVICE("ROLE_PUBLIC_EMPLOYMENT_SERVICE"),

    JOBSEEKER_CLIENT("ROLE_JOBSEEKER_CLIENT"),

    API("ROLE_API"),

    USER("ROLE_USER"),
    ALLOW("ROLE_ALLOW");

    private final String value;

    Role(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

}
