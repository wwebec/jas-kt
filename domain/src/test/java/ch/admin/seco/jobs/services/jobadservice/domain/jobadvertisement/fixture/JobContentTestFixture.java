package ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.fixture;


import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.fixture.ApplyChannelFixture.testApplyChannel;
import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.fixture.EmploymentFixture.testEmployment;
import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.fixture.JobDescriptionFixture.testJobDescription;
import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.fixture.LanguageSkillFixture.testLanguageSkill;
import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.fixture.LocationFixture.testLocation;
import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.fixture.OccupationFixture.testOccupation;
import static java.util.Collections.singletonList;

import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.Employment;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisementId;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobContent;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobDescription;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.Location;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.Occupation;

public class JobContentTestFixture {

    public static JobContent testJobContent(JobAdvertisementId jobAdvertisementId,
            String title, String description,
            Occupation occupation, Location location, Employment employment) {
        return new JobContent.Builder()
                .setJobDescriptions(singletonList(testJobDescription()
                        .setTitle(title)
                        .setDescription(description)
                        .build()))
                .setDisplayCompany(CompanyFixture.of(jobAdvertisementId).build())
                .setCompany(CompanyFixture.of(jobAdvertisementId).build())
                .setLanguageSkills(singletonList(testLanguageSkill().build()))
                .setEmployment(employment)
                .setPublicContact(PublicContactFixture.of(jobAdvertisementId).build())
                .setApplyChannel(testApplyChannel().build())
                .setLocation(location)
                .setOccupations(singletonList(occupation))
                .build();
    }

    public static JobContent testJobContent(JobAdvertisementId jobAdvertisementId) {
        return prepareJobContentBuilder(jobAdvertisementId).build();
    }

    public static JobContent.Builder prepareJobContentBuilder(JobAdvertisementId jobAdvertisementId) {
        return prepareJobContentBuilder(jobAdvertisementId, JobDescriptionFixture.of(jobAdvertisementId).build(), testOccupation().build(), testLocation().build());
    }

    public static JobContent.Builder prepareJobContentBuilder(JobAdvertisementId jobAdvertisementId, JobDescription jobDescription, Occupation occupation, Location location) {
        return new JobContent.Builder()
                .setJobDescriptions(singletonList(jobDescription))
                .setDisplayCompany(CompanyFixture.of(jobAdvertisementId).build())
                .setCompany(CompanyFixture.of(jobAdvertisementId).build())
                .setLanguageSkills(singletonList(testLanguageSkill().build()))
                .setEmployment(testEmployment()
                        .setShortEmployment(true)
                        .build())
                .setPublicContact(PublicContactFixture.of(jobAdvertisementId).build())
                .setApplyChannel(testApplyChannel().build())
                .setLocation(location)
                .setOccupations(singletonList(occupation));
    }
}
