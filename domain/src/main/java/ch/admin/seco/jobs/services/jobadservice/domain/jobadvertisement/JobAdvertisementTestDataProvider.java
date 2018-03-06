package ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement;

import java.util.ArrayList;
import java.util.List;

import ch.admin.seco.jobs.services.jobadservice.core.domain.TestDataProvider;

public class JobAdvertisementTestDataProvider implements TestDataProvider<JobAdvertisement> {

    public static final JobAdvertisementId JOB_ADVERTISEMENT_ID_01 = new JobAdvertisementId("job01");
    public static final JobAdvertisementId JOB_ADVERTISEMENT_ID_02 = new JobAdvertisementId("job02");
    public static final JobAdvertisementId JOB_ADVERTISEMENT_ID_03 = new JobAdvertisementId("job03");
    public static final JobAdvertisementId JOB_ADVERTISEMENT_ID_04 = new JobAdvertisementId("job04");
    public static final JobAdvertisementId JOB_ADVERTISEMENT_ID_05 = new JobAdvertisementId("job05");


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
                SourceSystem.JOBROOM,
                JobAdvertisementStatus.CREATED,
                "Job number 1",
                "This is the job number 1 with the minimum of data"
        );
    }

}
