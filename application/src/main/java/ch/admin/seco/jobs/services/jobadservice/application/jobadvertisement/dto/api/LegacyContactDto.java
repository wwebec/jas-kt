package ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.api;

import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.Salutation;

import javax.validation.constraints.NotNull;

public class LegacyContactDto {

    @NotNull
    private Salutation salutation;
    @NotNull
    private String firstName;
    @NotNull
    private String lastName;
    @NotNull
    private String phoneNumber;
    @NotNull
    private String email;

    protected LegacyContactDto() {
        // For reflection libs
    }

    public LegacyContactDto(Salutation salutation, String firstName, String lastName, String phoneNumber, String email) {
        this.salutation = salutation;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }

    public Salutation getSalutation() {
        return salutation;
    }

    public void setSalutation(Salutation salutation) {
        this.salutation = salutation;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
