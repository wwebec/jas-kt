package ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement;

import ch.admin.seco.jobs.services.jobadservice.core.domain.ValueObject;

import javax.persistence.*;
import javax.validation.Valid;
import java.util.List;

@Entity
public class JobContent implements ValueObject<JobContent> {

    @EmbeddedId
    @AttributeOverride(name = "value", column = @Column(name = "ID"))
    @Valid
    private JobContentId id;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "JOB_ADVERTISEMENT_ID"))
    private JobAdvertisementId jobAdvertisementId;

    private String externalUrl;

    @ElementCollection
    @CollectionTable(name = "JOB_CONTENT_OCCUPATION", joinColumns = @JoinColumn(name = "JOB_CONTENT_ID"))
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
            @AttributeOverride(name = "website", column = @Column(name = "COMPANY_WEBSITE"))
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
    @CollectionTable(name = "JOB_CONTENT_OCCUPATION", joinColumns = @JoinColumn(name = "JOB_CONTENT_ID"))
    @Valid
    private List<Occupation> occupations;

    @ElementCollection
    @CollectionTable(name = "JOB_CONTENT_LANGUAGE_SKILL", joinColumns = @JoinColumn(name = "JOB_CONTENT_ID"))
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

}
