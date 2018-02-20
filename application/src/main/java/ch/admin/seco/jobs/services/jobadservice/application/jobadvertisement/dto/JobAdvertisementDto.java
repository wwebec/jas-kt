package ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto;

import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisement;

import java.time.LocalDate;
import java.util.List;

public class JobAdvertisementDto {

    private String id;
    private String stellennummerAvam;
    private String fingerprint;
    private String sourceSystem;
    private String sourceEntryId;
    private String externalUrl;
    private String status;
    private LocalDate registrationDate;
    private LocalDate rejectionDate;
    private String rejectionCode;
    private String rejectionReason;
    private LocalDate cancellationDate;
    private String cancellationCode;
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
    private Integer numberOfJobs; // TODO check if used anywhere outside JobRoom
    private Boolean accessibly; // TODO Add this in JobRoom (Improvement-Issue)
    private Boolean jobSharing; // TODO check if used anywhere outside JobRoom
    private Boolean hasPersonalVehicle; // TODO check if used anywhere outside JobRoom
    private String jobCenterCode;
    private String drivingLicenseLevel;
    private ApplyChannelDto applyChannel;
    private CompanyDto company;
    private ContactDto contact;
    private List<LocalityDto> localities;
    private List<OccupationDto> occupations;
    private String educationCode;
    private List<LanguageSkillDto> languageSkills;
    private List<String> professionCodes;

    protected JobAdvertisementDto() {
        // For reflection libs
    }

    public JobAdvertisementDto(String id, String stellennummerAvam, String fingerprint, String sourceSystem, String sourceEntryId, String externalUrl, String status, LocalDate registrationDate, LocalDate rejectionDate, String rejectionCode, String rejectionReason, LocalDate cancellationDate, String cancellationCode, LocalDate publicationStartDate, LocalDate publicationEndDate, boolean eures, boolean euresAnonymous, String title, String description, LocalDate employmentStartDate, LocalDate employmentEndDate, Integer durationInDays, Boolean immediately, Boolean permanent, int workloadPercentageMin, int workloadPercentageMax, Integer numberOfJobs, Boolean accessibly, Boolean jobSharing, Boolean hasPersonalVehicle, String jobCenterCode, String drivingLicenseLevel, ApplyChannelDto applyChannel, CompanyDto company, ContactDto contact, List<LocalityDto> localities, List<OccupationDto> occupations, String educationCode, List<LanguageSkillDto> languageSkills, List<String> professionCodes) {
        this.id = id;
        this.stellennummerAvam = stellennummerAvam;
        this.fingerprint = fingerprint;
        this.sourceSystem = sourceSystem;
        this.sourceEntryId = sourceEntryId;
        this.externalUrl = externalUrl;
        this.status = status;
        this.registrationDate = registrationDate;
        this.rejectionDate = rejectionDate;
        this.rejectionCode = rejectionCode;
        this.rejectionReason = rejectionReason;
        this.cancellationDate = cancellationDate;
        this.cancellationCode = cancellationCode;
        this.publicationStartDate = publicationStartDate;
        this.publicationEndDate = publicationEndDate;
        this.eures = eures;
        this.euresAnonymous = euresAnonymous;
        this.title = title;
        this.description = description;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStellennummerAvam() {
        return stellennummerAvam;
    }

    public void setStellennummerAvam(String stellennummerAvam) {
        this.stellennummerAvam = stellennummerAvam;
    }

    public String getFingerprint() {
        return fingerprint;
    }

    public void setFingerprint(String fingerprint) {
        this.fingerprint = fingerprint;
    }

    public String getSourceSystem() {
        return sourceSystem;
    }

    public void setSourceSystem(String sourceSystem) {
        this.sourceSystem = sourceSystem;
    }

    public String getSourceEntryId() {
        return sourceEntryId;
    }

    public void setSourceEntryId(String sourceEntryId) {
        this.sourceEntryId = sourceEntryId;
    }

    public String getExternalUrl() {
        return externalUrl;
    }

    public void setExternalUrl(String externalUrl) {
        this.externalUrl = externalUrl;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDate getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(LocalDate registrationDate) {
        this.registrationDate = registrationDate;
    }

    public LocalDate getRejectionDate() {
        return rejectionDate;
    }

    public void setRejectionDate(LocalDate rejectionDate) {
        this.rejectionDate = rejectionDate;
    }

    public String getRejectionCode() {
        return rejectionCode;
    }

    public void setRejectionCode(String rejectionCode) {
        this.rejectionCode = rejectionCode;
    }

    public String getRejectionReason() {
        return rejectionReason;
    }

    public void setRejectionReason(String rejectionReason) {
        this.rejectionReason = rejectionReason;
    }

    public LocalDate getCancellationDate() {
        return cancellationDate;
    }

    public void setCancellationDate(LocalDate cancellationDate) {
        this.cancellationDate = cancellationDate;
    }

    public String getCancellationCode() {
        return cancellationCode;
    }

    public void setCancellationCode(String cancellationCode) {
        this.cancellationCode = cancellationCode;
    }

    public LocalDate getPublicationStartDate() {
        return publicationStartDate;
    }

    public void setPublicationStartDate(LocalDate publicationStartDate) {
        this.publicationStartDate = publicationStartDate;
    }

    public LocalDate getPublicationEndDate() {
        return publicationEndDate;
    }

    public void setPublicationEndDate(LocalDate publicationEndDate) {
        this.publicationEndDate = publicationEndDate;
    }

    public boolean isEures() {
        return eures;
    }

    public void setEures(boolean eures) {
        this.eures = eures;
    }

    public boolean isEuresAnonymous() {
        return euresAnonymous;
    }

    public void setEuresAnonymous(boolean euresAnonymous) {
        this.euresAnonymous = euresAnonymous;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getEmploymentStartDate() {
        return employmentStartDate;
    }

    public void setEmploymentStartDate(LocalDate employmentStartDate) {
        this.employmentStartDate = employmentStartDate;
    }

    public LocalDate getEmploymentEndDate() {
        return employmentEndDate;
    }

    public void setEmploymentEndDate(LocalDate employmentEndDate) {
        this.employmentEndDate = employmentEndDate;
    }

    public Integer getDurationInDays() {
        return durationInDays;
    }

    public void setDurationInDays(Integer durationInDays) {
        this.durationInDays = durationInDays;
    }

    public Boolean getImmediately() {
        return immediately;
    }

    public void setImmediately(Boolean immediately) {
        this.immediately = immediately;
    }

    public Boolean getPermanent() {
        return permanent;
    }

    public void setPermanent(Boolean permanent) {
        this.permanent = permanent;
    }

    public int getWorkloadPercentageMin() {
        return workloadPercentageMin;
    }

    public void setWorkloadPercentageMin(int workloadPercentageMin) {
        this.workloadPercentageMin = workloadPercentageMin;
    }

    public int getWorkloadPercentageMax() {
        return workloadPercentageMax;
    }

    public void setWorkloadPercentageMax(int workloadPercentageMax) {
        this.workloadPercentageMax = workloadPercentageMax;
    }

    public Integer getNumberOfJobs() {
        return numberOfJobs;
    }

    public void setNumberOfJobs(Integer numberOfJobs) {
        this.numberOfJobs = numberOfJobs;
    }

    public Boolean getAccessibly() {
        return accessibly;
    }

    public void setAccessibly(Boolean accessibly) {
        this.accessibly = accessibly;
    }

    public Boolean getJobSharing() {
        return jobSharing;
    }

    public void setJobSharing(Boolean jobSharing) {
        this.jobSharing = jobSharing;
    }

    public Boolean getHasPersonalVehicle() {
        return hasPersonalVehicle;
    }

    public void setHasPersonalVehicle(Boolean hasPersonalVehicle) {
        this.hasPersonalVehicle = hasPersonalVehicle;
    }

    public String getJobCenterCode() {
        return jobCenterCode;
    }

    public void setJobCenterCode(String jobCenterCode) {
        this.jobCenterCode = jobCenterCode;
    }

    public String getDrivingLicenseLevel() {
        return drivingLicenseLevel;
    }

    public void setDrivingLicenseLevel(String drivingLicenseLevel) {
        this.drivingLicenseLevel = drivingLicenseLevel;
    }

    public ApplyChannelDto getApplyChannel() {
        return applyChannel;
    }

    public void setApplyChannel(ApplyChannelDto applyChannel) {
        this.applyChannel = applyChannel;
    }

    public CompanyDto getCompany() {
        return company;
    }

    public void setCompany(CompanyDto company) {
        this.company = company;
    }

    public ContactDto getContact() {
        return contact;
    }

    public void setContact(ContactDto contact) {
        this.contact = contact;
    }

    public List<LocalityDto> getLocalities() {
        return localities;
    }

    public void setLocalities(List<LocalityDto> localities) {
        this.localities = localities;
    }

    public List<OccupationDto> getOccupations() {
        return occupations;
    }

    public void setOccupations(List<OccupationDto> occupations) {
        this.occupations = occupations;
    }

    public String getEducationCode() {
        return educationCode;
    }

    public void setEducationCode(String educationCode) {
        this.educationCode = educationCode;
    }

    public List<LanguageSkillDto> getLanguageSkills() {
        return languageSkills;
    }

    public void setLanguageSkills(List<LanguageSkillDto> languageSkills) {
        this.languageSkills = languageSkills;
    }

    public List<String> getProfessionCodes() {
        return professionCodes;
    }

    public void setProfessionCodes(List<String> professionCodes) {
        this.professionCodes = professionCodes;
    }

    public static JobAdvertisementDto toDto(JobAdvertisement jobAdvertisement) {
        JobAdvertisementDto jobAdvertisementDto = new JobAdvertisementDto();
        jobAdvertisementDto.setId(jobAdvertisement.getId().getValue());
        jobAdvertisementDto.setStellennummerAvam(jobAdvertisement.getStellennummerAvam());
        jobAdvertisementDto.setFingerprint(jobAdvertisement.getFingerprint());
        jobAdvertisementDto.setSourceSystem(jobAdvertisement.getSourceSystem());
        jobAdvertisementDto.setSourceEntryId(jobAdvertisement.getSourceEntryId());
        jobAdvertisementDto.setExternalUrl(jobAdvertisement.getExternalUrl());
        jobAdvertisementDto.setStatus(jobAdvertisement.getStatus().toString());
        jobAdvertisementDto.setRegistrationDate(jobAdvertisement.getRegistrationDate());
        jobAdvertisementDto.setRejectionDate(jobAdvertisement.getRejectionDate());
        jobAdvertisementDto.setRejectionCode(jobAdvertisement.getRejectionCode());
        jobAdvertisementDto.setRejectionReason(jobAdvertisement.getRejectionReason());
        jobAdvertisementDto.setCancellationDate(jobAdvertisement.getCancellationDate());
        jobAdvertisementDto.setCancellationCode(jobAdvertisement.getCancellationCode());
        jobAdvertisementDto.setPublicationStartDate(jobAdvertisement.getPublicationStartDate());
        jobAdvertisementDto.setPublicationEndDate(jobAdvertisement.getPublicationEndDate());
        jobAdvertisementDto.setEures(jobAdvertisement.isEures());
        jobAdvertisementDto.setEuresAnonymous(jobAdvertisement.isEuresAnonymous());
        jobAdvertisementDto.setTitle(jobAdvertisement.getTitle());
        jobAdvertisementDto.setDescription(jobAdvertisement.getDescription());
        jobAdvertisementDto.setEmploymentStartDate(jobAdvertisement.getEmploymentStartDate());
        jobAdvertisementDto.setEmploymentEndDate(jobAdvertisement.getEmploymentEndDate());
        jobAdvertisementDto.setDurationInDays(jobAdvertisement.getDurationInDays());
        jobAdvertisementDto.setImmediately(jobAdvertisement.getImmediately());
        jobAdvertisementDto.setPermanent(jobAdvertisement.getPermanent());
        jobAdvertisementDto.setWorkloadPercentageMin(jobAdvertisement.getWorkloadPercentageMin());
        jobAdvertisementDto.setWorkloadPercentageMax(jobAdvertisement.getWorkloadPercentageMax());
        jobAdvertisementDto.setNumberOfJobs(jobAdvertisement.getNumberOfJobs());
        jobAdvertisementDto.setAccessibly(jobAdvertisement.getAccessibly());
        jobAdvertisementDto.setJobSharing(jobAdvertisement.getJobSharing());
        jobAdvertisementDto.setHasPersonalVehicle(jobAdvertisement.getHasPersonalVehicle());
        jobAdvertisementDto.setJobCenterCode(jobAdvertisement.getJobCenterCode());
        jobAdvertisementDto.setDrivingLicenseLevel(jobAdvertisement.getDrivingLicenseLevel());
        jobAdvertisementDto.setApplyChannel(ApplyChannelDto.toDto(jobAdvertisement.getApplyChannel()));
        jobAdvertisementDto.setCompany(CompanyDto.toDto(jobAdvertisement.getCompany()));
        jobAdvertisementDto.setContact(ContactDto.toDto(jobAdvertisement.getContact()));
        jobAdvertisementDto.setLocalities(LocalityDto.toDto(jobAdvertisement.getLocalities()));
        jobAdvertisementDto.setOccupations(OccupationDto.toDto(jobAdvertisement.getOccupations()));
        jobAdvertisementDto.setEducationCode(jobAdvertisement.getEducationCode());
        jobAdvertisementDto.setLanguageSkills(LanguageSkillDto.toDto(jobAdvertisement.getLanguageSkills()));
        jobAdvertisementDto.setProfessionCodes(jobAdvertisement.getProfessionCodes());
        return jobAdvertisementDto;
    }
}
