package ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement;

import ch.admin.seco.jobs.services.jobadservice.core.domain.events.AuditUser;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    static final String SECTION_LOCALITY = "SECTION_LOCALITY";
    static final String SECTION_OCCUPATIONS = "SECTION_OCCUPATIONS";
    static final String SECTION_LANGUAGE_SKILLS = "SECTION_LANGUAGE_SKILLS";
    static final String SECTION_PROFESSION_CODES = "SECTION_PROFESSION_CODES";

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

    private LocalDate employmentStartDate;

    private LocalDate employmentEndDate;

    private Integer durationInDays;

    private Boolean immediately;

    private Boolean permanent;

    private int workloadPercentageMin;

    private int workloadPercentageMax;

    private String jobCenterCode;

    private String drivingLicenseLevel;

    private ApplyChannel applyChannel;

    private Company company;

    private Contact contact;

    private Locality locality;

    private List<Occupation> occupations;

    private List<LanguageSkill> languageSkills;

    private List<String> professionCodes;

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
        this.employmentStartDate = builder.employmentStartDate;
        this.employmentEndDate = builder.employmentEndDate;
        this.durationInDays = builder.durationInDays;
        this.immediately = builder.immediately;
        this.permanent = builder.permanent;
        this.workloadPercentageMin = builder.workloadPercentageMin;
        this.workloadPercentageMax = builder.workloadPercentageMax;
        this.jobCenterCode = builder.jobCenterCode;
        this.drivingLicenseLevel = builder.drivingLicenseLevel;
        this.applyChannel = builder.applyChannel;
        this.company = builder.company;
        this.contact = builder.contact;
        this.locality = builder.locality;
        this.occupations = builder.occupations;
        this.languageSkills = builder.languageSkills;
        this.professionCodes = builder.professionCodes;
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

    public Locality getLocality() {
        return locality;
    }

    public List<Occupation> getOccupations() {
        return occupations;
    }

    public List<LanguageSkill> getLanguageSkills() {
        return languageSkills;
    }

    public List<String> getProfessionCodes() {
        return professionCodes;
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
        private LocalDate employmentStartDate;
        private LocalDate employmentEndDate;
        private Integer durationInDays;
        private Boolean immediately;
        private Boolean permanent;
        private int workloadPercentageMin;
        private int workloadPercentageMax;
        private String jobCenterCode;
        private String drivingLicenseLevel;
        private ApplyChannel applyChannel;
        private Company company;
        private Contact contact;
        private Locality locality;
        private List<Occupation> occupations;
        private List<LanguageSkill> languageSkills;
        private List<String> professionCodes;

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

        public Builder setEmployment(LocalDate employmentStartDate, LocalDate employmentEndDate, Integer durationInDays, Boolean immediately, Boolean permanent, int workloadPercentageMin, int workloadPercentageMax) {
            this.changedSections.add(SECTION_EMPLOYMENT);
            this.employmentStartDate = employmentStartDate;
            this.employmentEndDate = employmentEndDate;
            this.durationInDays = durationInDays;
            this.immediately = immediately;
            this.permanent = permanent;
            this.workloadPercentageMin = workloadPercentageMin;
            this.workloadPercentageMax = workloadPercentageMax;
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

        public Builder setLocality(Locality locality) {
            this.changedSections.add(SECTION_LOCALITY);
            this.locality = locality;
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

        public Builder setProfessionCodes(List<String> professionCodes) {
            this.changedSections.add(SECTION_PROFESSION_CODES);
            this.professionCodes = professionCodes;
            return this;
        }
    }

}
