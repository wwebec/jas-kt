package ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.fixture;

import static java.lang.String.format;
import static java.util.Locale.GERMAN;

import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisementId;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobDescription;

public class JobDescriptionTextFixture {

    public static JobDescription testJobDescription(String title, String description) {
        return new JobDescription.Builder()
                .setLanguage(GERMAN)
                .setTitle(title)
                .setDescription(description)
                .build();
    }

    public static JobDescription testJobDescription(JobAdvertisementId jobAdvertisementId) {
        return new JobDescription.Builder()
                .setLanguage(GERMAN)
                .setTitle(format("title-%s", jobAdvertisementId.getValue()))
                .setDescription(format("description-%s", jobAdvertisementId.getValue()))
                .build();
    }
}
