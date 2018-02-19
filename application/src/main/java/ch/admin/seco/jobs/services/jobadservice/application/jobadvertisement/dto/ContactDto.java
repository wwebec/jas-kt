package ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto;

import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.Contact;

import javax.validation.constraints.NotNull;

public class ContactDto {

    @NotNull
    private String salutation;
    @NotNull
    private String firstName;
    @NotNull
    private String lastName;
    @NotNull
    private String phone;
    @NotNull
    private String email;

    protected ContactDto() {
        // For reflection libs
    }

    public ContactDto(String salutation, String firstName, String lastName, String phone, String email) {
        this.salutation = salutation;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.email = email;
    }

    public String getSalutation() {
        return salutation;
    }

    public void setSalutation(String salutation) {
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

    public static ContactDto toDto(Contact contact) {
        ContactDto contactDto = new ContactDto();
        contactDto.setSalutation(contact.getSalutation().toString());
        contactDto.setFirstName(contact.getFirstName());
        contactDto.setLastName(contact.getLastName());
        contactDto.setPhone(contact.getPhone());
        contactDto.setEmail(contact.getEmail());
        return contactDto;
    }
}
