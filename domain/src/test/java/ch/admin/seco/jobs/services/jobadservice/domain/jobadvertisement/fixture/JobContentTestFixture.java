package ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.fixture;

import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.fixture.CompanyTestFixture.testCompany;
import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.fixture.EmploymentTestFixture.testEmployment;
import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.fixture.JobAdvertisementTestFixtureProvider.testApplyChannel;
import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.fixture.JobAdvertisementTestFixtureProvider.testLanguageSkill;
import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.fixture.JobAdvertisementTestFixtureProvider.testLocation;
import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.fixture.JobAdvertisementTestFixtureProvider.testOccupation;
import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.fixture.JobAdvertisementTestFixtureProvider.createPublicContact;
import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.fixture.JobDescriptionTextFixture.testJobDescription;
import static java.util.Collections.singletonList;

import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.Employment;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisementId;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobContent;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobDescription;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.Location;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.Occupation;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.PublicContact;

public class JobContentTestFixture {

    public static JobContent testJobContent(JobAdvertisementId jobAdvertisementId,
            String title, String description,
            Occupation occupation, Location location, Employment employment) {
        JobDescription jobDescription = testJobDescription(title, description);
        PublicContact publicContact = createPublicContact(jobAdvertisementId);
        return new JobContent.Builder()
                .setJobDescriptions(singletonList(jobDescription))
                .setDisplayCompany(testCompany(jobAdvertisementId))
                .setCompany(testCompany(jobAdvertisementId))
                .setLanguageSkills(singletonList(testLanguageSkill()))
                .setEmployment(employment)
                .setPublicContact(publicContact)
                .setApplyChannel(testApplyChannel())
                .setLocation(location)
                .setOccupations(singletonList(occupation))
                .build();
    }

    public static JobContent testJobContent(JobAdvertisementId jobAdvertisementId) {
        return prepareJobContentBuilder(jobAdvertisementId).build();
    }

    public static JobContent.Builder prepareJobContentBuilder(JobAdvertisementId jobAdvertisementId) {
        return prepareJobContentBuilder(jobAdvertisementId, JobDescriptionTextFixture.testJobDescription(jobAdvertisementId), testOccupation(), testLocation());
    }

    public static JobContent.Builder prepareJobContentBuilder(JobAdvertisementId jobAdvertisementId, JobDescription jobDescription, Occupation occupation, Location location) {
        return new JobContent.Builder()
                .setJobDescriptions(singletonList(jobDescription))
                .setDisplayCompany(testCompany(jobAdvertisementId))
                .setCompany(testCompany(jobAdvertisementId))
                .setLanguageSkills(singletonList(testLanguageSkill()))
                .setEmployment(testEmployment())
                .setPublicContact(createPublicContact(jobAdvertisementId))
                .setApplyChannel(testApplyChannel())
                .setLocation(location)
                .setOccupations(singletonList(occupation));
    }
}
