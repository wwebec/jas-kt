package ch.admin.seco.jobs.services.jobadservice.domain.jobcenter.fixture;

import ch.admin.seco.jobs.services.jobadservice.domain.jobcenter.JobCenter;

public class JobCenterTestFixture {

    public static final String JOB_CENTER_CODE = "jobCenterCode";

    public static JobCenter testJobCenter() {
        return new JobCenter(
                "jobCenterId",
                JOB_CENTER_CODE,
                "email",
                "phone",
                "jobCenterFax",
                true,
                JobCenterAddressTestFixture.testJobCenterAddress()
        );
    }

}
