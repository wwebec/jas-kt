package ch.admin.seco.jobs.services.jobadservice.core.domain.events;

import ch.admin.seco.jobs.services.jobadservice.core.conditions.Condition;

public class AuditUser {

    private final String userId;

    private final String externalId;

    private final String companyId;

    private final String firstName;

    private final String lastName;

    private final String email;

    public AuditUser(String firstName, String lastName) {
        this.userId = null;
        this.externalId = null;
        this.companyId = null;
        this.firstName = Condition.notBlank(firstName);
        this.lastName = Condition.notBlank(lastName);
        this.email = null;
    }

    public AuditUser(String userId, String externalId, String companyId, String firstName, String lastName, String email) {
        this.userId = Condition.notBlank(userId);
        this.externalId = Condition.notBlank(externalId);
        this.companyId = companyId;
        this.firstName = Condition.notBlank(firstName);
        this.lastName = Condition.notBlank(lastName);
        this.email = Condition.notBlank(email);
    }

    public String getUserId() {
        return userId;
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

    public String getEmail() {
        return email;
    }

    public String getDisplayName() {
        return this.firstName + " " + this.lastName;
    }
}
