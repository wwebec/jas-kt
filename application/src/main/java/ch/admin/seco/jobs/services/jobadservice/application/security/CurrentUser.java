package ch.admin.seco.jobs.services.jobadservice.application.security;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class CurrentUser {

    private final String externalId;

    private final String companyId;

    private final String firstName;

    private final String lastName;

    private final String email;

    private final Set<String> authorities = new HashSet<>();

    public CurrentUser(String externalId, String companyId, String firstName, String lastName, String email, Collection<String> authorities) {
        this.externalId = externalId;
        this.companyId = companyId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.authorities.addAll(authorities);
    }

    public String getExternalId() {
        return externalId;
    }

    public String getCompanyId() {
        return companyId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getDisplayName() {
        return this.firstName + " " + this.lastName;
    }

    public String getEmail() {
        return email;
    }

    public Set<String> getAuthorities() {
        return authorities;
    }
}
