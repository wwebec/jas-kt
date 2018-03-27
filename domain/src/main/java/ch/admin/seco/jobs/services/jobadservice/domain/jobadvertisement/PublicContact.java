package ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement;

import ch.admin.seco.jobs.services.jobadservice.core.conditions.Condition;
import ch.admin.seco.jobs.services.jobadservice.core.domain.ValueObject;

import javax.persistence.*;
import java.util.Locale;
import java.util.Objects;

@Embeddable
@Access(AccessType.FIELD)
public class PublicContact implements ValueObject<PublicContact> {

    @Enumerated(EnumType.STRING)
    private Salutation salutation;

    private String firstName;

    private String lastName;

    private String phone;

    private String email;

    protected PublicContact() {
        // For reflection libs
    }

    public PublicContact(Builder builder) {
        this.salutation = Condition.notNull(builder.salutation, "Salutation can't be null");
        this.firstName = Condition.notBlank(builder.firstName, "First name can't be blank");
        this.lastName = Condition.notBlank(builder.lastName, "Last name can't be blank");
        this.phone = Condition.notBlank(builder.phone, "Phone can't be blank");
        this.email = Condition.notBlank(builder.email, "Email can't be blank");
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PublicContact contact = (PublicContact) o;
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

    @Override
    public String toString() {
        return "PublicContact{" +
                "salutation=" + salutation +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
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

        public PublicContact build() {
            return new PublicContact(this);
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
    }
}
