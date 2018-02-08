package ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement;

import ch.admin.seco.jobs.services.jobadservice.core.conditions.Condition;
import ch.admin.seco.jobs.services.jobadservice.core.domain.Aggregate;

import java.time.LocalDate;
import java.util.Set;

public class JobAdvertisement implements Aggregate<JobAdvertisement, JobAdvertisementId> {

    private JobAdvertisementId id;
    private String stellennummerAvam;
    private String stellennummerEgov;
    private String fingerprint;
    private String sourceSystem;
    private String sourceEntryId;
    private String externalViewUrl;
    private String externalApplyUrl;
    private JobAdvertisementStatus status;
    private LocalDate publicationStartDate;
    private LocalDate publicationEndDate;
    private String title;
    private String description;
    private LocalDate employmentStartDate;
    private LocalDate employmentEndDate;
    private Boolean immediately;
    private int workloadPercentageMin;
    private int workloadPercentageMax;
    private Boolean permanent;
    private Integer numberOfJobs; // TODO check if used anywhere outside JobRoom
    private Boolean accessibly; // TODO Add this in JobRoom (Improvement-Issue)
    private Boolean jobSharing; // TODO check if used anywhere outside JobRoom
    private Boolean hasPersonalVehicle; // TODO check if used anywhere outside JobRoom
    private String jobCenterCode;
    private String drivingLicenseLevel;
    private ApplyChannel applyChannel;
    private Company company;
    private Contact contact;
    private Set<Locality> localities;
    private Set<Occupation> occupations;
    private Set<LanguageSkill> languages;

    protected JobAdvertisement() {
    }

    public JobAdvertisement(JobAdvertisementId id, JobAdvertisementStatus status, String title, String description) {
        this.id = Condition.notNull(id);
        this.status = Condition.notNull(status);
        this.title = Condition.notBlank(title);
        this.description = Condition.notBlank(description);
    }

    public JobAdvertisement(JobAdvertisementId id, String stellennummerAvam, String stellennummerEgov, String fingerprint, String sourceSystem, String sourceEntryId, String externalViewUrl, String externalApplyUrl, JobAdvertisementStatus status, LocalDate publicationStartDate, LocalDate publicationEndDate, String title, String description, LocalDate employmentStartDate, LocalDate employmentEndDate, Boolean immediately, int workloadPercentageMin, int workloadPercentageMax, Boolean permanent, Integer numberOfJobs, Boolean accessibly, Boolean jobSharing, Boolean hasPersonalVehicle, String jobCenterCode, String drivingLicenseLevel, ApplyChannel applyChannel, Company company, Contact contact, Set<Locality> localities, Set<Occupation> occupations, Set<LanguageSkill> languages) {
        this(id, status, title, description);
        this.stellennummerAvam = stellennummerAvam;
        this.stellennummerEgov = stellennummerEgov;
        this.fingerprint = fingerprint;
        this.sourceSystem = sourceSystem;
        this.sourceEntryId = sourceEntryId;
        this.externalViewUrl = externalViewUrl;
        this.externalApplyUrl = externalApplyUrl;
        this.publicationStartDate = publicationStartDate;
        this.publicationEndDate = publicationEndDate;
        this.employmentStartDate = employmentStartDate;
        this.employmentEndDate = employmentEndDate;
        this.immediately = immediately;
        this.workloadPercentageMin = workloadPercentageMin;
        this.workloadPercentageMax = workloadPercentageMax;
        this.permanent = permanent;
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
        this.languages = languages;
    }

    @Override
    public boolean isSameAggregateAs(JobAdvertisement other) {
        return (other != null) && id.isSameValueAs(other.id);
    }

    public JobAdvertisementId getId() {
        return id;
    }

    public String getStellennummerAvam() {
        return stellennummerAvam;
    }

    public String getStellennummerEgov() {
        return stellennummerEgov;
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

    public String getExternalViewUrl() {
        return externalViewUrl;
    }

    public String getExternalApplyUrl() {
        return externalApplyUrl;
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

    public Boolean getImmediately() {
        return immediately;
    }

    public int getWorkloadPercentageMin() {
        return workloadPercentageMin;
    }

    public int getWorkloadPercentageMax() {
        return workloadPercentageMax;
    }

    public Boolean getPermanent() {
        return permanent;
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

    public Set<Locality> getLocalities() {
        return localities;
    }

    public Set<Occupation> getOccupations() {
        return occupations;
    }

    public Set<LanguageSkill> getLanguages() {
        return languages;
    }
}
