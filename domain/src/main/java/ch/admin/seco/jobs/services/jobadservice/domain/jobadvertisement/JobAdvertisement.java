package ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement;

import ch.admin.seco.jobs.services.jobadservice.core.conditions.Condition;
import ch.admin.seco.jobs.services.jobadservice.core.domain.Aggregate;
import ch.admin.seco.jobs.services.jobadservice.core.domain.events.DomainEventPublisher;
import ch.admin.seco.jobs.services.jobadservice.core.time.TimeMachine;
import ch.admin.seco.jobs.services.jobadservice.core.validations.Violations;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.changes.ChangeLog;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.events.*;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Objects;

import static ch.admin.seco.jobs.services.jobadservice.core.utils.CompareUtils.hasChanged;
import static ch.admin.seco.jobs.services.jobadservice.core.utils.CompareUtils.hasChangedContent;
import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisementUpdater.*;

@Entity
public class JobAdvertisement implements Aggregate<JobAdvertisement, JobAdvertisementId> {

    @EmbeddedId
    @AttributeOverride(name = "value", column = @Column(name = "ID"))
    @Valid
    private JobAdvertisementId id;

    private LocalDateTime createdTime;

    private LocalDateTime updatedTime;

    @NotNull
    @Enumerated(EnumType.STRING)
    private JobAdvertisementStatus status;

    @NotNull
    @Enumerated(EnumType.STRING)
    private SourceSystem sourceSystem;

    private String externalReference;

    private String stellennummerEgov;

    private String stellennummerAvam;

    private String fingerprint;

    private boolean reportingObligation;

    private LocalDate reportingObligationEndDate;

    private boolean reportToAvam;

    private String jobCenterCode;

    private LocalDate approvalDate;

    private LocalDate rejectionDate;

    private String rejectionCode;

    private String rejectionReason;

    private LocalDate cancellationDate;

    @Enumerated(EnumType.STRING)
    private CancellationCode cancellationCode;

    @Embedded
    @Valid
    @NotNull
    private JobContent jobContent;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "userId", column = @Column(name = "OWNER_USER_ID")),
            @AttributeOverride(name = "companyId", column = @Column(name = "OWNER_COMPANY_ID")),
            @AttributeOverride(name = "accessToken", column = @Column(name = "OWNER_ACCESS_TOKEN"))
    })
    @Valid
    @NotNull
    private Owner owner;

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
            @AttributeOverride(name = "startDate", column = @Column(name = "PUBLICATION_START_DATE")),
            @AttributeOverride(name = "endDate", column = @Column(name = "PUBLICATION_END_DATE")),
            @AttributeOverride(name = "euresDisplay", column = @Column(name = "PUBLICATION_EURES_DISPLAY")),
            @AttributeOverride(name = "euresAnonymous", column = @Column(name = "PUBLICATION_EURES_ANONYMOUS")),
            @AttributeOverride(name = "publicDisplay", column = @Column(name = "PUBLICATION_PUBLIC_DISPLAY")),
            @AttributeOverride(name = "publicAnonymous", column = @Column(name = "PUBLICATION_PUBLIC_ANONYMOUS")),
            @AttributeOverride(name = "restrictedDisplay", column = @Column(name = "PUBLICATION_RESTRICTED_DISPLAY")),
            @AttributeOverride(name = "restrictedAnonymous", column = @Column(name = "PUBLICATION_RESTRICTED_ANONYMOUS"))
    })
    @Valid
    @NotNull
    private Publication publication;

    protected JobAdvertisement() {
        // For reflection libs
    }

    private JobAdvertisement(Builder builder) {
        this.id = Condition.notNull(builder.id, "Id can't be null");
        this.createdTime = TimeMachine.now();
        this.updatedTime = TimeMachine.now();
        this.status = Condition.notNull(builder.status, "Status can't be null");
        this.sourceSystem = Condition.notNull(builder.sourceSystem, "Source system can't be null");
        this.externalReference = builder.externalReference;
        this.stellennummerEgov = builder.stellennummerEgov;
        this.stellennummerAvam = builder.stellennummerAvam;
        this.fingerprint = builder.fingerprint;
        this.reportingObligation = builder.reportingObligation;
        this.reportingObligationEndDate = builder.reportingObligationEndDate;
        this.reportToAvam = builder.reportToAvam;
        this.jobCenterCode = builder.jobCenterCode;
        this.approvalDate = builder.approvalDate;
        this.rejectionDate = builder.rejectionDate;
        this.rejectionCode = builder.rejectionCode;
        this.rejectionReason = builder.rejectionReason;
        this.cancellationDate = builder.cancellationDate;
        this.cancellationCode = builder.cancellationCode;
        this.jobContent = Condition.notNull(builder.jobContent);
        this.owner = Condition.notNull(builder.owner);
        this.contact = builder.contact;
        this.publication = Condition.notNull(builder.publication);
        checkViolations();
    }

    public JobAdvertisementId getId() {
        return id;
    }

    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    public LocalDateTime getUpdatedTime() {
        return updatedTime;
    }

    public JobAdvertisementStatus getStatus() {
        return status;
    }

    public SourceSystem getSourceSystem() {
        return sourceSystem;
    }

    public String getExternalReference() {
        return externalReference;
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

    public boolean isReportingObligation() {
        return reportingObligation;
    }

    public LocalDate getReportingObligationEndDate() {
        return reportingObligationEndDate;
    }

    public boolean isReportToAvam() {
        return reportToAvam;
    }

    public String getJobCenterCode() {
        return jobCenterCode;
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

    public CancellationCode getCancellationCode() {
        return cancellationCode;
    }

    public JobContent getJobContent() {
        return jobContent;
    }

    public Owner getOwner() {
        return owner;
    }

    public Contact getContact() {
        return contact;
    }

    public Publication getPublication() {
        return publication;
    }

    public void update(JobAdvertisementUpdater updater) {
        ChangeLog changeLog = applyUpdates(updater);
        if (!changeLog.isEmpty()) {
            this.updatedTime = TimeMachine.now();
            DomainEventPublisher.publish(new JobAdvertisementUpdatedEvent(this, changeLog));
        }
    }

    public void inspect() {
        Condition.notBlank(this.stellennummerEgov, "StellennummerEgov can't be null");

        this.status = status.validateTransitionTo(JobAdvertisementStatus.INSPECTING);
        this.updatedTime = TimeMachine.now();
        DomainEventPublisher.publish(new JobAdvertisementInspectingEvent(this));
    }

    public void approve(String stellennummerAvam, LocalDate date, boolean reportingObligation, LocalDate reportingObligationEndDate) {
        if (reportingObligation) {
            Condition.notNull(reportingObligationEndDate, "Reporting obligation end date is missing");
        }
        this.stellennummerAvam = Condition.notBlank(stellennummerAvam);
        this.approvalDate = Condition.notNull(date);
        this.reportingObligation = reportingObligation;
        this.reportingObligationEndDate = reportingObligationEndDate;
        this.status = status.validateTransitionTo(JobAdvertisementStatus.APPROVED);
        this.updatedTime = TimeMachine.now();
        DomainEventPublisher.publish(new JobAdvertisementApprovedEvent(this));
    }

    public void expireBlackout() {
        Condition.isTrue(this.reportingObligationEndDate.isBefore(TimeMachine.now().toLocalDate()), "Must not be published before setReportingObligationEndDate.");
        Condition.isTrue(JobAdvertisementStatus.PUBLISHED_RESTRICTED.equals(status), "Must be in PUBLISHED_RESTRICTED state.");

        DomainEventPublisher.publish(new JobAdvertisementBlackoutExpiredEvent(this));
    }

    public void expirePublication() {
        Condition.isTrue(JobAdvertisementStatus.PUBLISHED_PUBLIC.equals(status), "Must be in PUBLISHED_PUBLIC state.");

        DomainEventPublisher.publish(new JobAdvertisementPublishExpiredEvent(this));
    }

    public void reject(String stellennummerAvam, LocalDate date, String code, String reason, String jobCenterCode) {
        this.stellennummerAvam = stellennummerAvam;
        this.rejectionDate = Condition.notNull(date);
        this.rejectionCode = Condition.notBlank(code);
        this.rejectionReason = reason;
        this.jobCenterCode = jobCenterCode;
        this.status = status.validateTransitionTo(JobAdvertisementStatus.REJECTED);
        this.updatedTime = TimeMachine.now();
        DomainEventPublisher.publish(new JobAdvertisementRejectedEvent(this));
    }

    public void cancel(LocalDate date, CancellationCode cancellationCode, SourceSystem cancelledBy) {
        this.cancellationDate = Condition.notNull(date);
        this.cancellationCode = Condition.notNull(cancellationCode);
        this.status = status.validateTransitionTo(JobAdvertisementStatus.CANCELLED);
        this.updatedTime = TimeMachine.now();
        DomainEventPublisher.publish(new JobAdvertisementCancelledEvent(this, cancelledBy));
    }

    public void refining() {
        this.status = status.validateTransitionTo(JobAdvertisementStatus.REFINING);
        this.updatedTime = TimeMachine.now();
        DomainEventPublisher.publish(new JobAdvertisementRefiningEvent(this));
        // FIXME: shortcut, because the x28-api is not yet ready. to bee handel when api is implemented
        DomainEventPublisher.publish(new JobAdvertisementRefinedEvent(this));
    }

    public void publishRestricted() {
        Condition.notNull(reportingObligationEndDate, "Reporting obligation end date is missing");

        this.status = status.validateTransitionTo(JobAdvertisementStatus.PUBLISHED_RESTRICTED);
        this.updatedTime = TimeMachine.now();
        DomainEventPublisher.publish(new JobAdvertisementPublishRestrictedEvent(this));
    }

    public void publishPublic() {
        Condition.notNull(getPublication().getEndDate(), "Publication end date is missing");

        this.status = status.validateTransitionTo(JobAdvertisementStatus.PUBLISHED_PUBLIC);
        this.updatedTime = TimeMachine.now();
        DomainEventPublisher.publish(new JobAdvertisementPublishPublicEvent(this));
    }

    public void republish() {
        Condition.notNull(getPublication().getEndDate(), "Publication end date is missing");

        this.status = status.validateTransitionTo(JobAdvertisementStatus.PUBLISHED_PUBLIC);
        this.updatedTime = TimeMachine.now();
        DomainEventPublisher.publish(new JobAdvertisementPublishPublicEvent(this));
    }

    public void archive() {
        this.status = status.validateTransitionTo(JobAdvertisementStatus.ARCHIVED);
        this.updatedTime = TimeMachine.now();
        DomainEventPublisher.publish(new JobAdvertisementArchivedEvent(this));
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
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "JobAdvertisement{" +
                "id=" + id +
                ", status=" + status +
                ", sourceSystem=" + sourceSystem +
                ", externalReference='" + externalReference + '\'' +
                ", stellennummerEgov='" + stellennummerEgov + '\'' +
                ", stellennummerAvam='" + stellennummerAvam + '\'' +
                ", fingerprint='" + fingerprint + '\'' +
                ", reportingObligation=" + reportingObligation +
                ", reportingObligationEndDate=" + reportingObligationEndDate +
                ", reportToAvam=" + reportToAvam +
                ", jobCenterCode='" + jobCenterCode + '\'' +
                ", approvalDate=" + approvalDate +
                ", rejectionDate=" + rejectionDate +
                ", rejectionCode='" + rejectionCode + '\'' +
                ", rejectionReason='" + rejectionReason + '\'' +
                ", cancellationDate=" + cancellationDate +
                ", cancellationCode='" + cancellationCode + '\'' +
                ", jobContent=" + jobContent +
                ", owner=" + owner +
                ", contact=" + contact +
                ", publication=" + publication +
                '}';
    }

    private void checkViolations() {
        Violations violations = new Violations();
        Employment employment = jobContent.getEmployment();
        violations.addIfTrue(
                (employment.getEndDate() != null) && (employment.getStartDate() != null) && employment.getStartDate().isAfter(employment.getEndDate()),
                "Employment endDate %s is before startDate %s",
                employment.getEndDate(),
                employment.getStartDate()
        );
        violations.addIfTrue(
                employment.isShortEmployment() && employment.isPermanent(),
                "Employment is short-term and permanent at the same time"
        );
        violations.addIfTrue(
                (employment.getWorkloadPercentageMin() <= 0) || (employment.getWorkloadPercentageMin() > 100),
                "WorkloadPercentageMin %d is out of range [1..100]",
                employment.getWorkloadPercentageMin()
        );
        violations.addIfTrue(
                (employment.getWorkloadPercentageMax() <= 0) || (employment.getWorkloadPercentageMax() > 100),
                "WorkloadPercentageMax %d is out of range [1..100]",
                employment.getWorkloadPercentageMax()
        );
        violations.addIfTrue(
                employment.getWorkloadPercentageMin() > employment.getWorkloadPercentageMax(),
                "WorkloadPercentageMin %d is greater than workloadPercentageMax %d",
                employment.getWorkloadPercentageMin(),
                employment.getWorkloadPercentageMax()
        );
        violations.addIfTrue(
                (publication.getEndDate() != null) && (publication.getStartDate() != null) && publication.getStartDate().isAfter(publication.getEndDate()),
                "Publication endDate %s is before startDate %s",
                publication.getEndDate(),
                publication.getStartDate()
        );
        Condition.isTrue(violations.isEmpty(), String.valueOf(violations.getMessages()));
    }

    private ChangeLog applyUpdates(JobAdvertisementUpdater updater) {
        ChangeLog changeLog = new ChangeLog();

        if (updater.hasAnyChangesIn(SECTION_FINGERPRINT) && hasChanged(fingerprint, updater.getFingerprint())) {
            changeLog.add("fingerprint", fingerprint, updater.getFingerprint());
            fingerprint = updater.getFingerprint();
        }

        if (updater.hasAnyChangesIn(SECTION_X28_OCCUPATION_CODES) && hasChanged(jobContent.getX28OccupationCodes(), updater.getX28OccupationCodes())) {
            changeLog.add("x28OccupationCodes", jobContent.getX28OccupationCodes(), updater.getX28OccupationCodes());
            getJobContent().setX28OccupationCodes(updater.getX28OccupationCodes());
        }

        if (updater.hasAnyChangesIn(SECTION_NUMBER_OF_JOBS) && hasChanged(jobContent.getNumberOfJobs(), updater.getNumberOfJobs())) {
            changeLog.add("numberOfJobs", jobContent.getNumberOfJobs(), updater.getNumberOfJobs());
            jobContent.setNumberOfJobs(updater.getNumberOfJobs());
        }

        if (updater.hasAnyChangesIn(SECTION_REPORTING_OBLIGATION) && (
                hasChanged(reportingObligation, updater.isReportingObligation()) ||
                        hasChanged(reportingObligationEndDate, updater.getReportingObligationEndDate()))) {
            changeLog.add("reportingObligation", reportingObligation, updater.isReportingObligation());
            reportingObligation = updater.isReportingObligation();
            changeLog.add("reportingObligationEndDate", reportingObligationEndDate, updater.getReportingObligationEndDate());
            reportingObligationEndDate = updater.getReportingObligationEndDate();
        }

        if (updater.hasAnyChangesIn(SECTION_JOBDESCRIPTION)) {
            if (jobContent.getJobDescriptions().size() > 0) {
                JobDescription jobDescription = jobContent.getJobDescriptions().get(0);
                if (hasChanged(jobDescription.getTitle(), updater.getTitle()) || hasChanged(jobDescription.getDescription(), updater.getDescription())) {
                    changeLog.add("title", jobDescription.getTitle(), updater.getTitle());
                    changeLog.add("description", jobDescription.getDescription(), updater.getDescription());
                    jobDescription.updateTitleAndDescription(updater.getTitle(), updater.getDescription());
                }
            }
        }

        if (updater.hasAnyChangesIn(SECTION_JOB_CENTER_CODE) && hasChanged(jobCenterCode, updater.getJobCenterCode())) {
            changeLog.add("jobCenterCode", jobCenterCode, updater.getJobCenterCode());
            this.jobCenterCode = updater.getJobCenterCode();
        }

        if (updater.hasAnyChangesIn(SECTION_COMPANY) && hasChanged(jobContent.getCompany(), updater.getCompany())) {
            changeLog.add("company", jobContent.getCompany(), updater.getCompany());
            jobContent.setCompany(updater.getCompany());
        }

        if (updater.hasAnyChangesIn(SECTION_EMPLOYMENT) && hasChanged(jobContent.getEmployment(), updater.getEmployment())) {
            changeLog.add("employment", jobContent.getEmployment(), updater.getEmployment());
            jobContent.setEmployment(updater.getEmployment());
        }

        if (updater.hasAnyChangesIn(SECTION_LOCATION) && hasChanged(jobContent.getLocation(), updater.getLocation())) {
            changeLog.add("location", jobContent.getLocation(), updater.getLocation());
            jobContent.setLocation(updater.getLocation());
        }

        if (updater.hasAnyChangesIn(SECTION_OCCUPATIONS) && hasChangedContent(jobContent.getOccupations(), updater.getOccupations())) {
            changeLog.add("occupations", jobContent.getOccupations(), updater.getOccupations());
            jobContent.setOccupations(updater.getOccupations());
        }

        if (updater.hasAnyChangesIn(SECTION_LANGUAGE_SKILLS) && hasChangedContent(jobContent.getLanguageSkills(), updater.getLanguageSkills())) {
            changeLog.add("languageSkills", jobContent.getLanguageSkills(), updater.getLanguageSkills());
            jobContent.setLanguageSkills(updater.getLanguageSkills());
        }

        if (updater.hasAnyChangesIn(SECTION_APPLY_CHANNEL) && hasChanged(jobContent.getApplyChannel(), updater.getApplyChannel())) {
            changeLog.add("applyChannel", jobContent.getApplyChannel(), updater.getApplyChannel());
            jobContent.setApplyChannel(updater.getApplyChannel());
        }

        if (updater.hasAnyChangesIn(SECTION_CONTACT) && hasChanged(contact, updater.getContact())) {
            if ((contact != null) && (updater.getContact() != null)) {
                if (hasChanged(contact.getSalutation(), updater.getContact().getSalutation()) ||
                        hasChanged(contact.getFirstName(), updater.getContact().getFirstName()) ||
                        hasChanged(contact.getLastName(), updater.getContact().getLastName()) ||
                        hasChanged(contact.getEmail(), updater.getContact().getEmail()) ||
                        hasChanged(contact.getPhone(), updater.getContact().getPhone())) {
                    changeLog.add("contact", contact, updater.getContact());
                    Locale language = contact.getLanguage();
                    contact = updater.getContact();
                    contact.setLanguage(language);
                }
            } else {
                changeLog.add("contact", contact, updater.getContact());
                contact = updater.getContact();
            }
        }

        if (updater.hasAnyChangesIn(SECTION_PUBLICATION) && hasChanged(publication, updater.getPublication())) {
            changeLog.add("publication", publication, updater.getPublication());
            publication = updater.getPublication();
        }

        return changeLog;
    }

    public static final class Builder {
        private JobAdvertisementId id;
        private JobAdvertisementStatus status;
        private SourceSystem sourceSystem;
        private String externalReference;
        private String stellennummerEgov;
        private String stellennummerAvam;
        private String fingerprint;
        private boolean reportingObligation;
        private LocalDate reportingObligationEndDate;
        private boolean reportToAvam;
        private String jobCenterCode;
        private LocalDate approvalDate;
        private LocalDate rejectionDate;
        private String rejectionCode;
        private String rejectionReason;
        private LocalDate cancellationDate;
        private CancellationCode cancellationCode;
        private JobContent jobContent;
        private Owner owner;
        private Contact contact;
        private Publication publication;

        public Builder() {
        }

        public JobAdvertisement build() {
            return new JobAdvertisement(this);
        }

        public Builder setId(JobAdvertisementId id) {
            this.id = id;
            return this;
        }

        public Builder setStatus(JobAdvertisementStatus status) {
            this.status = status;
            return this;
        }

        public Builder setSourceSystem(SourceSystem sourceSystem) {
            this.sourceSystem = sourceSystem;
            return this;
        }

        public Builder setExternalReference(String externalReference) {
            this.externalReference = externalReference;
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

        public Builder setReportingObligation(boolean reportingObligation) {
            this.reportingObligation = reportingObligation;
            return this;
        }

        public Builder setReportingObligationEndDate(LocalDate reportingObligationEndDate) {
            this.reportingObligationEndDate = reportingObligationEndDate;
            return this;
        }

        public Builder setReportToAvam(boolean reportToAvam) {
            this.reportToAvam = reportToAvam;
            return this;
        }

        public Builder setJobCenterCode(String jobCenterCode) {
            this.jobCenterCode = jobCenterCode;
            return this;
        }

        public Builder setApprovalDate(LocalDate approvalDate) {
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

        public Builder setCancellationCode(CancellationCode cancellationCode) {
            this.cancellationCode = cancellationCode;
            return this;
        }

        public Builder setJobContent(JobContent jobContent) {
            this.jobContent = jobContent;
            return this;
        }

        public Builder setOwner(Owner owner) {
            this.owner = owner;
            return this;
        }

        public Builder setContact(Contact contact) {
            this.contact = contact;
            return this;
        }

        public Builder setPublication(Publication publication) {
            this.publication = publication;
            return this;
        }
    }
}
