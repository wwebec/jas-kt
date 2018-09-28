package ch.admin.seco.jobs.services.jobadservice.infrastructure.web.controller.webform;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.PublicContact;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.Salutation;

public class WebFormPublicContactDto {

    @NotNull
    private Salutation salutation;

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    private String phone;

    private String email;

    public Salutation getSalutation() {
        return salutation;
    }

    public WebFormPublicContactDto setSalutation(Salutation salutation) {
        this.salutation = salutation;
        return this;
    }

    public String getFirstName() {
        return firstName;
    }

    public WebFormPublicContactDto setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String getLastName() {
        return lastName;
    }

    public WebFormPublicContactDto setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public String getPhone() {
        return phone;
    }

    public WebFormPublicContactDto setPhone(String phone) {
        this.phone = phone;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public WebFormPublicContactDto setEmail(String email) {
        this.email = email;
        return this;
    }

    public static WebFormPublicContactDto toDto(PublicContact publicContact) {
        if (publicContact == null) {
            return null;
        }
        return new WebFormPublicContactDto()
                .setSalutation(publicContact.getSalutation())
                .setFirstName(publicContact.getFirstName())
                .setLastName(publicContact.getLastName())
                .setPhone(publicContact.getPhone())
                .setEmail(publicContact.getEmail());
    }

}
