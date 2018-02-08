package ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement;

import ch.admin.seco.jobs.services.jobadservice.core.domain.TestDataProvider;

import java.util.ArrayList;
import java.util.List;

public class JobAdvertisementTestDataProvider implements TestDataProvider<JobAdvertisement> {

    public static final JobAdvertisementId JOB_ADVERTISEMENT_ID_01 = new JobAdvertisementId("job01");

    private List<JobAdvertisement> jobAdvertisements;

    @Override
    public List<JobAdvertisement> getTestData() {
        if (jobAdvertisements == null) {
            jobAdvertisements = new ArrayList<>();
            jobAdvertisements.add(createJob01());
        }
        return jobAdvertisements;
    }

    private JobAdvertisement createJob01() {
        return new JobAdvertisement(
                JOB_ADVERTISEMENT_ID_01,
                JobAdvertisementStatus.CREATED,
                "Job number 1",
                "This is the job number 1 with the minimum of data"
        );
    }

}
