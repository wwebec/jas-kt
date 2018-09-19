package ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.x28;

import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.ContactDto;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.PublicContactDto;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.Salutation;

public class X28ContactDto {

    private Salutation salutation;

    private String firstName;

    private String lastName;

    private String phone;

    private String email;

    private String languageIsoCode;

    protected X28ContactDto() {
        // For reflection libs
    }

    public X28ContactDto(Salutation salutation, String firstName, String lastName, String phone, String email, String languageIsoCode) {
        this.salutation = salutation;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.email = email;
        this.languageIsoCode = languageIsoCode;
    }

    ContactDto toContactDto() {
        return new ContactDto(this.salutation, this.firstName, this.lastName, this.phone, this.email, this.languageIsoCode);
    }

    PublicContactDto toPublicContactDto() {
        return new PublicContactDto(this.salutation, this.firstName, this.lastName, this.phone, this.email);
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLanguageIsoCode() {
        return languageIsoCode;
    }

    public void setLanguageIsoCode(String languageIsoCode) {
        this.languageIsoCode = languageIsoCode;
    }

}
