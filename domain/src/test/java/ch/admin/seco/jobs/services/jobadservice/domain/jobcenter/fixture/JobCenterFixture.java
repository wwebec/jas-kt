package ch.admin.seco.jobs.services.jobadservice.domain.jobcenter.fixture;

import static ch.admin.seco.jobs.services.jobadservice.domain.jobcenter.fixture.JobCenterAddressFixture.*;

import ch.admin.seco.jobs.services.jobadservice.domain.jobcenter.JobCenter;

public class JobCenterFixture {
    public static JobCenter testJobCenter() {
        return new JobCenter("id", "code", "email", "phone", "fax", false, testJobCenterAddress());
    }
}
