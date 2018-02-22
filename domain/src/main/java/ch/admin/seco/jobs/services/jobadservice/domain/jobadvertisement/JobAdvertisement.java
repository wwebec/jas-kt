package ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement;

import ch.admin.seco.jobs.services.jobadservice.core.conditions.Condition;
import ch.admin.seco.jobs.services.jobadservice.core.domain.Aggregate;
import ch.admin.seco.jobs.services.jobadservice.core.domain.events.DomainEventPublisher;
import ch.admin.seco.jobs.services.jobadservice.core.time.TimeMachine;

import javax.persistence.*;
import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;

import static ch.admin.seco.jobs.services.jobadservice.core.utils.CompareUtils.hasChanged;
import static ch.admin.seco.jobs.services.jobadservice.core.utils.CompareUtils.hasChangedContent;
import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisementUpdater.*;

@Entity
public class JobAdvertisement implements Aggregate<JobAdvertisement, JobAdvertisementId> {

    @EmbeddedId
    @AttributeOverride(name = "value", column = @Column(name = "ID"))
    @Valid
    private JobAdvertisementId id;

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

    private LocalDate publicationStartDate;

    private LocalDate publicationEndDate;

    private boolean eures;

    private boolean euresAnonymous;

    private String title;

    private String description;

    private LocalDate employmentStartDate;

    private LocalDate employmentEndDate;

    private Integer durationInDays;

    private Boolean immediately;

    private Boolean permanent;

    private int workloadPercentageMin;

    private int workloadPercentageMax;

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

    public JobAdvertisement(JobAdvertisementId id, SourceSystem sourceSystem, JobAdvertisementStatus status, String title, String description) {
        this.id = Condition.notNull(id);
        this.sourceSystem = Condition.notNull(sourceSystem);
        this.status = Condition.notNull(status);
        this.title = Condition.notBlank(title);
        this.description = Condition.notBlank(description);
    }

    public JobAdvertisement(JobAdvertisementId id, String stellennummerAvam, String fingerprint, SourceSystem sourceSystem, String sourceEntryId, String externalUrl, JobAdvertisementStatus status, LocalDate ravRegistrationDate, LocalDate approvalDate, LocalDate rejectionDate, String rejectionReason, LocalDate cancellationDate, String cancellationCode, boolean reportingObligation, LocalDate publicationStartDate, LocalDate publicationEndDate, boolean eures, boolean euresAnonymous, String title, String description, String rejectionCode, LocalDate employmentStartDate, LocalDate employmentEndDate, Integer durationInDays, Boolean immediately, Boolean permanent, int workloadPercentageMin, int workloadPercentageMax, String jobCenterCode, String drivingLicenseLevel, ApplyChannel applyChannel, Company company, Contact contact, List<Locality> localities, List<Occupation> occupations, String educationCode, List<LanguageSkill> languageSkills, List<String> professionCodes) {
        this(id, sourceSystem, status, title, description);
        this.stellennummerAvam = stellennummerAvam;
        this.fingerprint = fingerprint;
        this.sourceEntryId = sourceEntryId;
        this.externalUrl = externalUrl;
        this.ravRegistrationDate = ravRegistrationDate;
        this.approvalDate = approvalDate;
        this.rejectionDate = rejectionDate;
        this.rejectionCode = rejectionCode;
        this.rejectionReason = rejectionReason;
        this.cancellationDate = cancellationDate;
        this.cancellationCode = cancellationCode;
        this.reportingObligation = reportingObligation;
        this.publicationStartDate = publicationStartDate;
        this.publicationEndDate = publicationEndDate;
        this.eures = eures;
        this.euresAnonymous = euresAnonymous;
        this.employmentStartDate = employmentStartDate;
        this.employmentEndDate = employmentEndDate;
        this.durationInDays = durationInDays;
        this.immediately = immediately;
        this.permanent = permanent;
        this.workloadPercentageMin = workloadPercentageMin;
        this.workloadPercentageMax = workloadPercentageMax;
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
        this.ravRegistrationDate = TimeMachine.now().toLocalDate();
        DomainEventPublisher.publish(new JobAdvertisementEvent(JobAdvertisementEvents.JOB_ADVERTISEMENT_INSPECTING, this));
    }

    public void approve(String stellennummerAvam, LocalDate date, boolean reportingObligation) {
        this.stellennummerAvam = Condition.notBlank(stellennummerAvam);
        this.approvalDate = Condition.notNull(date);
        this.reportingObligation = reportingObligation;
        this.status = status.validateTransitionTo(JobAdvertisementStatus.APPROVED);
        DomainEventPublisher.publish(new JobAdvertisementEvent(JobAdvertisementEvents.JOB_ADVERTISEMENT_APPROVED, this));
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

    public void publishRestricted() {
        this.status = status.validateTransitionTo(JobAdvertisementStatus.PUBLISHED_RESTRICTED);
        DomainEventPublisher.publish(new JobAdvertisementEvent(JobAdvertisementEvents.JOB_ADVERTISEMENT_PUBLISHED_RESTRICTED, this));
    }

    public void publishPublic() {
        this.status = status.validateTransitionTo(JobAdvertisementStatus.PUBLISHED_PUBLIC);
        DomainEventPublisher.publish(new JobAdvertisementEvent(JobAdvertisementEvents.JOB_ADVERTISEMENT_PUBLISHED_PUBLIC, this));
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

        if (updater.hasAnyChangesIn(SECTION_EMPLOYMENT) && (
                hasChanged(this.employmentStartDate, updater.getEmploymentStartDate()) ||
                        hasChanged(this.employmentEndDate, updater.getEmploymentEndDate()) ||
                        hasChanged(this.durationInDays, updater.getDurationInDays()) ||
                        hasChanged(this.immediately, updater.getImmediately()) ||
                        hasChanged(this.permanent, updater.getPermanent()) ||
                        hasChanged(this.workloadPercentageMin, updater.getWorkloadPercentageMin()) ||
                        hasChanged(this.workloadPercentageMax, updater.getWorkloadPercentageMax())
        )) {
            this.employmentStartDate = updater.getEmploymentStartDate();
            this.employmentEndDate = updater.getEmploymentEndDate();
            this.durationInDays = updater.getDurationInDays();
            this.immediately = updater.getImmediately();
            this.permanent = updater.getPermanent();
            this.workloadPercentageMin = updater.getWorkloadPercentageMin();
            this.workloadPercentageMax = updater.getWorkloadPercentageMax();
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

        if (updater.hasAnyChangesIn(SECTION_LOCALITIES) && hasChangedContent(this.localities, updater.getLocalities())) {
            this.localities = updater.getLocalities();
            hasChangedAnything = true;
        }

        if (updater.hasAnyChangesIn(SECTION_OCCUPATIONS) && hasChangedContent(this.occupations, updater.getOccupations())) {
            this.occupations = updater.getOccupations();
            hasChangedAnything = true;
        }

        if (updater.hasAnyChangesIn(SECTION_EDUCATION_CODE) && hasChanged(this.educationCode, updater.getEducationCode())) {
            this.educationCode = updater.getEducationCode();
            hasChangedAnything = true;
        }

        if (updater.hasAnyChangesIn(SECTION_LANGUAGE_SKILLS) && hasChangedContent(this.languageSkills, updater.getLanguageSkills())) {
            this.languageSkills = updater.getLanguageSkills();
            hasChangedAnything = true;
        }

        if (updater.hasAnyChangesIn(SECTION_PROFESSION_CODES) && hasChangedContent(this.professionCodes, updater.getProfessionCodes())) {
            this.professionCodes = updater.getProfessionCodes();
            hasChangedAnything = true;
        }

        if (hasChangedAnything) {
            // FIXME Auditor
            //applyUpdater(updater.getAuditUser());
        }

        return hasChangedAnything;
    }
}
