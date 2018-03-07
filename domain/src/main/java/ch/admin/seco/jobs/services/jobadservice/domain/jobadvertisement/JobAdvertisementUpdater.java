package ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ch.admin.seco.jobs.services.jobadservice.core.domain.events.AuditUser;

public class JobAdvertisementUpdater {

    static final String SECTION_FINGERPRINT = "SECTION_FINGERPRINT";
    static final String SECTION_SOURCE_ENTRY_ID = "SECTION_SOURCE_ENTRY_ID";
    static final String SECTION_EXTERNAL_URL = "SECTION_EXTERNAL_URL";
    static final String SECTION_REPORTING_OBLIGATION = "SECTION_REPORTING_OBLIGATION";
    static final String SECTION_PUBLICATION_DATES = "SECTION_PUBLICATION_DATES";
    static final String SECTION_EURES = "SECTION_EURES";
    static final String SECTION_EMPLOYMENT = "SECTION_EMPLOYMENT";
    static final String SECTION_JOB_CENTER_CODE = "SECTION_JOB_CENTER_CODE";
    static final String SECTION_DRIVING_LICENSE_LEVEL = "SECTION_DRIVING_LICENSE_LEVEL";
    static final String SECTION_APPLY_CHANNEL = "SECTION_APPLY_CHANNEL";
    static final String SECTION_COMPANY = "SECTION_COMPANY";
    static final String SECTION_CONTACT = "SECTION_CONTACT";
    static final String SECTION_LOCATION = "SECTION_LOCATION";
    static final String SECTION_OCCUPATIONS = "SECTION_OCCUPATIONS";
    static final String SECTION_LANGUAGE_SKILLS = "SECTION_LANGUAGE_SKILLS";

    private Set<String> changedSections;

    private AuditUser auditUser;

    private String fingerprint;

    private String sourceEntryId;

    private String externalUrl;

    private boolean reportingObligation;

    private LocalDate publicationStartDate;

    private LocalDate publicationEndDate;

    private boolean eures;

    private boolean euresAnonymous;

    private Employment employment;

    private String jobCenterCode;

    private String drivingLicenseLevel;

    private ApplyChannel applyChannel;

    private Company company;

    private Contact contact;

    private Location location;

    private List<Occupation> occupations;

    private List<LanguageSkill> languageSkills;

    public JobAdvertisementUpdater(Builder builder) {
        this.changedSections = builder.changedSections;
        this.auditUser = builder.auditUser;
        this.fingerprint = builder.fingerprint;
        this.sourceEntryId = builder.sourceEntryId;
        this.externalUrl = builder.externalUrl;
        this.reportingObligation = builder.reportingObligation;
        this.publicationStartDate = builder.publicationStartDate;
        this.publicationEndDate = builder.publicationEndDate;
        this.eures = builder.eures;
        this.euresAnonymous = builder.euresAnonymous;
        this.employment = builder.employment;
        this.jobCenterCode = builder.jobCenterCode;
        this.drivingLicenseLevel = builder.drivingLicenseLevel;
        this.applyChannel = builder.applyChannel;
        this.company = builder.company;
        this.contact = builder.contact;
        this.location = builder.location;
        this.occupations = builder.occupations;
        this.languageSkills = builder.languageSkills;
    }

    public boolean hasAnyChangesIn(String section) {
        return changedSections.contains(section);
    }

    public AuditUser getAuditUser() {
        return auditUser;
    }

    public String getFingerprint() {
        return fingerprint;
    }

    public String getSourceEntryId() {
        return sourceEntryId;
    }

    public String getExternalUrl() {
        return externalUrl;
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

    public Location getLocation() {
        return location;
    }

    public List<Occupation> getOccupations() {
        return occupations;
    }

    public List<LanguageSkill> getLanguageSkills() {
        return languageSkills;
    }

    public static class Builder {

        private Set<String> changedSections = new HashSet<>();
        private AuditUser auditUser;
        private String fingerprint;
        private String sourceEntryId;
        private String externalUrl;
        private boolean reportingObligation;
        private LocalDate publicationStartDate;
        private LocalDate publicationEndDate;
        private boolean eures;
        private boolean euresAnonymous;
        private Employment employment;
        private String jobCenterCode;
        private String drivingLicenseLevel;
        private ApplyChannel applyChannel;
        private Company company;
        private Contact contact;
        private Location location;
        private List<Occupation> occupations;
        private List<LanguageSkill> languageSkills;

        public Builder(AuditUser auditUser) {
            this.auditUser = auditUser;
        }

        public JobAdvertisementUpdater build() {
            return new JobAdvertisementUpdater(this);
        }

        public Builder setFingerprint(String fingerprint) {
            this.changedSections.add(SECTION_FINGERPRINT);
            this.fingerprint = fingerprint;
            return this;
        }

        public Builder setSourceEntryId(String sourceEntryId) {
            this.changedSections.add(SECTION_SOURCE_ENTRY_ID);
            this.sourceEntryId = sourceEntryId;
            return this;
        }

        public Builder setExternalUrl(String externalUrl) {
            this.changedSections.add(SECTION_EXTERNAL_URL);
            this.externalUrl = externalUrl;
            return this;
        }

        public Builder setReportingObligation(boolean reportingObligation) {
            this.changedSections.add(SECTION_REPORTING_OBLIGATION);
            this.reportingObligation = reportingObligation;
            return this;
        }

        public Builder setPublicationDates(LocalDate publicationStartDate, LocalDate publicationEndDate) {
            this.changedSections.add(SECTION_PUBLICATION_DATES);
            this.publicationStartDate = publicationStartDate;
            this.publicationEndDate = publicationEndDate;
            return this;
        }

        public Builder setEures(boolean eures, boolean anonymous) {
            this.changedSections.add(SECTION_EURES);
            this.eures = !anonymous && eures;
            this.euresAnonymous = anonymous && eures;
            return this;
        }

        public Builder setEmployment(Employment employment) {
            this.changedSections.add(SECTION_EMPLOYMENT);
            this.employment = employment;
            return this;
        }

        public Builder setJobCenterCode(String jobCenterCode) {
            this.changedSections.add(SECTION_JOB_CENTER_CODE);
            this.jobCenterCode = jobCenterCode;
            return this;
        }

        public Builder setDrivingLicenseLevel(String drivingLicenseLevel) {
            this.changedSections.add(SECTION_DRIVING_LICENSE_LEVEL);
            this.drivingLicenseLevel = drivingLicenseLevel;
            return this;
        }

        public Builder setApplyChannel(ApplyChannel applyChannel) {
            this.changedSections.add(SECTION_APPLY_CHANNEL);
            this.applyChannel = applyChannel;
            return this;
        }

        public Builder setCompany(Company company) {
            this.changedSections.add(SECTION_COMPANY);
            this.company = company;
            return this;
        }

        public Builder setContact(Contact contact) {
            this.changedSections.add(SECTION_CONTACT);
            this.contact = contact;
            return this;
        }

        public Builder setLocation(Location location) {
            this.changedSections.add(SECTION_LOCATION);
            this.location = location;
            return this;
        }

        public Builder setOccupations(List<Occupation> occupations) {
            this.changedSections.add(SECTION_OCCUPATIONS);
            this.occupations = occupations;
            return this;
        }

        public Builder setLanguageSkills(List<LanguageSkill> languageSkills) {
            this.changedSections.add(SECTION_LANGUAGE_SKILLS);
            this.languageSkills = languageSkills;
            return this;
        }
    }

}
