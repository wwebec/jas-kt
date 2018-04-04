package ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto;

import java.time.LocalDate;
import java.util.HashSet;

import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisement;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisementStatus;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.SourceSystem;

public class JobAdvertisementDto {

    private String id;
    private JobAdvertisementStatus status;
    private SourceSystem sourceSystem;
    private String sourceEntryId;
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
    private String cancellationCode;
    private JobContentDto jobContent;
    private OwnerDto owner;
    private ContactDto contact;
    private PublicationDto publication;

    protected JobAdvertisementDto() {
        // For reflection libs
    }

    public JobAdvertisementDto(String id, JobAdvertisementStatus status, SourceSystem sourceSystem, String sourceEntryId, String stellennummerEgov, String stellennummerAvam, String fingerprint, boolean reportingObligation, LocalDate reportingObligationEndDate, boolean reportToAvam, String jobCenterCode, LocalDate approvalDate, LocalDate rejectionDate, String rejectionCode, String rejectionReason, LocalDate cancellationDate, String cancellationCode, JobContentDto jobContent, OwnerDto owner, ContactDto contact, PublicationDto publication) {
        this.id = id;
        this.status = status;
        this.sourceSystem = sourceSystem;
        this.sourceEntryId = sourceEntryId;
        this.stellennummerEgov = stellennummerEgov;
        this.stellennummerAvam = stellennummerAvam;
        this.fingerprint = fingerprint;
        this.reportingObligation = reportingObligation;
        this.reportingObligationEndDate = reportingObligationEndDate;
        this.reportToAvam = reportToAvam;
        this.jobCenterCode = jobCenterCode;
        this.approvalDate = approvalDate;
        this.rejectionDate = rejectionDate;
        this.rejectionCode = rejectionCode;
        this.rejectionReason = rejectionReason;
        this.cancellationDate = cancellationDate;
        this.cancellationCode = cancellationCode;
        this.jobContent = jobContent;
        this.owner = owner;
        this.contact = contact;
        this.publication = publication;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public JobAdvertisementStatus getStatus() {
        return status;
    }

    public void setStatus(JobAdvertisementStatus status) {
        this.status = status;
    }

    public SourceSystem getSourceSystem() {
        return sourceSystem;
    }

    public void setSourceSystem(SourceSystem sourceSystem) {
        this.sourceSystem = sourceSystem;
    }

    public String getSourceEntryId() {
        return sourceEntryId;
    }

    public void setSourceEntryId(String sourceEntryId) {
        this.sourceEntryId = sourceEntryId;
    }

    public String getStellennummerEgov() {
        return stellennummerEgov;
    }

    public void setStellennummerEgov(String stellennummerEgov) {
        this.stellennummerEgov = stellennummerEgov;
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

    public boolean isReportingObligation() {
        return reportingObligation;
    }

    public void setReportingObligation(boolean reportingObligation) {
        this.reportingObligation = reportingObligation;
    }

    public LocalDate getReportingObligationEndDate() {
        return reportingObligationEndDate;
    }

    public void setReportingObligationEndDate(LocalDate reportingObligationEndDate) {
        this.reportingObligationEndDate = reportingObligationEndDate;
    }

    public boolean isReportToAvam() {
        return reportToAvam;
    }

    public void setReportToAvam(boolean reportToAvam) {
        this.reportToAvam = reportToAvam;
    }

    public String getJobCenterCode() {
        return jobCenterCode;
    }

    public void setJobCenterCode(String jobCenterCode) {
        this.jobCenterCode = jobCenterCode;
    }

    public LocalDate getApprovalDate() {
        return approvalDate;
    }

    public void setApprovalDate(LocalDate approvalDate) {
        this.approvalDate = approvalDate;
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

    public JobContentDto getJobContent() {
        return jobContent;
    }

    public void setJobContent(JobContentDto jobContent) {
        this.jobContent = jobContent;
    }

    public OwnerDto getOwner() {
        return owner;
    }

    public void setOwner(OwnerDto owner) {
        this.owner = owner;
    }

    public ContactDto getContact() {
        return contact;
    }

    public void setContact(ContactDto contact) {
        this.contact = contact;
    }

    public PublicationDto getPublication() {
        return publication;
    }

    public void setPublication(PublicationDto publication) {
        this.publication = publication;
    }

    public static JobAdvertisementDto toDto(JobAdvertisement jobAdvertisement) {
        JobAdvertisementDto jobAdvertisementDto = new JobAdvertisementDto();
        jobAdvertisementDto.setId(jobAdvertisement.getId().getValue());
        jobAdvertisementDto.setStatus(jobAdvertisement.getStatus());
        jobAdvertisementDto.setSourceSystem(jobAdvertisement.getSourceSystem());
        jobAdvertisementDto.setSourceEntryId(jobAdvertisement.getSourceEntryId());
        jobAdvertisementDto.setStellennummerEgov(jobAdvertisement.getStellennummerEgov());
        jobAdvertisementDto.setStellennummerAvam(jobAdvertisement.getStellennummerAvam());
        jobAdvertisementDto.setFingerprint(jobAdvertisement.getFingerprint());
        jobAdvertisementDto.setReportingObligation(jobAdvertisement.isReportingObligation());
        jobAdvertisementDto.setReportingObligationEndDate(jobAdvertisement.getReportingObligationEndDate());
        jobAdvertisementDto.setReportToAvam(jobAdvertisement.isReportToAvam());
        jobAdvertisementDto.setJobCenterCode(jobAdvertisement.getJobCenterCode());
        jobAdvertisementDto.setApprovalDate(jobAdvertisement.getApprovalDate());
        jobAdvertisementDto.setRejectionDate(jobAdvertisement.getRejectionDate());
        jobAdvertisementDto.setRejectionCode(jobAdvertisement.getRejectionCode());
        jobAdvertisementDto.setRejectionReason(jobAdvertisement.getRejectionReason());
        jobAdvertisementDto.setCancellationDate(jobAdvertisement.getCancellationDate());
        jobAdvertisementDto.setCancellationCode(jobAdvertisement.getCancellationCode());
        jobAdvertisementDto.setJobContent(JobContentDto.toDto(jobAdvertisement.getJobContent()));
        jobAdvertisementDto.setOwner(OwnerDto.toDto(jobAdvertisement.getOwner()));
        jobAdvertisementDto.setContact(ContactDto.toDto(jobAdvertisement.getContact()));
        jobAdvertisementDto.setPublication(PublicationDto.toDto(jobAdvertisement.getPublication()));

        // Eager load data from ElementCollection
        jobAdvertisementDto.getJobContent().getEmployment().setWorkForms(new HashSet<>(jobAdvertisement.getJobContent().getEmployment().getWorkForms()));
        return jobAdvertisementDto;
    }
}
