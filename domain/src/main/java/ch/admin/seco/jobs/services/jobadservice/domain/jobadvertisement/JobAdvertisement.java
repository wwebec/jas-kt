package ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement;

import ch.admin.seco.jobs.services.jobadservice.core.conditions.Condition;
import ch.admin.seco.jobs.services.jobadservice.core.domain.Aggregate;

import javax.persistence.*;
import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;

@Entity
public class JobAdvertisement implements Aggregate<JobAdvertisement, JobAdvertisementId> {

    @EmbeddedId
    @AttributeOverride(name = "value", column = @Column(name = "ID"))
    @Valid
    private JobAdvertisementId id;

    private String stellennummerAvam;

    private String fingerprint;

    private String sourceSystem;

    private String sourceEntryId;

    private String externalUrl;

    @Enumerated(EnumType.STRING)
    private JobAdvertisementStatus status;

    private LocalDate publicationStartDate;

    private LocalDate publicationEndDate;

    private String title;

    private String description;

    private LocalDate employmentStartDate;

    private LocalDate employmentEndDate;

    private Integer durationInDays;

    private Boolean immediately;

    private Boolean permanent;

    private int workloadPercentageMin;

    private int workloadPercentageMax;

    private Integer numberOfJobs; // TODO check if used anywhere outside JobRoom

    private Boolean accessibly; // TODO Add this in JobRoom (Improvement-Issue)

    private Boolean jobSharing; // TODO check if used anywhere outside JobRoom

    private Boolean hasPersonalVehicle; // TODO check if used anywhere outside JobRoom

    private String jobCenterCode;

    private String drivingLicenseLevel;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "mailAddress", column = @Column(name = "APPLY_CHANNEL_MAIL_ADDRESS")),
            @AttributeOverride(name = "emailAddress", column = @Column(name = "APPLY_CHANNEL_EMAIL_ADDRESS")),
            @AttributeOverride(name = "phoneNumber", column = @Column(name = "APPLY_CHANNEL_PHONE_NUMBER")),
            @AttributeOverride(name = "applicationUrl", column = @Column(name = "APPLY_CHANNEL_FORM_URL")),
            @AttributeOverride(name = "additionalInfo", column = @Column(name = "APPLY_CHANNEL_ADDITIONAL_INFO"))
    })
    @Valid
    private ApplyChannel applyChannel;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "name", column = @Column(name = "COMPANY_NAME")),
            @AttributeOverride(name = "street", column = @Column(name = "COMPANY_STREET")),
            @AttributeOverride(name = "houseNumber", column = @Column(name = "COMPANY_HOUSE_NUMBER")),
            @AttributeOverride(name = "zipCode", column = @Column(name = "COMPANY_ZIP_CODE")),
            @AttributeOverride(name = "city", column = @Column(name = "COMPANY_CITY")),
            @AttributeOverride(name = "countryCode", column = @Column(name = "COMPANY_COUNTRY_CODE")),
            @AttributeOverride(name = "postOfficeBoxNumber", column = @Column(name = "COMPANY_POST_OFFICE_BOX_NUMBER")),
            @AttributeOverride(name = "postOfficeBoxZipCode", column = @Column(name = "COMPANY_POST_OFFICE_BOX_ZIP_CODE")),
            @AttributeOverride(name = "postOfficeBoxCity", column = @Column(name = "COMPANY_POST_OFFICE_BOX_CITY")),
            @AttributeOverride(name = "phone", column = @Column(name = "COMPANY_PHONE")),
            @AttributeOverride(name = "email", column = @Column(name = "COMPANY_EMAIL")),
            @AttributeOverride(name = "website", column = @Column(name = "COMPANY_WEBSITE"))
    })
    @Valid
    private Company company;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "salutation", column = @Column(name = "CONTACT_SALUTATION")),
            @AttributeOverride(name = "firstName", column = @Column(name = "CONTACT_FIRST_NAME")),
            @AttributeOverride(name = "lastName", column = @Column(name = "CONTACT_LAST_NAME")),
            @AttributeOverride(name = "phone", column = @Column(name = "CONTACT_PHONE")),
            @AttributeOverride(name = "email", column = @Column(name = "CONTACT_EMAIL"))
    })
    @Valid
    private Contact contact;

    @ElementCollection
    @CollectionTable(name = "JOB_ADVERTISEMENT_LOCALITY", joinColumns = @JoinColumn(name = "JOB_ADVERTISEMENT_ID"))
    @Valid
    private List<Locality> localities;

    @ElementCollection
    @CollectionTable(name = "JOB_ADVERTISEMENT_OCCUPATION", joinColumns = @JoinColumn(name = "JOB_ADVERTISEMENT_ID"))
    @Valid
    private List<Occupation> occupations;

    private String educationCode;

    @ElementCollection
    @CollectionTable(name = "JOB_ADVERTISEMENT_LANGUAGES_KILL", joinColumns = @JoinColumn(name = "JOB_ADVERTISEMENT_ID"))
    @Valid
    private List<LanguageSkill> languageSkills;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "JOB_ADVERTISEMENT_PROFESSION_CODES", joinColumns = @JoinColumn(name = "JOB_ADVERTISEMENT_ID"))
    private List<String> professionCodes;

    protected JobAdvertisement() {
        // For reflection libs
    }

    public JobAdvertisement(JobAdvertisementId id, JobAdvertisementStatus status, String title, String description) {
        this.id = Condition.notNull(id);
        this.status = Condition.notNull(status);
        this.title = Condition.notBlank(title);
        this.description = Condition.notBlank(description);
    }

    public JobAdvertisement(JobAdvertisementId id, String stellennummerAvam, String fingerprint, String sourceSystem, String sourceEntryId, String externalUrl, JobAdvertisementStatus status, LocalDate publicationStartDate, LocalDate publicationEndDate, String title, String description, LocalDate employmentStartDate, LocalDate employmentEndDate, Integer durationInDays, Boolean immediately, Boolean permanent, int workloadPercentageMin, int workloadPercentageMax, Integer numberOfJobs, Boolean accessibly, Boolean jobSharing, Boolean hasPersonalVehicle, String jobCenterCode, String drivingLicenseLevel, ApplyChannel applyChannel, Company company, Contact contact, List<Locality> localities, List<Occupation> occupations, String educationCode, List<LanguageSkill> languageSkills, List<String> professionCodes) {
        this(id, status, title, description);
        this.stellennummerAvam = stellennummerAvam;
        this.fingerprint = fingerprint;
        this.sourceSystem = sourceSystem;
        this.sourceEntryId = sourceEntryId;
        this.externalUrl = externalUrl;
        this.publicationStartDate = publicationStartDate;
        this.publicationEndDate = publicationEndDate;
        this.employmentStartDate = employmentStartDate;
        this.employmentEndDate = employmentEndDate;
        this.durationInDays = durationInDays;
        this.immediately = immediately;
        this.permanent = permanent;
        this.workloadPercentageMin = workloadPercentageMin;
        this.workloadPercentageMax = workloadPercentageMax;
        this.numberOfJobs = numberOfJobs;
        this.accessibly = accessibly;
        this.jobSharing = jobSharing;
        this.hasPersonalVehicle = hasPersonalVehicle;
        this.jobCenterCode = jobCenterCode;
        this.drivingLicenseLevel = drivingLicenseLevel;
        this.applyChannel = applyChannel;
        this.company = company;
        this.contact = contact;
        this.localities = localities;
        this.occupations = occupations;
        this.educationCode = educationCode;
        this.languageSkills = languageSkills;
        this.professionCodes = professionCodes;
    }

    @Override
    public boolean sameAggregateAs(JobAdvertisement other) {
        return (other != null) && id.sameValueObjectAs(other.id);
    }

    public JobAdvertisementId getId() {
        return id;
    }

    public String getStellennummerAvam() {
        return stellennummerAvam;
    }

    public String getFingerprint() {
        return fingerprint;
    }

    public String getSourceSystem() {
        return sourceSystem;
    }

    public String getSourceEntryId() {
        return sourceEntryId;
    }

    public String getExternalUrl() {
        return externalUrl;
    }

    public JobAdvertisementStatus getStatus() {
        return status;
    }

    public LocalDate getPublicationStartDate() {
        return publicationStartDate;
    }

    public LocalDate getPublicationEndDate() {
        return publicationEndDate;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public LocalDate getEmploymentStartDate() {
        return employmentStartDate;
    }

    public LocalDate getEmploymentEndDate() {
        return employmentEndDate;
    }

    public Integer getDurationInDays() {
        return durationInDays;
    }

    public Boolean getImmediately() {
        return immediately;
    }

    public Boolean getPermanent() {
        return permanent;
    }

    public int getWorkloadPercentageMin() {
        return workloadPercentageMin;
    }

    public int getWorkloadPercentageMax() {
        return workloadPercentageMax;
    }

    public Integer getNumberOfJobs() {
        return numberOfJobs;
    }

    public Boolean getAccessibly() {
        return accessibly;
    }

    public Boolean getJobSharing() {
        return jobSharing;
    }

    public Boolean getHasPersonalVehicle() {
        return hasPersonalVehicle;
    }

    public String getJobCenterCode() {
        return jobCenterCode;
    }

    public String getDrivingLicenseLevel() {
        return drivingLicenseLevel;
    }

    public ApplyChannel getApplyChannel() {
        return applyChannel;
    }

    public Company getCompany() {
        return company;
    }

    public Contact getContact() {
        return contact;
    }

    public List<Locality> getLocalities() {
        return localities;
    }

    public List<Occupation> getOccupations() {
        return occupations;
    }

    public String getEducationCode() {
        return educationCode;
    }

    public List<LanguageSkill> getLanguageSkills() {
        return languageSkills;
    }

    public List<String> getProfessionCodes() {
        return professionCodes;
    }

    public void updateEmployment(LocalDate employmentStartDate, LocalDate employmentEndDate, Integer durationInDays, Boolean immediately, Boolean permanent, int workloadPercentageMin, int workloadPercentageMax) {
        this.employmentStartDate = employmentStartDate;
        this.employmentEndDate = employmentEndDate;
        this.durationInDays = durationInDays;
        this.immediately = immediately;
        this.permanent = permanent;
        this.workloadPercentageMin = workloadPercentageMin;
        this.workloadPercentageMax = workloadPercentageMax;
    }

    public void updateApplyChannel(ApplyChannel applyChannel) {
        this.applyChannel = applyChannel;
    }

    public void updateCompany(Company company) {
        this.company = company;
    }

    public void updateContact(Contact contact) {
        this.contact = contact;
    }

    public void updateLocalities(List<Locality> localities) {
        this.localities = localities;
    }

    public void updateRequirements(List<Occupation> occupations, String educationId) {
        this.occupations = occupations;
        this.educationCode = educationId;
    }

    public void updateLanguageSkills(List<LanguageSkill> languageSkills) {
        this.languageSkills = languageSkills;
    }
}
