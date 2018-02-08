package ch.admin.seco.jobs.services.jobadservice.core.domain.events;

import ch.admin.seco.jobs.services.jobadservice.core.conditions.Condition;

public class AuditUser {

    private final String internalId;

    private final String externalId;

    private final String firstname;

    private final String lastname;

    private final String email;

    public AuditUser(String internalId, String externalId, String firstname, String lastname, String email) {
        this.internalId = Condition.notBlank(internalId);
        this.externalId = Condition.notBlank(externalId);
        this.firstname = Condition.notBlank(firstname);
        this.lastname = Condition.notBlank(lastname);
        this.email = Condition.notBlank(email);
    }

    public String getInternalId() {
        return internalId;
    }

    public String getExternalId() {
        return externalId;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getEmail() {
        return email;
    }

    public String getDisplayName() {
        return this.firstname + " " + this.lastname;
    }
}
