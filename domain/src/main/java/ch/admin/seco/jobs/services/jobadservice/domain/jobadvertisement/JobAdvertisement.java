package ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement;

import static ch.admin.seco.jobs.services.jobadservice.core.utils.CompareUtils.hasChanged;
import static ch.admin.seco.jobs.services.jobadservice.core.utils.CompareUtils.hasChangedContent;
import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisementUpdater.SECTION_APPLY_CHANNEL;
import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisementUpdater.SECTION_COMPANY;
import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisementUpdater.SECTION_CONTACT;
import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisementUpdater.SECTION_DRIVING_LICENSE_LEVEL;
import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisementUpdater.SECTION_EMPLOYMENT;
import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisementUpdater.SECTION_EURES;
import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisementUpdater.SECTION_EXTERNAL_URL;
import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisementUpdater.SECTION_FINGERPRINT;
import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisementUpdater.SECTION_JOB_CENTER_CODE;
import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisementUpdater.SECTION_LANGUAGE_SKILLS;
import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisementUpdater.SECTION_LOCALITY;
import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisementUpdater.SECTION_OCCUPATIONS;
import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisementUpdater.SECTION_PUBLICATION_DATES;
import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisementUpdater.SECTION_REPORTING_OBLIGATION;
import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisementUpdater.SECTION_SOURCE_ENTRY_ID;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.validation.Valid;

import ch.admin.seco.jobs.services.jobadservice.core.conditions.Condition;
import ch.admin.seco.jobs.services.jobadservice.core.domain.Aggregate;
import ch.admin.seco.jobs.services.jobadservice.core.domain.events.DomainEventPublisher;
import ch.admin.seco.jobs.services.jobadservice.core.time.TimeMachine;

@Entity
public class JobAdvertisement implements Aggregate<JobAdvertisement, JobAdvertisementId> {

    @EmbeddedId
    @AttributeOverride(name = "value", column = @Column(name = "ID"))
    @Valid
    private JobAdvertisementId id;

    private String stellennummerEgov;

    private String stellennummerAvam;

    private String fingerprint;

    @Enumerated(EnumType.STRING)
    private SourceSystem sourceSystem;

    private String sourceEntryId;

    private String externalUrl;

    @Enumerated(EnumType.STRING)
    private JobAdvertisementStatus status;

    private LocalDate ravRegistrationDate;

    private LocalDate approvalDate;

    private LocalDate rejectionDate;

    private String rejectionCode;

    private String rejectionReason;

    private LocalDate cancellationDate;

    private String cancellationCode;

    private boolean reportingObligation;

    private LocalDate reportingObligationEndDate;

    private LocalDate publicationStartDate;

    private LocalDate publicationEndDate;

    private boolean eures;

    private boolean euresAnonymous;

    private String title;

    private String description;

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

    private String jobCenterCode;

    private String drivingLicenseLevel;

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
            @AttributeOverride(name = "name", column = @Column(name = "COMPANY_NAME")),
            @AttributeOverride(name = "street", column = @Column(name = "COMPANY_STREET")),
            @AttributeOverride(name = "houseNumber", column = @Column(name = "COMPANY_HOUSE_NUMBER")),
            @AttributeOverride(name = "zipCode", column = @Column(name = "COMPANY_ZIP_CODE")),
            @AttributeOverride(name = "city", column = @Column(name = "COMPANY_CITY")),
            @AttributeOverride(name = "countryIsoCode", column = @Column(name = "COMPANY_COUNTRY_ISO_CODE")),
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

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "remarks", column = @Column(name = "LOCALITY_REMARKS")),
            @AttributeOverride(name = "city", column = @Column(name = "LOCALITY_CITY")),
            @AttributeOverride(name = "zipCode", column = @Column(name = "LOCALITY_ZIP_CODE")),
            @AttributeOverride(name = "communalCode", column = @Column(name = "LOCALITY_COMMUNAL_CODE")),
            @AttributeOverride(name = "regionCode", column = @Column(name = "LOCALITY_REGION_CODE")),
            @AttributeOverride(name = "cantonCode", column = @Column(name = "LOCALITY_CANTON_CODE")),
            @AttributeOverride(name = "countryIsoCode", column = @Column(name = "LOCALITY_COUNTRY_ISO_CODE")),
            @AttributeOverride(name = "location.longitude", column = @Column(name = "LOCALITY_LOCATION_LONGITUDE")),
            @AttributeOverride(name = "location.latitude", column = @Column(name = "LOCALITY_LOCATION_LATITUDE"))
    })
    @Valid
    private Locality locality;

    @ElementCollection
    @CollectionTable(name = "JOB_ADVERTISEMENT_OCCUPATION", joinColumns = @JoinColumn(name = "JOB_ADVERTISEMENT_ID"))
    @Valid
    private List<Occupation> occupations;

    @ElementCollection
    @CollectionTable(name = "JOB_ADVERTISEMENT_LANGUAGE_SKILL", joinColumns = @JoinColumn(name = "JOB_ADVERTISEMENT_ID"))
    @Valid
    private List<LanguageSkill> languageSkills;

    protected JobAdvertisement() {
        // For reflection libs
    }

    private JobAdvertisement(Builder builder) {
        this(builder.id, builder.sourceSystem, builder.status, builder.title, builder.description);

        this.stellennummerEgov = builder.stellennummerEgov;
        this.occupations = builder.occupations;
        this.eures = builder.eures;
        this.locality = builder.locality;
        this.publicationStartDate = builder.publicationStartDate;
        this.rejectionCode = builder.rejectionCode;
        this.approvalDate = builder.approvalDate;
        this.reportingObligationEndDate = builder.reportingObligationEndDate;
        this.jobCenterCode = builder.jobCenterCode;
        this.company = builder.company;
        this.employment = builder.employment;
        this.sourceEntryId = builder.sourceEntryId;
        this.rejectionDate = builder.rejectionDate;
        this.contact = builder.contact;
        this.reportingObligation = builder.reportingObligation;
        this.stellennummerAvam = builder.stellennummerAvam;
        this.publicationEndDate = builder.publicationEndDate;
        this.ravRegistrationDate = builder.ravRegistrationDate;
        this.euresAnonymous = builder.euresAnonymous;
        this.fingerprint = builder.fingerprint;
        this.cancellationCode = builder.cancellationCode;
        this.applyChannel = builder.applyChannel;
        this.languageSkills = builder.languageSkills;
        this.drivingLicenseLevel = builder.drivingLicenseLevel;
        this.cancellationDate = builder.cancellationDate;
        this.externalUrl = builder.externalUrl;
        this.rejectionReason = builder.rejectionReason;
    }

    public JobAdvertisement(JobAdvertisementId id, SourceSystem sourceSystem, JobAdvertisementStatus status, String title, String description) {
        this.id = Condition.notNull(id);
        this.sourceSystem = Condition.notNull(sourceSystem);
        this.status = Condition.notNull(status);
        this.title = Condition.notBlank(title);
        this.description = Condition.notBlank(description);
    }

    @Override
    public boolean sameAggregateAs(JobAdvertisement other) {
        return (other != null) && id.sameValueObjectAs(other.id);
    }

    public JobAdvertisementId getId() {
        return id;
    }

    public String getStellennummerEgov() {
        return stellennummerEgov;
    }

    public String getStellennummerAvam() {
        return stellennummerAvam;
    }

    public String getFingerprint() {
        return fingerprint;
    }

    public SourceSystem getSourceSystem() {
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

    public LocalDate getRavRegistrationDate() {
        return ravRegistrationDate;
    }

    public LocalDate getApprovalDate() {
        return approvalDate;
    }

    public LocalDate getRejectionDate() {
        return rejectionDate;
    }

    public String getRejectionCode() {
        return rejectionCode;
    }

    public String getRejectionReason() {
        return rejectionReason;
    }

    public LocalDate getCancellationDate() {
        return cancellationDate;
    }

    public String getCancellationCode() {
        return cancellationCode;
    }

    public boolean isReportingObligation() {
        return reportingObligation;
    }

    public LocalDate getReportingObligationEndDate() {
        return reportingObligationEndDate;
    }

    public LocalDate getPublicationStartDate() {
        return publicationStartDate;
    }

    public LocalDate getPublicationEndDate() {
        return publicationEndDate;
    }

    public boolean isEures() {
        return eures;
    }

    public boolean isEuresAnonymous() {
        return euresAnonymous;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Employment getEmployment() {
        return employment;
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

    public Locality getLocality() {
        return locality;
    }

    public List<Occupation> getOccupations() {
        return occupations;
    }

    public List<LanguageSkill> getLanguageSkills() {
        return languageSkills;
    }

    public void init(JobAdvertisementUpdater updater) {
        checkIfEndStatus();
        applyUpdates(updater);
    }

    public void update(JobAdvertisementUpdater updater) {
        checkIfEndStatus();
        if (applyUpdates(updater)) {
            DomainEventPublisher.publish(new JobAdvertisementEvent(JobAdvertisementEvents.JOB_ADVERTISEMENT_UPDATED, this));
        }
    }

    public void inspect() {
        // TODO check if can be inspected (e.g. stellennummerEgov is not missing)
        //Condition.notBlank(this.stellennummerEgov, "StellennummerEgov can't be null");

        this.ravRegistrationDate = TimeMachine.now().toLocalDate();
        this.status = status.validateTransitionTo(JobAdvertisementStatus.INSPECTING);
        DomainEventPublisher.publish(new JobAdvertisementEvent(JobAdvertisementEvents.JOB_ADVERTISEMENT_INSPECTING, this));
    }

    public void approve(String stellennummerAvam, LocalDate date, boolean reportingObligation, LocalDate reportingObligationEndDate) {
        // TODO tbd where/when the data updates has to be done (over ApprovalDto --> JobAdUpdater?)
        Condition.isTrue(reportingObligation && (reportingObligationEndDate != null), "Reporting obligation end date is missing");

        this.stellennummerAvam = Condition.notBlank(stellennummerAvam);
        this.approvalDate = Condition.notNull(date);
        this.reportingObligation = reportingObligation;
        this.reportingObligationEndDate = reportingObligationEndDate;
        this.status = status.validateTransitionTo(JobAdvertisementStatus.APPROVED);
        DomainEventPublisher.publish(new JobAdvertisementEvent(JobAdvertisementEvents.JOB_ADVERTISEMENT_APPROVED, this));
    }

    public void expireBlackout() {
        Condition.isTrue(this.reportingObligationEndDate.isBefore(TimeMachine.now().toLocalDate()), "Must not be published before setReportingObligationEndDate.");
        Condition.isTrue(JobAdvertisementStatus.PUBLISHED_RESTRICTED.equals(status), "Must be in PUBLISHED_RESTRICTED state.");

        DomainEventPublisher.publish(new JobAdvertisementEvent(JobAdvertisementEvents.JOB_ADVERTISEMENT_BLACKOUT_EXPIRED, this));
    }

    public void expirePublication() {
        Condition.isTrue(this.publicationEndDate.isBefore(TimeMachine.now().toLocalDate()), "Must not be archived before publicationEndDate.");
        Condition.isTrue(JobAdvertisementStatus.PUBLISHED_PUBLIC.equals(status), "Must be in PUBLISHED_PUBLIC state.");

        DomainEventPublisher.publish(new JobAdvertisementEvent(JobAdvertisementEvents.JOB_ADVERTISEMENT_PUBLISH_EXPIRED, this));
    }

    public void reject(String stellennummerAvam, LocalDate date, String code, String reason) {
        this.stellennummerAvam = Condition.notBlank(stellennummerAvam);
        this.rejectionDate = Condition.notNull(date);
        this.rejectionCode = Condition.notBlank(code);
        this.rejectionReason = reason;
        this.status = status.validateTransitionTo(JobAdvertisementStatus.REJECTED);
        DomainEventPublisher.publish(new JobAdvertisementEvent(JobAdvertisementEvents.JOB_ADVERTISEMENT_REJECTED, this));
    }

    public void cancel(LocalDate date, String code) {
        this.cancellationDate = Condition.notNull(date);
        this.cancellationCode = Condition.notBlank(code);
        this.status = status.validateTransitionTo(JobAdvertisementStatus.CANCELLED);
        DomainEventPublisher.publish(new JobAdvertisementEvent(JobAdvertisementEvents.JOB_ADVERTISEMENT_CANCELLED, this));
    }

    public void refining() {
        this.status = status.validateTransitionTo(JobAdvertisementStatus.REFINING);
        DomainEventPublisher.publish(new JobAdvertisementEvent(JobAdvertisementEvents.JOB_ADVERTISEMENT_REFINING, this));
    }

    public void publishRestricted() {
        Condition.notNull(reportingObligationEndDate, "Reporting obligation end date is missing");

        this.status = status.validateTransitionTo(JobAdvertisementStatus.PUBLISHED_RESTRICTED);
        DomainEventPublisher.publish(new JobAdvertisementEvent(JobAdvertisementEvents.JOB_ADVERTISEMENT_PUBLISH_RESTRICTED, this));
    }

    public void publishPublic() {
        Condition.notNull(publicationEndDate, "Publication end date is missing");

        this.status = status.validateTransitionTo(JobAdvertisementStatus.PUBLISHED_PUBLIC);
        DomainEventPublisher.publish(new JobAdvertisementEvent(JobAdvertisementEvents.JOB_ADVERTISEMENT_PUBLISH_PUBLIC, this));
    }

    public void archive() {
        this.status = status.validateTransitionTo(JobAdvertisementStatus.ARCHIVED);
        DomainEventPublisher.publish(new JobAdvertisementEvent(JobAdvertisementEvents.JOB_ADVERTISEMENT_ARCHIVED, this));
    }

    private void checkIfEndStatus() {
        if (this.status.isInAnyStates(JobAdvertisementStatus.REJECTED, JobAdvertisementStatus.CANCELLED, JobAdvertisementStatus.ARCHIVED)) {
            throw new IllegalStateException(String.format("JobAdvertisement must not be in a end status like: %s", this.status));
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        JobAdvertisement that = (JobAdvertisement) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public String toString() {
        return "JobAdvertisement{" +
                "id=" + id +
                ", stellennummerEgov='" + stellennummerEgov + '\'' +
                ", stellennummerAvam='" + stellennummerAvam + '\'' +
                ", fingerprint='" + fingerprint + '\'' +
                ", sourceSystem=" + sourceSystem +
                ", sourceEntryId='" + sourceEntryId + '\'' +
                ", externalUrl='" + externalUrl + '\'' +
                ", status=" + status +
                ", ravRegistrationDate=" + ravRegistrationDate +
                ", approvalDate=" + approvalDate +
                ", rejectionDate=" + rejectionDate +
                ", rejectionCode='" + rejectionCode + '\'' +
                ", rejectionReason='" + rejectionReason + '\'' +
                ", cancellationDate=" + cancellationDate +
                ", cancellationCode='" + cancellationCode + '\'' +
                ", reportingObligation=" + reportingObligation +
                ", reportingObligationEndDate=" + reportingObligationEndDate +
                ", publicationStartDate=" + publicationStartDate +
                ", publicationEndDate=" + publicationEndDate +
                ", eures=" + eures +
                ", euresAnonymous=" + euresAnonymous +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", employment=" + employment +
                ", jobCenterCode='" + jobCenterCode + '\'' +
                ", drivingLicenseLevel='" + drivingLicenseLevel + '\'' +
                ", applyChannel=" + applyChannel +
                ", company=" + company +
                ", contact=" + contact +
                ", locality=" + locality +
                ", occupations=" + occupations +
                ", languageSkills=" + languageSkills +
                '}';
    }

    private boolean applyUpdates(JobAdvertisementUpdater updater) {
        boolean hasChangedAnything = false;

        if (updater.hasAnyChangesIn(SECTION_FINGERPRINT) && hasChanged(this.fingerprint, updater.getFingerprint())) {
            this.fingerprint = updater.getFingerprint();
            hasChangedAnything = true;
        }

        if (updater.hasAnyChangesIn(SECTION_SOURCE_ENTRY_ID) && hasChanged(this.sourceEntryId, updater.getSourceEntryId())) {
            this.sourceEntryId = updater.getSourceEntryId();
            hasChangedAnything = true;
        }

        if (updater.hasAnyChangesIn(SECTION_EXTERNAL_URL) && hasChanged(this.externalUrl, updater.getExternalUrl())) {
            this.externalUrl = updater.getExternalUrl();
            hasChangedAnything = true;
        }

        if (updater.hasAnyChangesIn(SECTION_REPORTING_OBLIGATION) && hasChanged(this.reportingObligation, updater.isReportingObligation())) {
            this.reportingObligation = updater.isReportingObligation();
            hasChangedAnything = true;
        }

        if (updater.hasAnyChangesIn(SECTION_PUBLICATION_DATES) && (
                hasChanged(this.publicationStartDate, updater.getPublicationStartDate()) ||
                        hasChanged(this.publicationEndDate, updater.getPublicationEndDate()))) {
            this.publicationStartDate = updater.getPublicationStartDate();
            this.publicationEndDate = updater.getPublicationEndDate();
            hasChangedAnything = true;
        }

        if (updater.hasAnyChangesIn(SECTION_EURES) && (
                hasChanged(this.eures, updater.isEures()) ||
                        hasChanged(this.euresAnonymous, updater.isEuresAnonymous()))) {
            this.eures = updater.isEures();
            this.euresAnonymous = updater.isEuresAnonymous();
            hasChangedAnything = true;
        }

        if (updater.hasAnyChangesIn(SECTION_EMPLOYMENT) && hasChanged(this.employment, updater.getEmployment())) {
            this.employment = updater.getEmployment();
            hasChangedAnything = true;
        }

        if (updater.hasAnyChangesIn(SECTION_JOB_CENTER_CODE) && hasChanged(this.jobCenterCode, updater.getJobCenterCode())) {
            this.jobCenterCode = updater.getJobCenterCode();
            hasChangedAnything = true;
        }

        if (updater.hasAnyChangesIn(SECTION_DRIVING_LICENSE_LEVEL) && hasChanged(this.drivingLicenseLevel, updater.getDrivingLicenseLevel())) {
            this.drivingLicenseLevel = updater.getDrivingLicenseLevel();
            hasChangedAnything = true;
        }

        if (updater.hasAnyChangesIn(SECTION_APPLY_CHANNEL) && hasChanged(this.applyChannel, updater.getApplyChannel())) {
            this.applyChannel = updater.getApplyChannel();
            hasChangedAnything = true;
        }

        if (updater.hasAnyChangesIn(SECTION_COMPANY) && hasChanged(this.company, updater.getCompany())) {
            this.company = updater.getCompany();
            hasChangedAnything = true;
        }

        if (updater.hasAnyChangesIn(SECTION_CONTACT) && hasChanged(this.contact, updater.getContact())) {
            this.contact = updater.getContact();
            hasChangedAnything = true;
        }

        if (updater.hasAnyChangesIn(SECTION_LOCALITY) && hasChanged(this.locality, updater.getLocality())) {
            this.locality = updater.getLocality();
            hasChangedAnything = true;
        }

        if (updater.hasAnyChangesIn(SECTION_OCCUPATIONS) && hasChangedContent(this.occupations, updater.getOccupations())) {
            this.occupations = updater.getOccupations();
            hasChangedAnything = true;
        }

        if (updater.hasAnyChangesIn(SECTION_LANGUAGE_SKILLS) && hasChangedContent(this.languageSkills, updater.getLanguageSkills())) {
            this.languageSkills = updater.getLanguageSkills();
            hasChangedAnything = true;
        }

        if (hasChangedAnything) {
            // FIXME Auditor
            //applyUpdater(updater.getAuditUser());
        }

        return hasChangedAnything;
    }

    public static final class Builder {
        private JobAdvertisementId id;
        private String stellennummerEgov;
        private String stellennummerAvam;
        private String fingerprint;
        private SourceSystem sourceSystem;
        private String sourceEntryId;
        private String externalUrl;
        private JobAdvertisementStatus status;
        private LocalDate ravRegistrationDate;
        private LocalDate approvalDate;
        private LocalDate rejectionDate;
        private String rejectionCode;
        private String rejectionReason;
        private LocalDate cancellationDate;
        private String cancellationCode;
        private boolean reportingObligation;
        private LocalDate reportingObligationEndDate;
        private LocalDate publicationStartDate;
        private LocalDate publicationEndDate;
        private boolean eures;
        private boolean euresAnonymous;
        private String title;
        private String description;
        private Employment employment;
        private String jobCenterCode;
        private String drivingLicenseLevel;
        private ApplyChannel applyChannel;
        private Company company;
        private Contact contact;
        private Locality locality;
        private List<Occupation> occupations;
        private List<LanguageSkill> languageSkills;

        public Builder() {
        }

        public Builder setId(JobAdvertisementId id) {
            this.id = id;
            return this;
        }

        public Builder setStellennummerEgov(String stellennummerEgov) {
            this.stellennummerEgov = stellennummerEgov;
            return this;
        }

        public Builder setStellennummerAvam(String stellennummerAvam) {
            this.stellennummerAvam = stellennummerAvam;
            return this;
        }

        public Builder setFingerprint(String fingerprint) {
            this.fingerprint = fingerprint;
            return this;
        }

        public Builder setSourceSystem(SourceSystem sourceSystem) {
            this.sourceSystem = sourceSystem;
            return this;
        }

        public Builder setSourceEntryId(String sourceEntryId) {
            this.sourceEntryId = sourceEntryId;
            return this;
        }

        public Builder setExternalUrl(String externalUrl) {
            this.externalUrl = externalUrl;
            return this;
        }

        public Builder setStatus(JobAdvertisementStatus status) {
            this.status = status;
            return this;
        }

        public Builder setRavRegistrationDate(LocalDate ravRegistrationDate) {
            this.ravRegistrationDate = ravRegistrationDate;
            return this;
        }

        public Builder approvalDate(LocalDate approvalDate) {
            this.approvalDate = approvalDate;
            return this;
        }

        public Builder setRejectionDate(LocalDate rejectionDate) {
            this.rejectionDate = rejectionDate;
            return this;
        }

        public Builder setRejectionCode(String rejectionCode) {
            this.rejectionCode = rejectionCode;
            return this;
        }

        public Builder setRejectionReason(String rejectionReason) {
            this.rejectionReason = rejectionReason;
            return this;
        }

        public Builder setCancellationDate(LocalDate cancellationDate) {
            this.cancellationDate = cancellationDate;
            return this;
        }

        public Builder setCancellationCode(String cancellationCode) {
            this.cancellationCode = cancellationCode;
            return this;
        }

        public Builder setReportingObligation(boolean reportingObligation) {
            this.reportingObligation = reportingObligation;
            return this;
        }

        public Builder setReportingObligationEndDate(LocalDate reportingObligationEndDate) {
            this.reportingObligationEndDate = reportingObligationEndDate;
            return this;
        }

        public Builder setPublicationStartDate(LocalDate publicationStartDate) {
            this.publicationStartDate = publicationStartDate;
            return this;
        }

        public Builder setPublicationEndDate(LocalDate publicationEndDate) {
            this.publicationEndDate = publicationEndDate;
            return this;
        }

        public Builder setEures(boolean eures) {
            this.eures = eures;
            return this;
        }

        public Builder setEuresAnonymous(boolean euresAnonymous) {
            this.euresAnonymous = euresAnonymous;
            return this;
        }

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setDescription(String description) {
            this.description = description;
            return this;
        }

        public Builder setEmployment(Employment employment) {
            this.employment = employment;
            return this;
        }

        public Builder setJobCenterCode(String jobCenterCode) {
            this.jobCenterCode = jobCenterCode;
            return this;
        }

        public Builder setDrivingLicenseLevel(String drivingLicenseLevel) {
            this.drivingLicenseLevel = drivingLicenseLevel;
            return this;
        }

        public Builder setApplyChannel(ApplyChannel applyChannel) {
            this.applyChannel = applyChannel;
            return this;
        }

        public Builder setCompany(Company company) {
            this.company = company;
            return this;
        }

        public Builder setContact(Contact contact) {
            this.contact = contact;
            return this;
        }

        public Builder setLocality(Locality locality) {
            this.locality = locality;
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

        public JobAdvertisement build() {
            return new JobAdvertisement(this);
        }
    }
}
