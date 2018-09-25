package ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.fixture;

import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.fixture.ApplyChannelFixture.testApplyChannel;
import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.fixture.CompanyFixture.testCompany;
import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.fixture.CompanyFixture.testDisplayCompany;
import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.fixture.EmployerFixture.testEmployer;
import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.fixture.EmploymentFixture.testEmployment;
import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.fixture.JobDescriptionFixture.testJobDescription;
import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.fixture.LanguageSkillFixture.testLanguageSkill;
import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.fixture.LocationFixture.testLocation;
import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.fixture.OccupationFixture.testOccupationEmpty;
import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.fixture.PublicContactFixture.testPublicContact;
import static ch.admin.seco.jobs.services.jobadservice.domain.jobcenter.fixture.JobCenterFixture.testJobCenter;
import static java.util.Collections.singletonList;

import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisementId;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobContent.Builder;

public class JobContentFixture {

    public static Builder of(JobAdvertisementId id) {
        return testJobContent()
                .setDisplayCompany(CompanyFixture.of(id).build())
                .setCompany(CompanyFixture.of(id).build())
                .setLanguageSkills(singletonList(testLanguageSkill().build()))
                .setPublicContact(PublicContactFixture.of(id).build());
    }

    public static Builder testJobContentEmpty() {
        return new Builder();
    }

    public static Builder testJobContent() {
        return testJobContentEmpty()
                .setExternalUrl("externalUr")
                .setX28OccupationCodes("x28OccupationCodes")
                .setNumberOfJobs("numberOfJobs")
                .setJobDescriptions(singletonList(testJobDescription().build()))
                .setDisplayCompany(testDisplayCompany(testJobCenter()).build())
                .setCompany(testCompany().build())
                .setEmployer(testEmployer().build())
                .setEmployment(testEmployment().build())
                .setLocation(testLocation().build())
                .setOccupations(singletonList(
                        testOccupationEmpty()
                                .setAvamOccupationCode("avamOccupationCode")
                                .build()))
                .setLanguageSkills(singletonList(testLanguageSkill().build()))
                .setApplyChannel(testApplyChannel().build())
                .setPublicContact(testPublicContact().build());
    }
}