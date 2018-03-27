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

    public static Contact createContact(JobAdvertisementId jobAdvertisementId) {
        return new Contact.Builder()
                .setLanguage(Locale.GERMAN)
                .setSalutation(Salutation.MR)
                .setEmail(String.format("mail-%s@mail.com", jobAdvertisementId.getValue()))
                .setPhone(String.format("+41 %s", jobAdvertisementId.getValue()))
                .setFirstName(String.format("first-name-%s", jobAdvertisementId.getValue()))
                .setLastName(String.format("last-name-%s", jobAdvertisementId.getValue()))
                .build();
    }

    public static Owner createOwner(JobAdvertisementId jobAdvertisementId) {
        return new Owner.Builder()
                .setAccessToken(String.format("access-token-%s", jobAdvertisementId.getValue()))
                .setAvgId(String.format("avg-id-%s", jobAdvertisementId.getValue()))
                .setUserId(String.format("user-id-%s", jobAdvertisementId.getValue()))
                .build();
    }

    public static Company createCompany(JobAdvertisementId jobAdvertisementId) {
        return new Company.Builder()
                .setName(String.format("name-%s", jobAdvertisementId.getValue()))
                .setStreet("street")
                .setPostalCode("postalCode")
                .setCity("city")
                .setCountryIsoCode("ch")
                .setSurrogate(false)
                .build();
    }

    public static JobContent createJobContent(JobAdvertisementId jobAdvertisementId) {
        JobDescription jobDescription = new JobDescription(
                Locale.GERMAN,
                String.format("title-%s", jobAdvertisementId.getValue()),
                String.format("description-%s", jobAdvertisementId.getValue())
        );
        return new JobContent.Builder()
                .setJobDescriptions(Collections.singletonList(jobDescription))
                .setCompany(createCompany(jobAdvertisementId))
                .build();
    }

}
