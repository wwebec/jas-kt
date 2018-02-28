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
    private String zipCode;
    private String city;
    private String countryIsoCode;
    private String postOfficeBoxNumber;
    private String postOfficeBoxZipCode;
    private String postOfficeBoxCity;
    private String phone;
    private String email;
    private String website;

    protected Company() {
        // For reflection libs
    }

    private Company(Builder builder) {
        this.name = Condition.notBlank(builder.name);
        this.street = builder.street;
        this.houseNumber = builder.houseNumber;
        this.zipCode = Condition.notBlank(builder.zipCode);
        this.city = Condition.notBlank(builder.city);
        this.countryIsoCode = Condition.notBlank(builder.countryIsoCode);
        this.postOfficeBoxNumber = builder.postOfficeBoxNumber;
        this.postOfficeBoxZipCode = builder.postOfficeBoxZipCode;
        this.postOfficeBoxCity = builder.postOfficeBoxCity;
        this.phone = builder.phone;
        this.email = builder.email;
        this.website = builder.website;
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

    public String getZipCode() {
        return zipCode;
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

    public String getPostOfficeBoxZipCode() {
        return postOfficeBoxZipCode;
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

    @Override
    public boolean sameValueObjectAs(Company other) {
        return equals(other);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Company company = (Company) o;
        return Objects.equals(name, company.name) &&
                Objects.equals(street, company.street) &&
                Objects.equals(houseNumber, company.houseNumber) &&
                Objects.equals(zipCode, company.zipCode) &&
                Objects.equals(city, company.city) &&
                Objects.equals(countryIsoCode, company.countryIsoCode) &&
                Objects.equals(postOfficeBoxNumber, company.postOfficeBoxNumber) &&
                Objects.equals(postOfficeBoxZipCode, company.postOfficeBoxZipCode) &&
                Objects.equals(postOfficeBoxCity, company.postOfficeBoxCity) &&
                Objects.equals(phone, company.phone) &&
                Objects.equals(email, company.email) &&
                Objects.equals(website, company.website);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, street, houseNumber, zipCode, city, countryIsoCode, postOfficeBoxNumber, postOfficeBoxZipCode, postOfficeBoxCity, phone, email, website);
    }

    public static class Builder<T> {
        private T parentBuilder;
        private String name;
        private String street;
        private String houseNumber;
        private String zipCode;
        private String city;
        private String countryIsoCode;
        private String postOfficeBoxNumber;
        private String postOfficeBoxZipCode;
        private String postOfficeBoxCity;
        private String phone;
        private String email;
        private String website;

        public Builder(T parentBuilder) {
            this.parentBuilder = parentBuilder;
        }

        public Builder() {
        }

        public Company build() {
            return new Company(this);
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

        public Builder<T> setZipCode(String zipCode) {
            this.zipCode = zipCode;
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

        public Builder<T> setPostOfficeBoxZipCode(String postOfficeBoxZipCode) {
            this.postOfficeBoxZipCode = postOfficeBoxZipCode;
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
    }
}
