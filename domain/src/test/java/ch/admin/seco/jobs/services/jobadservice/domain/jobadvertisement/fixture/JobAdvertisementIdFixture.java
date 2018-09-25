package ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.fixture;

import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisementId;

public enum JobAdvertisementIdFixture {
    job01, job02, job03, job04, job05, job06, job07, job08, job09, job10;

    private final JobAdvertisementId id;

    JobAdvertisementIdFixture() {
        this.id = new JobAdvertisementId(name());
    }

    public JobAdvertisementId id() {
        return id;
    }
}
