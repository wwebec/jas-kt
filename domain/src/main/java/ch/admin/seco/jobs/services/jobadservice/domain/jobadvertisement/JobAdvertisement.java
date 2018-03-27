package ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement;

import ch.admin.seco.jobs.services.jobadservice.core.conditions.Condition;
import ch.admin.seco.jobs.services.jobadservice.core.domain.Aggregate;
import ch.admin.seco.jobs.services.jobadservice.core.domain.events.DomainEventPublisher;
import ch.admin.seco.jobs.services.jobadservice.core.time.TimeMachine;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.events.*;

import javax.persistence.*;
import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;

import static ch.admin.seco.jobs.services.jobadservice.core.utils.CompareUtils.hasChanged;
import static ch.admin.seco.jobs.services.jobadservice.core.utils.CompareUtils.hasChangedContent;
import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisementUpdater.*;

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

    private boolean reportToRav;

    private boolean reportingObligation;

    private LocalDate reportingObligationEndDate;

    private LocalDate publicationStartDate;

    private LocalDate publicationEndDate;

    private boolean eures;

    private boolean euresAnonymous;

    private Locale language;

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
            @AttributeOverride(name = "salutation", column = @Column(name = "CONTACT_SALUTATION")),
            @AttributeOverride(name = "firstName", column = @Column(name = "CONTACT_FIRST_NAME")),
            @AttributeOverride(name = "lastName", column = @Column(name = "CONTACT_LAST_NAME")),
            @AttributeOverride(name = "phone", column = @Column(name = "CONTACT_PHONE")),
            @AttributeOverride(name = "email", column = @Column(name = "CONTACT_EMAIL")),
            @AttributeOverride(name = "language", column = @Column(name = "CONTACT_LANGUAGE"))
    })
    @Valid
    private Contact contact;

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
    private List<Occupation> occupations;

    @ElementCollection
    @CollectionTable(name = "JOB_ADVERTISEMENT_LANGUAGE_SKILL", joinColumns = @JoinColumn(name = "JOB_ADVERTISEMENT_ID"))
    @Valid
    private List<LanguageSkill> languageSkills;

    @ElementCollection
    @CollectionTable(name = "JOB_ADVERTISEMENT_WORK_FORM", joinColumns = @JoinColumn(name = "JOB_ADVERTISEMENT_ID"))
    @Enumerated(EnumType.STRING)
    private Set<WorkForm> workForms;

    private boolean publicAnonymous;

    private boolean publicPublication;

    private boolean restrictedAnonymous;

    private boolean restrictedPublication;

    protected JobAdvertisement() {
        // For reflection libs
    }

    private JobAdvertisement(Builder builder) {
        this(builder.id, builder.sourceSystem, builder.status, builder.language, builder.title, builder.description);
        this.stellennummerEgov = builder.stellennummerEgov;
        this.stellennummerAvam = builder.stellennummerAvam;
        this.fingerprint = builder.fingerprint;
        this.sourceEntryId = builder.sourceEntryId;
        this.externalUrl = builder.externalUrl;
        this.ravRegistrationDate = builder.ravRegistrationDate;
        this.approvalDate = builder.approvalDate;
        this.rejectionDate = builder.rejectionDate;
        this.rejectionCode = builder.rejectionCode;
        this.rejectionReason = builder.rejectionReason;
        this.cancellationDate = builder.cancellationDate;
        this.cancellationCode = builder.cancellationCode;
        this.reportToRav = builder.reportToRav;
        this.reportingObligation = builder.reportingObligation;
        this.reportingObligationEndDate = builder.reportingObligationEndDate;
        this.publicationStartDate = builder.publicationStartDate;
        this.publicationEndDate = builder.publicationEndDate;
        this.eures = builder.eures;
        this.euresAnonymous = builder.euresAnonymous;
        this.employment = builder.employment;
        this.jobCenterCode = builder.jobCenterCode;
        this.applyChannel = builder.applyChannel;
        this.company = builder.company;
        this.contact = builder.contact;
        this.location = builder.location;
        this.occupations = builder.occupations;
        this.languageSkills = builder.languageSkills;
        this.workForms = builder.workForms;
        this.publicAnonymous = builder.publicAnonymous;
        this.publicPublication = builder.publicPublication;
        this.restrictedAnonymous = builder.restrictedAnonymous;
        this.restrictedPublication = builder.restrictedPublication;
    }

    public JobAdvertisement(JobAdvertisementId id, SourceSystem sourceSystem, JobAdvertisementStatus status,
                            Locale language, String title, String description) {
        this.id = Condition.notNull(id, "Id can't be null");
        this.sourceSystem = Condition.notNull(sourceSystem, "Source system can't be null");
        this.status = Condition.notNull(status, "Status can't be null");
        this.language = Condition.notNull(language, "Language can't be null");
        this.title = Condition.notBlank(title, "Title can't be blank");
        this.description = Condition.notBlank(description, "Description can't be blank");
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

    public boolean isReportToRav() {
        return reportToRav;
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

    public Locale getLanguage() {
        return language;
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

    public ApplyChannel getApplyChannel() {
        return applyChannel;
    }

    public Company getCompany() {
        return company;
    }

    public Contact getContact() {
        return contact;
    }

    public Location getLocation() {
        return location;
    }

    public List<Occupation> getOccupations() {
        return occupations;
    }

    public List<LanguageSkill> getLanguageSkills() {
        return languageSkills;
    }

    public Set<WorkForm> getWorkForms() {
        return workForms;
    }

    public boolean isPublicAnonymous() {
        return publicAnonymous;
    }

    public boolean isPublicPublication() {
        return publicPublication;
    }

    public boolean isRestrictedAnonymous() {
        return restrictedAnonymous;
    }

    public boolean isRestrictedPublication() {
        return restrictedPublication;
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
        Condition.notBlank(this.stellennummerEgov, "StellennummerEgov can't be null");

        this.ravRegistrationDate = TimeMachine.now().toLocalDate();
        this.status = status.validateTransitionTo(JobAdvertisementStatus.INSPECTING);
        DomainEventPublisher.publish(new JobAdvertisementInspectingEvent(this));
    }

    public void approve(String stellennummerAvam, LocalDate date, boolean reportingObligation, LocalDate reportingObligationEndDate) {
        // TODO tbd where/when the data updates has to be done (over ApprovalDto --> JobAdUpdater?)
        Condition.isTrue(reportingObligation && (reportingObligationEndDate != null), "Reporting obligation end date is missing");

        this.stellennummerAvam = Condition.notBlank(stellennummerAvam);
        this.approvalDate = Condition.notNull(date);
        this.reportingObligation = reportingObligation;
        this.reportingObligationEndDate = reportingObligationEndDate;
        this.status = status.validateTransitionTo(JobAdvertisementStatus.APPROVED);
        DomainEventPublisher.publish(new JobAdvertisementApprovedEvent(this));
    }

    public void expireBlackout() {
        Condition.isTrue(this.reportingObligationEndDate.isBefore(TimeMachine.now().toLocalDate()), "Must not be published before setReportingObligationEndDate.");
        Condition.isTrue(JobAdvertisementStatus.PUBLISHED_RESTRICTED.equals(status), "Must be in PUBLISHED_RESTRICTED state.");

        DomainEventPublisher.publish(new JobAdvertisementBlackoutExpiredEvent(this));
    }

    public void expirePublication() {
        Condition.isTrue(this.publicationEndDate.isBefore(TimeMachine.now().toLocalDate()), "Must not be archived before publicationEndDate.");
        Condition.isTrue(JobAdvertisementStatus.PUBLISHED_PUBLIC.equals(status), "Must be in PUBLISHED_PUBLIC state.");

        DomainEventPublisher.publish(new JobAdvertisementPublishExpiredEvent(this));
    }

    public void reject(String stellennummerAvam, LocalDate date, String code, String reason) {
        this.stellennummerAvam = Condition.notBlank(stellennummerAvam);
        this.rejectionDate = Condition.notNull(date);
        this.rejectionCode = Condition.notBlank(code);
        this.rejectionReason = reason;
        this.status = status.validateTransitionTo(JobAdvertisementStatus.REJECTED);
        DomainEventPublisher.publish(new JobAdvertisementRejectedEvent(this));
    }

    public void cancel(LocalDate date, String code) {
        this.cancellationDate = Condition.notNull(date);
        this.cancellationCode = Condition.notBlank(code);
        this.status = status.validateTransitionTo(JobAdvertisementStatus.CANCELLED);
        DomainEventPublisher.publish(new JobAdvertisementCancelledEvent(this));
    }

    public void refining() {
        this.status = status.validateTransitionTo(JobAdvertisementStatus.REFINING);
        DomainEventPublisher.publish(new JobAdvertisementRefiningEvent(this));
    }

    public void publishRestricted() {
        Condition.notNull(reportingObligationEndDate, "Reporting obligation end date is missing");

        this.status = status.validateTransitionTo(JobAdvertisementStatus.PUBLISHED_RESTRICTED);
        DomainEventPublisher.publish(new JobAdvertisementPublishRestrictedEvent(this));
    }

    public void publishPublic() {
        Condition.notNull(publicationEndDate, "Publication end date is missing");

        this.status = status.validateTransitionTo(JobAdvertisementStatus.PUBLISHED_PUBLIC);
        DomainEventPublisher.publish(new JobAdvertisementPublishPublicEvent(this));
    }

    public void archive() {
        this.status = status.validateTransitionTo(JobAdvertisementStatus.ARCHIVED);
        DomainEventPublisher.publish(new JobAdvertisementArchivedEvent(this));
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
                ", language=" + language.getLanguage() +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", employment=" + employment +
                ", jobCenterCode='" + jobCenterCode + '\'' +
                ", applyChannel=" + applyChannel +
                ", company=" + company +
                ", contact=" + contact +
                ", location=" + location +
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

        if (updater.hasAnyChangesIn(SECTION_LOCATION) && hasChanged(this.location, updater.getLocation())) {
            this.location = updater.getLocation();
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

        if (updater.hasAnyChangesIn(SECTION_WORK_FORMS) && hasChangedContent(this.workForms, updater.getWorkForms())) {
            this.workForms = updater.getWorkForms();
            hasChangedAnything = true;
        }

        if (updater.hasAnyChangesIn(SECTION_PUBLIC_ANONYMOUS) && hasChanged(this.publicAnonymous, updater.isPublicAnonymous())) {
            this.publicAnonymous = updater.isPublicAnonymous();
            hasChangedAnything = true;
        }

        if (updater.hasAnyChangesIn(SECTION_PUBLIC_PUBLICATION) && hasChanged(this.publicPublication, updater.isPublicPublication())) {
            this.publicPublication = updater.isPublicPublication();
            hasChangedAnything = true;
        }

        if (updater.hasAnyChangesIn(SECTION_RESTRICTED_ANONYMOUS) && hasChanged(this.restrictedAnonymous, updater.isRestrictedAnonymous())) {
            this.restrictedAnonymous = updater.isRestrictedAnonymous();
            hasChangedAnything = true;
        }

        if (updater.hasAnyChangesIn(SECTION_RESTRICTED_PUBLICATION) && hasChanged(this.restrictedPublication, updater.isRestrictedPublication())) {
            this.restrictedPublication = updater.isRestrictedPublication();
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
        private boolean reportToRav;
        private boolean reportingObligation;
        private LocalDate reportingObligationEndDate;
        private LocalDate publicationStartDate;
        private LocalDate publicationEndDate;
        private boolean eures;
        private boolean euresAnonymous;
        private Locale language;
        private String title;
        private String description;
        private Employment employment;
        private String jobCenterCode;
        private ApplyChannel applyChannel;
        private Company company;
        private Contact contact;
        private Location location;
        private List<Occupation> occupations;
        private List<LanguageSkill> languageSkills;
        private Set<WorkForm> workForms;
        private boolean publicAnonymous;
        private boolean publicPublication;
        private boolean restrictedAnonymous;
        private boolean restrictedPublication;

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

        public Builder setReportToRav(boolean reportToRav) {
            this.reportToRav = reportToRav;
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

        public Builder setLanguage(Locale language) {
            this.language = language;
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

        public Builder setWorkForms(Set<WorkForm> workForms) {
            this.workForms = workForms;
            return this;
        }

        public Builder setPublicAnonymous(boolean publicAnonymous) {
            this.publicAnonymous = publicAnonymous;
            return this;
        }

        public Builder setPublicPublication(boolean publicPublication) {
            this.publicPublication = publicPublication;
            return this;
        }

        public Builder setRestrictedAnonymous(boolean restrictedAnonymous) {
            this.restrictedAnonymous = restrictedAnonymous;
            return this;
        }

        public Builder setRestrictedPublication(boolean restrictedPublication) {
            this.restrictedPublication = restrictedPublication;
            return this;
        }

        public JobAdvertisement build() {
            return new JobAdvertisement(this);
        }
    }
}
