package ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import ch.admin.seco.jobs.services.jobadservice.core.domain.TestDataProvider;
import ch.admin.seco.jobs.services.jobadservice.core.time.TimeMachine;

public class JobAdvertisementTestDataProvider implements TestDataProvider<JobAdvertisement> {

    public static final JobAdvertisementId JOB_ADVERTISEMENT_ID_01 = new JobAdvertisementId("job01");
    public static final JobAdvertisementId JOB_ADVERTISEMENT_ID_02 = new JobAdvertisementId("job02");
    public static final JobAdvertisementId JOB_ADVERTISEMENT_ID_03 = new JobAdvertisementId("job03");
    public static final JobAdvertisementId JOB_ADVERTISEMENT_ID_04 = new JobAdvertisementId("job04");
    public static final JobAdvertisementId JOB_ADVERTISEMENT_ID_05 = new JobAdvertisementId("job05");
    public static final JobAdvertisementId JOB_ADVERTISEMENT_ID_06 = new JobAdvertisementId("job06");
    public static final JobAdvertisementId JOB_ADVERTISEMENT_ID_07 = new JobAdvertisementId("job07");
    public static final JobAdvertisementId JOB_ADVERTISEMENT_ID_08 = new JobAdvertisementId("job08");
    public static final JobAdvertisementId JOB_ADVERTISEMENT_ID_09 = new JobAdvertisementId("job09");
    public static final JobAdvertisementId JOB_ADVERTISEMENT_ID_10 = new JobAdvertisementId("job10");

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
        return new JobAdvertisement.Builder()
                .setId(JOB_ADVERTISEMENT_ID_01)
                .setSourceSystem(SourceSystem.JOBROOM)
                .setStatus(JobAdvertisementStatus.CREATED)
                .setJobContent(createJobContent(JOB_ADVERTISEMENT_ID_01))
                .setOwner(createOwner(JOB_ADVERTISEMENT_ID_01))
                .setPublication(createPublication())
                .build();
    }

    public static JobContent createJobContent(JobAdvertisementId jobAdvertisementId) {
        return createJobContent(jobAdvertisementId, createJobDescription(jobAdvertisementId), createOccupation(), createLocation());
    }

    public static JobContent createJobContent(JobAdvertisementId jobAdvertisementId, JobDescription jobDescription, Occupation occupation, Location location) {
        return new JobContent.Builder()
                .setJobDescriptions(Collections.singletonList(jobDescription))
                .setDisplayCompany(createCompany(jobAdvertisementId))
                .setCompany(createCompany(jobAdvertisementId))
                .setLanguageSkills(Collections.singletonList(createLanguageSkill()))
                .setEmployment(createEmployment())
                .setPublicContact(createPublicContact(jobAdvertisementId))
                .setApplyChannel(createApplyChannel())
                .setLocation(location)
                .setOccupations(Collections.singletonList(occupation))
                .build();
    }

    public static JobContent createJobContent(JobAdvertisementId jobAdvertisementId,
                                              String title, String description,
                                              Occupation occupation, Location location, Employment employment) {
        JobDescription jobDescription = createJobDescription(title, description);
        PublicContact publicContact = createPublicContact(jobAdvertisementId);
        return new JobContent.Builder()
                .setJobDescriptions(Collections.singletonList(jobDescription))
                .setDisplayCompany(createCompany(jobAdvertisementId))
                .setCompany(createCompany(jobAdvertisementId))
                .setLanguageSkills(Collections.singletonList(createLanguageSkill()))
                .setEmployment(employment)
                .setPublicContact(publicContact)
                .setApplyChannel(createApplyChannel())
                .setLocation(location)
                .setOccupations(Collections.singletonList(occupation))
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
                .setCompanyId(String.format("avg-id-%s", jobAdvertisementId.getValue()))
                .setUserId(String.format("user-id-%s", jobAdvertisementId.getValue()))
                .build();
    }

    public static Company createCompany(JobAdvertisementId jobAdvertisementId) {
        return createCompany(String.format("name-%s", jobAdvertisementId.getValue()));
    }

    public static Company createCompany(String companyName) {
        return new Company.Builder()
                .setName(companyName)
                .setStreet("street")
                .setPostalCode("postalCode")
                .setCity("city")
                .setCountryIsoCode("ch")
                .setSurrogate(false)
                .build();
    }

    public static Employment createEmployment() {
        return new Employment.Builder()
                .setStartDate(TimeMachine.now().toLocalDate())
                .setEndDate(TimeMachine.now().plusDays(31).toLocalDate())
                .setShortEmployment(false)
                .setImmediately(false)
                .setPermanent(false)
                .setWorkloadPercentageMin(80)
                .setWorkloadPercentageMax(100)
                .build();
    }

    public static Occupation createOccupation() {
        return new Occupation.Builder()
                .setAvamOccupationCode("avamOccupationCode")
                .build();
    }

    public static JobDescription createJobDescription(String title, String description) {
        return new JobDescription.Builder()
                .setLanguage(Locale.GERMAN)
                .setTitle(title)
                .setDescription(description)
                .build();
    }

    public static JobDescription createJobDescription(JobAdvertisementId jobAdvertisementId) {
        String title = String.format("title-%s", jobAdvertisementId.getValue());
        String description = String.format("description-%s", jobAdvertisementId.getValue());
        return new JobDescription.Builder()
                .setLanguage(Locale.GERMAN)
                .setTitle(title)
                .setDescription(description)
                .build();
    }

    public static Location createLocation() {
        return new Location.Builder()
                .setRemarks("remarks")
                .setCity("city")
                .setPostalCode("postalCode")
                .setCommunalCode(null)
                .setRegionCode(null)
                .setCantonCode("BE")
                .setCountryIsoCode("CH")
                .build();
    }

    public static LanguageSkill createLanguageSkill() {
        return new LanguageSkill.Builder()
                .setLanguageIsoCode("de")
                .setSpokenLevel(LanguageLevel.PROFICIENT)
                .setWrittenLevel(LanguageLevel.INTERMEDIATE)
                .build();
    }

    public static PublicContact createPublicContact(JobAdvertisementId jobAdvertisementId) {
        return new PublicContact.Builder()
                .setSalutation(Salutation.MR)
                .setEmail(String.format("mail-%s@mail.com", jobAdvertisementId.getValue()))
                .setPhone(String.format("+41 %s", jobAdvertisementId.getValue()))
                .setFirstName(String.format("first-name-%s", jobAdvertisementId.getValue()))
                .setLastName(String.format("last-name-%s", jobAdvertisementId.getValue()))
                .build();
    }

    public static ApplyChannel createApplyChannel() {
        return new ApplyChannel.Builder()
                .setAdditionalInfo("additionalInfo")
                .setMailAddress("mailAddress")
                .setFormUrl("formUrl")
                .setEmailAddress("emailAddress")
                .setPhoneNumber("phoneNumber")
                .build();
    }

    public static Publication createPublication() {
        return createPublication(true, false);
    }

    public static Publication createPublication(boolean publicDisplay, boolean restrictedDisplay) {
        return new Publication.Builder()
                .setRestrictedDisplay(restrictedDisplay)
                .setPublicDisplay(publicDisplay)
                .setStartDate(TimeMachine.now().toLocalDate())
                .setEndDate(TimeMachine.now().plusDays(5).toLocalDate())
                .build();
    }

}
