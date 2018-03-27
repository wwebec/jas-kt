package ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement;

import ch.admin.seco.jobs.services.jobadservice.core.conditions.Condition;
import ch.admin.seco.jobs.services.jobadservice.core.domain.ValueObject;

import javax.persistence.*;
import javax.validation.Valid;
import java.util.List;
import java.util.Objects;

@Embeddable
public class JobContent implements ValueObject<JobContent> {
    private String externalUrl;

    @ElementCollection
    @CollectionTable(name = "JOB_CONTENT_DESCRIPTION", joinColumns = @JoinColumn(name = "JOB_ADVERTISEMENT_ID"))
    @Valid
    private List<JobDescription> jobDescriptions;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "name", column = @Column(name = "COMPANY_NAME")),
            @AttributeOverride(name = "street", column = @Column(name = "COMPANY_STREET")),
            @AttributeOverride(name = "houseNumber", column = @Column(name = "COMPANY_HOUSE_NUMBER")),
            @AttributeOverride(name = "postalCode", column = @Column(name = "COMPANY_POSTAL_CODE")),
            @AttributeOverride(name = "city", column = @Column(name = "COMPANY_CITY")),
            @AttributeOverride(name = "countryIsoCode", column = @Column(name = "COMPANY_COUNTRY_ISO_CODE")),
            @AttributeOverride(name = "postOfficeBoxNumber", column = @Column(name = "COMPANY_POST_OFFICE_BOX_NUMBER")),
            @AttributeOverride(name = "postOfficeBoxPostalCode", column = @Column(name = "COMPANY_POST_OFFICE_BOX_POSTAL_CODE")),
            @AttributeOverride(name = "postOfficeBoxCity", column = @Column(name = "COMPANY_POST_OFFICE_BOX_CITY")),
            @AttributeOverride(name = "phone", column = @Column(name = "COMPANY_PHONE")),
            @AttributeOverride(name = "email", column = @Column(name = "COMPANY_EMAIL")),
            @AttributeOverride(name = "website", column = @Column(name = "COMPANY_WEBSITE")),
            @AttributeOverride(name = "surrogate", column = @Column(name = "COMPANY_SURROGATE"))
    })
    @Valid
    private Company company;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "name", column = @Column(name = "EMPLOYER_NAME")),
            @AttributeOverride(name = "street", column = @Column(name = "EMPLOYER_STREET")),
            @AttributeOverride(name = "houseNumber", column = @Column(name = "EMPLOYER_HOUSE_NUMBER")),
            @AttributeOverride(name = "postalCode", column = @Column(name = "EMPLOYER_POSTAL_CODE")),
            @AttributeOverride(name = "city", column = @Column(name = "EMPLOYER_CITY")),
            @AttributeOverride(name = "countryIsoCode", column = @Column(name = "EMPLOYER_COUNTRY_ISO_CODE")),
    })
    @Valid
    private Employer employer;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "startDate", column = @Column(name = "EMPLOYMENT_START_DATE")),
            @AttributeOverride(name = "endDate", column = @Column(name = "EMPLOYMENT_END_DATE")),
            @AttributeOverride(name = "durationInDays", column = @Column(name = "EMPLOYMENT_DURATION_IN_DAYS")),
            @AttributeOverride(name = "immediately", column = @Column(name = "EMPLOYMENT_IMMEDIATELY")),
            @AttributeOverride(name = "permanent", column = @Column(name = "EMPLOYMENT_PERMANENT")),
            @AttributeOverride(name = "workloadPercentageMin", column = @Column(name = "EMPLOYMENT_WORKLOAD_PERCENTAGE_MIN")),
            @AttributeOverride(name = "workloadPercentageMax", column = @Column(name = "EMPLOYMENT_WORKLOAD_PERCENTAGE_MAX"))
    })
    @Valid
    private Employment employment;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "remarks", column = @Column(name = "LOCATION_REMARKS")),
            @AttributeOverride(name = "city", column = @Column(name = "LOCATION_CITY")),
            @AttributeOverride(name = "postalCode", column = @Column(name = "LOCATION_POSTAL_CODE")),
            @AttributeOverride(name = "communalCode", column = @Column(name = "LOCATION_COMMUNAL_CODE")),
            @AttributeOverride(name = "regionCode", column = @Column(name = "LOCATION_REGION_CODE")),
            @AttributeOverride(name = "cantonCode", column = @Column(name = "LOCATION_CANTON_CODE")),
            @AttributeOverride(name = "countryIsoCode", column = @Column(name = "LOCATION_COUNTRY_ISO_CODE")),
            @AttributeOverride(name = "coordinates.longitude", column = @Column(name = "LOCATION_LONGITUDE")),
            @AttributeOverride(name = "coordinates.latitude", column = @Column(name = "LOCATION_LATITUDE"))
    })
    @Valid
    private Location location;

    @ElementCollection
    @CollectionTable(name = "JOB_CONTENT_OCCUPATION", joinColumns = @JoinColumn(name = "JOB_ADVERTISEMENT_ID"))
    @Valid
    private List<Occupation> occupations;

    @ElementCollection
    @CollectionTable(name = "JOB_CONTENT_LANGUAGE_SKILL", joinColumns = @JoinColumn(name = "JOB_ADVERTISEMENT_ID"))
    @Valid
    private List<LanguageSkill> languageSkills;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "mailAddress", column = @Column(name = "APPLY_CHANNEL_MAIL_ADDRESS")),
            @AttributeOverride(name = "emailAddress", column = @Column(name = "APPLY_CHANNEL_EMAIL_ADDRESS")),
            @AttributeOverride(name = "phoneNumber", column = @Column(name = "APPLY_CHANNEL_PHONE_NUMBER")),
            @AttributeOverride(name = "formUrl", column = @Column(name = "APPLY_CHANNEL_FORM_URL")),
            @AttributeOverride(name = "additionalInfo", column = @Column(name = "APPLY_CHANNEL_ADDITIONAL_INFO"))
    })
    @Valid
    private ApplyChannel applyChannel;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "salutation", column = @Column(name = "PUBLIC_CONTACT_SALUTATION")),
            @AttributeOverride(name = "firstName", column = @Column(name = "PUBLIC_CONTACT_FIRST_NAME")),
            @AttributeOverride(name = "lastName", column = @Column(name = "PUBLIC_CONTACT_LAST_NAME")),
            @AttributeOverride(name = "phone", column = @Column(name = "PUBLIC_CONTACT_PHONE")),
            @AttributeOverride(name = "email", column = @Column(name = "PUBLIC_CONTACT_EMAIL"))
    })
    @Valid
    private PublicContact publicContact;


    protected JobContent() {
        // For reflection libs
    }

    public JobContent(Builder builder) {
        this.externalUrl = builder.externalUrl;
        this.company = Condition.notNull(builder.company, "Company can't be null");
        this.employer = builder.employer;
        this.jobDescriptions = Condition.notEmpty(builder.jobDescriptions, "Job descriptions can't be null or empty");
        this.languageSkills = builder.languageSkills;
        this.employment = Condition.notNull(builder.employment, "Employment can't be null");
        this.publicContact = Condition.notNull(builder.publicContact, "Public contact can't be null");
        this.applyChannel = builder.applyChannel;
        this.location = Condition.notNull(builder.location, "Location can't be null");
        this.occupations = Condition.notEmpty(builder.occupations, "Occupations can't be null or empty");
    }

    public String getExternalUrl() {
        return externalUrl;
    }

    public void setExternalUrl(String externalUrl) {
        this.externalUrl = externalUrl;
    }

    public List<JobDescription> getJobDescriptions() {
        return jobDescriptions;
    }

    public void setJobDescriptions(List<JobDescription> jobDescriptions) {
        this.jobDescriptions = jobDescriptions;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public Employer getEmployer() {
        return employer;
    }

    public void setEmployer(Employer employer) {
        this.employer = employer;
    }

    public Employment getEmployment() {
        return employment;
    }

    public void setEmployment(Employment employment) {
        this.employment = employment;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public List<Occupation> getOccupations() {
        return occupations;
    }

    public void setOccupations(List<Occupation> occupations) {
        this.occupations = occupations;
    }

    public List<LanguageSkill> getLanguageSkills() {
        return languageSkills;
    }

    public void setLanguageSkills(List<LanguageSkill> languageSkills) {
        this.languageSkills = languageSkills;
    }

    public ApplyChannel getApplyChannel() {
        return applyChannel;
    }

    public void setApplyChannel(ApplyChannel applyChannel) {
        this.applyChannel = applyChannel;
    }

    public PublicContact getPublicContact() {
        return publicContact;
    }

    public void setPublicContact(PublicContact publicContact) {
        this.publicContact = publicContact;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JobContent that = (JobContent) o;
        return Objects.equals(externalUrl, that.externalUrl) &&
                Objects.equals(jobDescriptions, that.jobDescriptions) &&
                Objects.equals(company, that.company) &&
                Objects.equals(employer, that.employer) &&
                Objects.equals(employment, that.employment) &&
                Objects.equals(location, that.location) &&
                Objects.equals(occupations, that.occupations) &&
                Objects.equals(languageSkills, that.languageSkills) &&
                Objects.equals(applyChannel, that.applyChannel) &&
                Objects.equals(publicContact, that.publicContact);
    }

    @Override
    public int hashCode() {
        return Objects.hash(externalUrl, jobDescriptions, company, employer, employment, location, occupations, languageSkills, applyChannel, publicContact);
    }

    @Override
    public String toString() {
        return "JobContent{" +
                "externalwithUrl='" + externalUrl + '\'' +
                ", jobDescriptions=" + jobDescriptions +
                ", company=" + company +
                ", employer=" + employer +
                ", employment=" + employment +
                ", location=" + location +
                ", occupations=" + occupations +
                ", languageSkills=" + languageSkills +
                ", applyChannel=" + applyChannel +
                ", publicContact=" + publicContact +
                '}';
    }


    public static final class Builder<T> {
        private T parentBuilder;
        private String externalUrl;
        private List<JobDescription> jobDescriptions;
        private Company company;
        private Employer employer;
        private Employment employment;
        private Location location;
        private List<Occupation> occupations;
        private List<LanguageSkill> languageSkills;
        private ApplyChannel applyChannel;
        private PublicContact publicContact;

        public Builder() {
        }

        public Builder(T parentBuilder) {
            this.parentBuilder = parentBuilder;
        }

        public T end() {
            return Condition.notNull(parentBuilder, "No parentBuilder has been set");
        }

        public Builder<T> setExternalUrl(String externalUrl) {
            this.externalUrl = externalUrl;
            return this;
        }

        public Builder<T> setJobDescriptions(List<JobDescription> jobDescriptions) {
            this.jobDescriptions = jobDescriptions;
            return this;
        }

        public Builder<T> setCompany(Company company) {
            this.company = company;
            return this;
        }

        public Builder<T> setEmployer(Employer employer) {
            this.employer = employer;
            return this;
        }

        public Builder<T> setEmployment(Employment employment) {
            this.employment = employment;
            return this;
        }

        public Builder<T> setLocation(Location location) {
            this.location = location;
            return this;
        }

        public Builder<T> setOccupations(List<Occupation> occupations) {
            this.occupations = occupations;
            return this;
        }

        public Builder<T> setLanguageSkills(List<LanguageSkill> languageSkills) {
            this.languageSkills = languageSkills;
            return this;
        }

        public Builder<T> setApplyChannel(ApplyChannel applyChannel) {
            this.applyChannel = applyChannel;
            return this;
        }

        public Builder<T> setPublicContact(PublicContact publicContact) {
            this.publicContact = publicContact;
            return this;
        }

        public JobContent build() {
            return new JobContent(this);
        }
    }
}
