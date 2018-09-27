package ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto;

import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.Contact;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.Salutation;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.utils.LanguageIsoCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class ContactDto {

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

    @NotBlank
    @LanguageIsoCode
    private String languageIsoCode;

    public Salutation getSalutation() {
        return salutation;
    }

    public ContactDto setSalutation(Salutation salutation) {
        this.salutation = salutation;
        return this;
    }

    public String getFirstName() {
        return firstName;
    }

    public ContactDto setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String getLastName() {
        return lastName;
    }

    public ContactDto setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public String getPhone() {
        return phone;
    }

    public ContactDto setPhone(String phone) {
        this.phone = phone;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public ContactDto setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getLanguageIsoCode() {
        return languageIsoCode;
    }

    public ContactDto setLanguageIsoCode(String languageIsoCode) {
        this.languageIsoCode = languageIsoCode;
        return this;
    }

    public static ContactDto toDto(Contact contact) {
        if (contact == null) {
            return null;
        }
        return new ContactDto()
                .setSalutation(contact.getSalutation())
                .setFirstName(contact.getFirstName())
                .setLastName(contact.getLastName())
                .setPhone(contact.getPhone())
                .setEmail(contact.getEmail())
                .setLanguageIsoCode(contact.getLanguage().getLanguage());
    }
}
