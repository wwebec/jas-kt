package ch.admin.seco.jobs.services.jobadservice.domain.jobcenter.fixture;

import ch.admin.seco.jobs.services.jobadservice.domain.jobcenter.JobCenterAddress;

public class JobCenterAddressFixture {
    public static JobCenterAddress testJobCenterAddress() {
        return new JobCenterAddress(
                "name",
                "city",
                "street",
                "houseNumber",
                "postalCode"
        );
    }
}
