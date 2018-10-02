package ch.admin.seco.jobs.services.jobadservice.domain.jobcenter.fixture;

import static ch.admin.seco.jobs.services.jobadservice.domain.jobcenter.fixture.JobCenterAddressFixture.*;

import ch.admin.seco.jobs.services.jobadservice.domain.jobcenter.JobCenter;

public class JobCenterFixture {
    public static JobCenter testJobCenter() {
        return new JobCenter("jobCenter-id", "jobCenter-code", "jobCenter-email", "jobCenter-phone", "jobCenter-fax", false, testJobCenterAddress());
    }
}
