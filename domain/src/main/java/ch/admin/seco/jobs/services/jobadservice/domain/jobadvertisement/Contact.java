package ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement;

import ch.admin.seco.jobs.services.jobadservice.core.domain.ValueObject;

import java.util.Objects;

public class Contact implements ValueObject<Contact> {

    private Salutation salutation;
    private String firstName;
    private String lastName;
    private String phone;
    private String email;

    protected Contact() {
        // For reflection libs
    }

    public Contact(Salutation salutation, String firstName, String lastName, String phone, String email) {
        this.salutation = salutation;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.email = email;
    }

    public Salutation getSalutation() {
        return salutation;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public boolean sameValueObjectAs(Contact other) {
        return equals(other);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Contact contact = (Contact) o;
        return salutation == contact.salutation &&
                Objects.equals(firstName, contact.firstName) &&
                Objects.equals(lastName, contact.lastName) &&
                Objects.equals(phone, contact.phone) &&
                Objects.equals(email, contact.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(salutation, firstName, lastName, phone, email);
    }
}
