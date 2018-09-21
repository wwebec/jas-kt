package ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.fixture;

import static java.lang.String.format;
import static java.util.Locale.GERMAN;

import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisementId;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobDescription.Builder;

public class JobDescriptionFixture {

    static Builder of(JobAdvertisementId id){
        return testJobDescription()
                .setTitle(format("title-%s", id.getValue()))
                .setDescription(format("description-%s", id.getValue()));
    }

    public static Builder testJobDescriptionEmpty() {
        return new Builder();
    }

    public static Builder testJobDescription() {
        return testJobDescriptionEmpty()
                .setLanguage(GERMAN)
                .setTitle("title")
                .setDescription("description");
    }

}
