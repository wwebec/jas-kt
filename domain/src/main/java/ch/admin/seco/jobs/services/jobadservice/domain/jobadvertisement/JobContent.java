package ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.persistence.JoinColumn;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import ch.admin.seco.jobs.services.jobadservice.core.conditions.Condition;
import ch.admin.seco.jobs.services.jobadservice.core.domain.ValueObject;

@Embeddable
public class JobContent implements ValueObject<JobContent> {

    private String title;

    private String description;

    private String externalUrl;

    @Column(name = "X28_OCCUPATION_CODES")
    private String x28OccupationCodes;

    private String numberOfJobs;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "name", column = @Column(name = "DISPLAY_COMPANY_NAME")),
            @AttributeOverride(name = "street", column = @Column(name = "DISPLAY_COMPANY_STREET")),
            @AttributeOverride(name = "houseNumber", column = @Column(name = "DISPLAY_COMPANY_HOUSE_NUMBER")),
            @AttributeOverride(name = "postalCode", column = @Column(name = "DISPLAY_COMPANY_POSTAL_CODE")),
            @AttributeOverride(name = "city", column = @Column(name = "DISPLAY_COMPANY_CITY")),
            @AttributeOverride(name = "countryIsoCode", column = @Column(name = "DISPLAY_COMPANY_COUNTRY_ISO_CODE")),
            @AttributeOverride(name = "postOfficeBoxNumber", column = @Column(name = "DISPLAY_COMPANY_POST_OFFICE_BOX_NUMBER")),
            @AttributeOverride(name = "postOfficeBoxPostalCode", column = @Column(name = "DISPLAY_COMPANY_POST_OFFICE_BOX_POSTAL_CODE")),
            @AttributeOverride(name = "postOfficeBoxCity", column = @Column(name = "DISPLAY_COMPANY_POST_OFFICE_BOX_CITY")),
            @AttributeOverride(name = "phone", column = @Column(name = "DISPLAY_COMPANY_PHONE")),
            @AttributeOverride(name = "email", column = @Column(name = "DISPLAY_COMPANY_EMAIL")),
            @AttributeOverride(name = "website", column = @Column(name = "DISPLAY_COMPANY_WEBSITE")),
            @AttributeOverride(name = "surrogate", column = @Column(name = "DISPLAY_COMPANY_SURROGATE"))
    })
    @Valid
    private Company displayCompany;

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
    @NotNull
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
    @NotNull
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
    @CollectionTable(name = "JOB_ADVERTISEMENT_OCCUPATION", joinColumns = @JoinColumn(name = "JOB_ADVERTISEMENT_ID"))
    @Valid
    //todo: We already have jobAdvertisements in the DB with empty occupation list.
    // Because of this currently we can not allow this constraint, otherwise we can not modify the entity.
    //@NotEmpty
    private List<Occupation> occupations = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "JOB_ADVERTISEMENT_LANGUAGE_SKILL", joinColumns = @JoinColumn(name = "JOB_ADVERTISEMENT_ID"))
    @Valid
    private List<LanguageSkill> languageSkills = new ArrayList<>();

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
        this.setTitle(builder.title);
        this.setDescription(builder.description);
        this.externalUrl = builder.externalUrl;
        this.x28OccupationCodes = builder.x28OccupationCodes;
        this.numberOfJobs = builder.numberOfJobs;
        this.displayCompany = builder.displayCompany;
        this.setCompany(builder.company);
        this.employer = builder.employer;
        this.setLanguageSkills(builder.languageSkills);
        this.setEmployment(builder.employment);
        this.publicContact = builder.publicContact;
        this.applyChannel = builder.applyChannel;
        this.location = builder.location;
        this.setOccupations(builder.occupations);
    }

    public String getTitle() {
        return title;
    }

    void setTitle(String title) {
        Condition.notBlank(title, "Title can't be null or empty");
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    void setDescription(String description) {
        Condition.notBlank(description, "Description can't be null or empty");
        this.title = description;
    }

    public String getExternalUrl() {
        return externalUrl;
    }

    void setExternalUrl(String externalUrl) {
        this.externalUrl = externalUrl;
    }

    public String getX28OccupationCodes() {
        return x28OccupationCodes;
    }

    void setX28OccupationCodes(String x28OccupationCodes) {
        this.x28OccupationCodes = x28OccupationCodes;
    }

    public String getNumberOfJobs() {
        return numberOfJobs;
    }

    void setNumberOfJobs(String numberOfJobs) {
        this.numberOfJobs = numberOfJobs;
    }

    public Company getDisplayCompany() {
        return displayCompany;
    }

    void setDisplayCompany(Company displayCompany) {
        this.displayCompany = displayCompany;
    }

    public Company getCompany() {
        return company;
    }

    void setCompany(Company company) {
        this.company = Condition.notNull(company, "Company can't be null");
    }

    public Employer getEmployer() {
        return employer;
    }

    void setEmployer(Employer employer) {
        this.employer = employer;
    }

    public Employment getEmployment() {
        return employment;
    }

    void setEmployment(Employment employment) {
        this.employment = Condition.notNull(employment, "Employment can't be null");
    }

    public Location getLocation() {
        return location;
    }

    void setLocation(Location location) {
        this.location = location;
    }

    public List<Occupation> getOccupations() {
        return occupations;
    }

    void setOccupations(List<Occupation> occupations) {
        Condition.notEmpty(occupations, "Occupations can't be null or empty");
        this.occupations.clear();
        this.occupations.addAll(occupations);
    }

    public List<LanguageSkill> getLanguageSkills() {
        return languageSkills;
    }

    void setLanguageSkills(List<LanguageSkill> languageSkills) {
        this.languageSkills.clear();
        this.languageSkills.addAll(languageSkills);
    }

    public ApplyChannel getApplyChannel() {
        return applyChannel;
    }

    void setApplyChannel(ApplyChannel applyChannel) {
        this.applyChannel = applyChannel;
    }

    public PublicContact getPublicContact() {
        return publicContact;
    }

    void setPublicContact(PublicContact publicContact) {
        this.publicContact = publicContact;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (o == null || getClass() != o.getClass()) { return false; }
        JobContent that = (JobContent) o;
        return Objects.equals(title, that.title) &&
                Objects.equals(description, that.description) &&
                Objects.equals(externalUrl, that.externalUrl) &&
                Objects.equals(x28OccupationCodes, that.x28OccupationCodes) &&
                Objects.equals(numberOfJobs, that.numberOfJobs) &&
                Objects.equals(displayCompany, that.displayCompany) &&
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
        return Objects.hash(title, description, externalUrl, x28OccupationCodes, numberOfJobs, displayCompany, company, employer, employment, location, occupations, languageSkills, applyChannel, publicContact);
    }

    @Override
    public String toString() {
        return "JobContent{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", externalUrl='" + externalUrl + '\'' +
                ", x28OccupationCodes=" + x28OccupationCodes +
                ", numberOfJobs=" + numberOfJobs +
                ", displayCompany=" + displayCompany +
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


    public static final class Builder {

        private String title;
        private String description;
        private String externalUrl;
        private String x28OccupationCodes;
        private String numberOfJobs;
        private Company displayCompany;
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

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setDescription(String description) {
            this.description = description;
            return this;
        }

        public Builder setExternalUrl(String externalUrl) {
            this.externalUrl = externalUrl;
            return this;
        }

        public Builder setX28OccupationCodes(String x28OccupationCodes) {
            this.x28OccupationCodes = x28OccupationCodes;
            return this;
        }

        public Builder setNumberOfJobs(String numberOfJobs) {
            this.numberOfJobs = numberOfJobs;
            return this;
        }

        public Builder setDisplayCompany(Company displayCompany) {
            this.displayCompany = displayCompany;
            return this;
        }

        public Builder setCompany(Company company) {
            this.company = company;
            return this;
        }

        public Builder setEmployer(Employer employer) {
            this.employer = employer;
            return this;
        }

        public Builder setEmployment(Employment employment) {
            this.employment = employment;
            return this;
        }

        public Builder setLocation(Location location) {
            this.location = location;
            return this;
        }

        public Builder setOccupations(List<Occupation> occupations) {
            this.occupations = occupations;
            return this;
        }

        public Builder setLanguageSkills(List<LanguageSkill> languageSkills) {
            this.languageSkills = languageSkills;
            return this;
        }

        public Builder setApplyChannel(ApplyChannel applyChannel) {
            this.applyChannel = applyChannel;
            return this;
        }

        public Builder setPublicContact(PublicContact publicContact) {
            this.publicContact = publicContact;
            return this;
        }

        public JobContent build() {
            return new JobContent(this);
        }
    }
}
