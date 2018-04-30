package ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement;

import ch.admin.seco.jobs.services.jobadservice.core.conditions.Condition;
import ch.admin.seco.jobs.services.jobadservice.core.domain.ValueObject;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
@Access(AccessType.FIELD)
public class Company implements ValueObject<Company> {

    private String name;
    private String street;
    private String houseNumber;
    private String postalCode;
    private String city;
    private String countryIsoCode;
    private String postOfficeBoxNumber;
    private String postOfficeBoxPostalCode;
    private String postOfficeBoxCity;
    private String phone;
    private String email;
    private String website;
    private boolean surrogate;

    protected Company() {
        // For reflection libs
    }

    private Company(Builder builder) {
        this.name = Condition.notBlank(builder.name, "Name of a company can't be null");
        this.street = builder.street;
        this.houseNumber = builder.houseNumber;
        this.postalCode = builder.postalCode;
        this.city = builder.city;
        // FIXME Add constrains when legacy API is removed!
//        this.postalCode = Condition.notBlank(builder.postalCode, "Postal code of a company can't be null");
//        this.city = Condition.notBlank(builder.city, "City of a company can't be null");
        this.countryIsoCode = Condition.notBlank(builder.countryIsoCode, "Country of a company can't be null");
        this.postOfficeBoxNumber = builder.postOfficeBoxNumber;
        this.postOfficeBoxPostalCode = builder.postOfficeBoxPostalCode;
        this.postOfficeBoxCity = builder.postOfficeBoxCity;
        this.phone = builder.phone;
        this.email = builder.email;
        this.website = builder.website;
        this.surrogate = builder.surrogate;
    }

    public String getName() {
        return name;
    }

    public String getStreet() {
        return street;
    }

    public String getHouseNumber() {
        return houseNumber;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String getCity() {
        return city;
    }

    public String getCountryIsoCode() {
        return countryIsoCode;
    }

    public String getPostOfficeBoxNumber() {
        return postOfficeBoxNumber;
    }

    public String getPostOfficeBoxPostalCode() {
        return postOfficeBoxPostalCode;
    }

    public String getPostOfficeBoxCity() {
        return postOfficeBoxCity;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public String getWebsite() {
        return website;
    }

    public boolean isSurrogate() {
        return surrogate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Company company = (Company) o;
        return Objects.equals(name, company.name) &&
                Objects.equals(street, company.street) &&
                Objects.equals(houseNumber, company.houseNumber) &&
                Objects.equals(postalCode, company.postalCode) &&
                Objects.equals(city, company.city) &&
                Objects.equals(countryIsoCode, company.countryIsoCode) &&
                Objects.equals(postOfficeBoxNumber, company.postOfficeBoxNumber) &&
                Objects.equals(postOfficeBoxPostalCode, company.postOfficeBoxPostalCode) &&
                Objects.equals(postOfficeBoxCity, company.postOfficeBoxCity) &&
                Objects.equals(phone, company.phone) &&
                Objects.equals(email, company.email) &&
                Objects.equals(website, company.website) &&
                Objects.equals(surrogate, company.surrogate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, street, houseNumber, postalCode, city, countryIsoCode, postOfficeBoxNumber, postOfficeBoxPostalCode, postOfficeBoxCity, phone, email, website, surrogate);
    }

    @Override
    public String toString() {
        return "Company{" +
                "name='" + name + '\'' +
                ", street='" + street + '\'' +
                ", houseNumber='" + houseNumber + '\'' +
                ", postalCode='" + postalCode + '\'' +
                ", city='" + city + '\'' +
                ", countryIsoCode='" + countryIsoCode + '\'' +
                ", postOfficeBoxNumber='" + postOfficeBoxNumber + '\'' +
                ", postOfficeBoxPostalCode='" + postOfficeBoxPostalCode + '\'' +
                ", postOfficeBoxCity='" + postOfficeBoxCity + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", website='" + website + '\'' +
                ", surrogate='" + surrogate + '\'' +
                '}';
    }

    public static class Builder<T> {
        private T parentBuilder;
        private String name;
        private String street;
        private String houseNumber;
        private String postalCode;
        private String city;
        private String countryIsoCode;
        private String postOfficeBoxNumber;
        private String postOfficeBoxPostalCode;
        private String postOfficeBoxCity;
        private String phone;
        private String email;
        private String website;
        private boolean surrogate;

        public Builder(T parentBuilder) {
            this.parentBuilder = parentBuilder;
        }

        public Builder() {
        }

        public Company build() {
            return new Company(this);
        }

        public T end() {
            return Condition.notNull(parentBuilder, "No parentBuilder has been set");
        }

        public Builder<T> setName(String name) {
            this.name = name;
            return this;
        }

        public Builder<T> setStreet(String street) {
            this.street = street;
            return this;
        }

        public Builder<T> setHouseNumber(String houseNumber) {
            this.houseNumber = houseNumber;
            return this;
        }

        public Builder<T> setPostalCode(String postalCode) {
            this.postalCode = postalCode;
            return this;
        }

        public Builder<T> setCity(String city) {
            this.city = city;
            return this;
        }

        public Builder<T> setCountryIsoCode(String countryIsoCode) {
            this.countryIsoCode = countryIsoCode;
            return this;
        }

        public Builder<T> setPostOfficeBoxNumber(String postOfficeBoxNumber) {
            this.postOfficeBoxNumber = postOfficeBoxNumber;
            return this;
        }

        public Builder<T> setPostOfficeBoxPostalCode(String postOfficeBoxPostalCode) {
            this.postOfficeBoxPostalCode = postOfficeBoxPostalCode;
            return this;
        }

        public Builder<T> setPostOfficeBoxCity(String postOfficeBoxCity) {
            this.postOfficeBoxCity = postOfficeBoxCity;
            return this;
        }

        public Builder<T> setPhone(String phone) {
            this.phone = phone;
            return this;
        }

        public Builder<T> setEmail(String email) {
            this.email = email;
            return this;
        }

        public Builder<T> setWebsite(String website) {
            this.website = website;
            return this;
        }

        public Builder<T> setSurrogate(boolean surrogate) {
            this.surrogate = surrogate;
            return this;
        }
    }
}
