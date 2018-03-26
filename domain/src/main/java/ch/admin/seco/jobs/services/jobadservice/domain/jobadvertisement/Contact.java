package ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement;

import java.util.Locale;
import java.util.Objects;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import ch.admin.seco.jobs.services.jobadservice.core.conditions.Condition;
import ch.admin.seco.jobs.services.jobadservice.core.domain.ValueObject;

@Embeddable
@Access(AccessType.FIELD)
public class Contact implements ValueObject<Contact> {

    @Enumerated(EnumType.STRING)
    private Salutation salutation;

    private String firstName;

    private String lastName;

    private String phone;

    private String email;

    private Locale language;

    protected Contact() {
        // For reflection libs
    }

    public Contact(Builder builder) {
        this.salutation = Condition.notNull(builder.salutation, "Salutation can't be null");
        this.firstName = Condition.notBlank(builder.firstName, "First name can't be blank");
        this.lastName = Condition.notBlank(builder.lastName, "Last name can't be blank");
        this.phone = Condition.notBlank(builder.phone, "Phone can't be blank");
        this.email = Condition.notBlank(builder.email, "Email can't be blank");
        this.language = Condition.notNull(builder.language, "Language can't be null");
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

    public Locale getLanguage() {
        return language;
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
                Objects.equals(email, contact.email) &&
                Objects.equals(language, contact.language);
    }

    @Override
    public int hashCode() {
        return Objects.hash(salutation, firstName, lastName, phone, email, language);
    }

    @Override
    public String toString() {
        return "Contact{" +
                "salutation=" + salutation +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", language='" + language.getLanguage() + '\'' +
                '}';
    }

    public static final class Builder {
        private Salutation salutation;
        private String firstName;
        private String lastName;
        private String phone;
        private String email;
        private Locale language;

        public Builder() {
        }

        public Contact build() {
            return new Contact(this);
        }

        public Builder setSalutation(Salutation salutation) {
            this.salutation = salutation;
            return this;
        }

        public Builder setFirstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public Builder setLastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public Builder setPhone(String phone) {
            this.phone = phone;
            return this;
        }

        public Builder setEmail(String email) {
            this.email = email;
            return this;
        }

        public Builder setLanguage(Locale language) {
            this.language = language;
            return this;
        }
    }
}
