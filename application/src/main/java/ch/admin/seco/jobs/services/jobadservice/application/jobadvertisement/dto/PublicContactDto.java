package ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.PublicContact;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.Salutation;

public class PublicContactDto {

    @NotNull
    private Salutation salutation;

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @NotBlank
    private String phone;

    @NotBlank
    private String email;

    public Salutation getSalutation() {
        return salutation;
    }

    public PublicContactDto setSalutation(Salutation salutation) {
        this.salutation = salutation;
        return this;
    }

    public String getFirstName() {
        return firstName;
    }

    public PublicContactDto setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String getLastName() {
        return lastName;
    }

    public PublicContactDto setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public String getPhone() {
        return phone;
    }

    public PublicContactDto setPhone(String phone) {
        this.phone = phone;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public PublicContactDto setEmail(String email) {
        this.email = email;
        return this;
    }

    public static PublicContactDto toDto(PublicContact publicContact) {
        if (publicContact == null) {
            return null;
        }
        return new PublicContactDto()
                .setSalutation(publicContact.getSalutation())
                .setFirstName(publicContact.getFirstName())
                .setLastName(publicContact.getLastName())
                .setPhone(publicContact.getPhone())
                .setEmail(publicContact.getEmail());
    }

}
