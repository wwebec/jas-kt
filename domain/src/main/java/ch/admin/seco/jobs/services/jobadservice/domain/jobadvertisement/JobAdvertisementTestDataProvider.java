package ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement;

import ch.admin.seco.jobs.services.jobadservice.core.domain.TestDataProvider;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

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
        List<JobDescription> jobDescriptions = Collections.singletonList(new JobDescription(
                Locale.GERMAN, "Job number 1", "This is the job number 1 with the minimum of data"));
        JobContent jobContent = new JobContent.Builder()
                .setJobDescriptions(jobDescriptions)
                .build();
        return new JobAdvertisement.Builder()
                .setId(JOB_ADVERTISEMENT_ID_01)
                .setSourceSystem(SourceSystem.JOBROOM)
                .setStatus(JobAdvertisementStatus.CREATED)
                .setJobContent(jobContent)
                .build();

    }

}
