package ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement;

import ch.admin.seco.jobs.services.jobadservice.core.domain.events.AuditUser;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class JobAdvertisementCreator {

    private AuditUser auditUser;
    private String sourceEntryId;
    private String stellennummerEgov;
    private String stellennummerAvam;
    private String fingerprint;
    private boolean reportingObligation;
    private LocalDate reportingObligationEndDate;
    private boolean reportToAvam;
    private String jobCenterCode;
    private JobContent jobContent;
    private Contact contact;
    private Publication publication;

    public JobAdvertisementCreator(Builder builder) {
        this.auditUser = builder.auditUser;
        this.sourceEntryId = builder.sourceEntryId;
        this.stellennummerEgov = builder.stellennummerEgov;
        this.stellennummerAvam = builder.stellennummerAvam;
        this.fingerprint = builder.fingerprint;
        this.reportingObligation = builder.reportingObligation;
        this.reportingObligationEndDate = builder.reportingObligationEndDate;
        this.reportToAvam = builder.reportToAvam;
        this.jobCenterCode = builder.jobCenterCode;
        this.jobContent = builder.jobContent;
        this.contact = builder.contact;
        this.publication = builder.publication;
    }

    public AuditUser getAuditUser() {
        return auditUser;
    }

    public String getSourceEntryId() {
        return sourceEntryId;
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

    public JobContent getJobContent() {
        return jobContent;
    }

    public Contact getContact() {
        return contact;
    }

    public Publication getPublication() {
        return publication;
    }

    public static class Builder {
        private AuditUser auditUser;
        private String sourceEntryId;
        private String stellennummerEgov;
        private String stellennummerAvam;
        private String fingerprint;
        private boolean reportingObligation;
        private LocalDate reportingObligationEndDate;
        private boolean reportToAvam;
        private String jobCenterCode;
        private JobContent jobContent;
        private Contact contact;
        private Publication publication;

        public Builder(AuditUser auditUser) {
            this.auditUser = auditUser;
        }

        public JobAdvertisementCreator build() {
            return new JobAdvertisementCreator(this);
        }

        public Builder setSourceEntryId(String sourceEntryId) {
            this.sourceEntryId = sourceEntryId;
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

        public Builder setJobContent(JobContent jobContent) {
            this.jobContent = jobContent;
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