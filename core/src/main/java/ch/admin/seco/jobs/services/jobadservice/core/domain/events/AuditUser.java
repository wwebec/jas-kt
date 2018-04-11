package ch.admin.seco.jobs.services.jobadservice.core.domain.events;

import ch.admin.seco.jobs.services.jobadservice.core.conditions.Condition;

public class AuditUser {

    private final String externalId;

    private final String firstName;

    private final String lastName;

    private final String email;

    public AuditUser(String externalId, String firstName, String lastName, String email) {
        this.externalId = Condition.notBlank(externalId);
        this.firstName = Condition.notBlank(firstName);
        this.lastName = Condition.notBlank(lastName);
        this.email = Condition.notBlank(email);
    }

    public String getExternalId() {
        return externalId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getDisplayName() {
        return this.firstName + " " + this.lastName;
    }
}
