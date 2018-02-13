package ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement;

public interface JobAdvertisementRepository {

    JobAdvertisement findById(JobAdvertisementId jobAdvertisementId);

    JobAdvertisement save(JobAdvertisement jobAdvertisement);

}
