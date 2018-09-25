package ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.fixture;

import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisementStatus.PUBLISHED_PUBLIC;
import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisementStatus.PUBLISHED_RESTRICTED;
import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.SourceSystem.EXTERN;
import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.SourceSystem.JOBROOM;
import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.fixture.CompanyFixture.testCompany;
import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.fixture.CompanyFixture.testCompanyEmpty;
import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.fixture.EmploymentFixture.testEmployment;
import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.fixture.JobContentFixture.testJobContent;
import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.fixture.JobDescriptionFixture.testJobDescription;
import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.fixture.LanguageSkillFixture.testLanguageSkill;
import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.fixture.PublicationFixture.testPublication;
import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.fixture.PublicationFixture.testPublicationEmpty;
import static java.time.LocalDate.now;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;

import java.time.LocalDate;

import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisement;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisementId;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisementStatus;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobContent;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.LanguageSkill;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.Location;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.Occupation;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.Owner;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.Publication;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.SourceSystem;

public class JobAdvertisementTestFixture {

    private static final String STELLENNUMMER_AVAM = "avam";

    public static JobAdvertisement testJobAdvertisementJobWithStatusAndReportingObligationEndDate(JobAdvertisementId jobAdvertisementId, JobAdvertisementStatus status, LocalDate reportingObligationEndDate) {
        return new JobAdvertisement.Builder()
                .setId(jobAdvertisementId)
                .setOwner(OwnerFixture.of(jobAdvertisementId).build())
                .setSourceSystem(JOBROOM)
                .setPublication(testPublication().setPublicDisplay(true).build())
                .setJobContent(JobContentFixture.of(jobAdvertisementId).build())
                .setStatus(status)
                .setStellennummerEgov(jobAdvertisementId.getValue())
                .setContact(ContactFixture.of(jobAdvertisementId).build())
                .setReportingObligationEndDate(reportingObligationEndDate)
                .build();
    }

    public static JobAdvertisement testJobAdvertisementWithStatusAndPublicationEndDate(JobAdvertisementId id, JobAdvertisementStatus status, LocalDate publicationEndDate) {
        return testJobAdvertisementWithStatusAndPublication(id, status, testPublicationEmpty()
                .setEndDate(publicationEndDate)
                .build());
    }

    public static JobAdvertisement testJobAdvertisementWithStatusAndPublicationStartDate(JobAdvertisementId id, JobAdvertisementStatus status, LocalDate publicationStartDate) {
        return testJobAdvertisementWithStatusAndPublication(id, status, testPublication()
                .setPublicDisplay(true)
                .setStartDate(publicationStartDate)
                .setEndDate(publicationStartDate.plusMonths(1))
                .build());
    }

    public static JobAdvertisement testJobAdvertisementWithAnonymousCompany(JobAdvertisementId id, JobAdvertisementStatus status, String jobCenterCode) {
        JobContent jobContent = testJobContent().setDisplayCompany(
                testCompanyEmpty()
                        .setName("Test-Company")
                        .setCity("Test-Cizy")
                        .setPostalCode("1234")
                        .build()
        )
                .build();
        return new JobAdvertisement.Builder()
                .setId(id)
                .setOwner(OwnerFixture.of(id).build())
                .setContact(ContactFixture.of(id).build())
                .setJobContent(jobContent)
                .setPublication(
                        testPublicationEmpty()
                                .setCompanyAnonymous(true)
                                .build())
                .setSourceSystem(JOBROOM)
                .setStellennummerEgov(id.getValue())
                .setStatus(status)
                .setJobCenterCode(jobCenterCode)
                .setStellennummerAvam(STELLENNUMMER_AVAM)
                .build();
    }

    private static JobAdvertisement testJobAdvertisementWithStatusAndPublication(JobAdvertisementId jobAdvertisementId, JobAdvertisementStatus status, Publication publication) {
        return new JobAdvertisement.Builder()
                .setId(jobAdvertisementId)
                .setOwner(OwnerFixture.of(jobAdvertisementId).build())
                .setContact(ContactFixture.of(jobAdvertisementId).build())
                .setJobContent(JobContentFixture.of(jobAdvertisementId).build())
                .setPublication(publication)
                .setSourceSystem(JOBROOM)
                .setStellennummerEgov(jobAdvertisementId.getValue())
                .setStatus(status)
                .setStellennummerAvam(STELLENNUMMER_AVAM)
                .build();
    }

    public static JobAdvertisement testJobAdvertisementWithExternalSourceSystemAndStatus(JobAdvertisementId jobAdvertisementId, String fingerprint, JobAdvertisementStatus status) {
        return new JobAdvertisement.Builder()
                .setId(jobAdvertisementId)
                .setFingerprint(fingerprint)
                .setOwner(OwnerFixture.of(jobAdvertisementId).build())
                .setContact(ContactFixture.of(jobAdvertisementId).build())
                .setJobContent(JobContentFixture.of(jobAdvertisementId).build())
                .setPublication(testPublication()
                        .setPublicDisplay(true)
                        .setEndDate(now())
                        .build())
                .setSourceSystem(EXTERN)
                .setStellennummerEgov(jobAdvertisementId.getValue())
                .setStellennummerAvam(null)
                .setStatus(status)
                .build();
    }

    public static JobAdvertisement createJob(JobAdvertisementId jobAdvertisementId) {
        return testJobAdvertisementWithContent(jobAdvertisementId, JobContentFixture.of(jobAdvertisementId).build());

    }

    public static JobAdvertisement createJobWithOwnerAndPublicationStartDate(JobAdvertisementId jobAdvertisementId, String companyId, LocalDate startDate) {
        return new JobAdvertisement.Builder()
                .setId(jobAdvertisementId)
                .setSourceSystem(JOBROOM)
                .setStatus(PUBLISHED_PUBLIC)
                .setOwner(OwnerFixture.of(jobAdvertisementId)
                        .setCompanyId(companyId)
                        .build())
                .setPublication(testPublication()
                        .setStartDate(startDate)
                        .setEndDate(startDate.plusDays(5))
                        .build())
                .setJobContent(JobContentFixture.of(jobAdvertisementId).build())
                .build();
    }


    public static JobAdvertisement createJobWithoutPublicDisplayAndWithoutRestrictedDisplay(JobAdvertisementId jobAdvertisementId) {
        return new JobAdvertisement.Builder()
                .setId(jobAdvertisementId)
                .setSourceSystem(JOBROOM)
                .setStatus(PUBLISHED_PUBLIC)
                .setOwner(OwnerFixture.of(jobAdvertisementId).build())
                .setPublication(testPublication().build())
                .setJobContent(JobContentFixture.of(jobAdvertisementId).build())
                .setReportingObligation(true)
                .build();

    }

    public static JobAdvertisement createJobWithPublicDisplayAndWithRestrictedDisplay(JobAdvertisementId jobAdvertisementId) {
        return new JobAdvertisement.Builder()
                .setId(jobAdvertisementId)
                .setSourceSystem(JOBROOM)
                .setStatus(PUBLISHED_PUBLIC)
                .setOwner(OwnerFixture.of(jobAdvertisementId).build())
                .setPublication(testPublication()
                        .setPublicDisplay(true)
                        .setRestrictedDisplay(true)
                        .build())
                .setJobContent(JobContentFixture.of(jobAdvertisementId).build())
                .setReportingObligation(true)
                .build();

    }

    public static JobAdvertisement createJobWithoutPublicDisplayAndWithRestrictedDisplay(JobAdvertisementId jobAdvertisementId) {
        return new JobAdvertisement.Builder()
                .setId(jobAdvertisementId)
                .setSourceSystem(JOBROOM)
                .setStatus(PUBLISHED_PUBLIC)
                .setOwner(OwnerFixture.of(jobAdvertisementId).build())
                .setPublication(testPublication()
                        .setRestrictedDisplay(true)
                        .build())
                .setJobContent(JobContentFixture.of(jobAdvertisementId).build())
                .setReportingObligation(true)
                .build();

    }

    public static JobAdvertisement createJobWithPublicDisplayAndWithoutRestrictedDisplay(JobAdvertisementId jobAdvertisementId) {
        return new JobAdvertisement.Builder()
                .setId(jobAdvertisementId)
                .setSourceSystem(JOBROOM)
                .setStatus(PUBLISHED_PUBLIC)
                .setOwner(OwnerFixture.of(jobAdvertisementId).build())
                .setPublication(testPublication()
                        .setPublicDisplay(true)
                        .build())
                .setJobContent(JobContentFixture.of(jobAdvertisementId).build())
                .setReportingObligation(true)
                .build();

    }

    public static JobAdvertisement createRestrictedJob(JobAdvertisementId jobAdvertisementId) {
        return new JobAdvertisement.Builder()
                .setId(jobAdvertisementId)
                .setSourceSystem(JOBROOM)
                .setStatus(PUBLISHED_RESTRICTED)
                .setOwner(OwnerFixture.of(jobAdvertisementId).build())
                .setPublication(testPublication().setPublicDisplay(true).build())
                .setJobContent(JobContentFixture.of(jobAdvertisementId).build())
                .setReportingObligation(true)
                .build();

    }

    public static JobAdvertisement createJobWithContractType(JobAdvertisementId jobAdvertisementId, boolean isPermanent) {
        return testJobAdvertisementWithContent(jobAdvertisementId, JobContentFixture.of(jobAdvertisementId)
                .setEmployment(
                        testEmployment()
                        .setPermanent(isPermanent)
                        .build())
        .build());

    }

    public static JobAdvertisement createJobWithCompanyName(JobAdvertisementId jobAdvertisementId, String companyName) {
        return testJobAdvertisementWithContent(jobAdvertisementId, JobContentFixture.of(jobAdvertisementId)
                .setDisplayCompany(testCompany()
                        .setName(companyName)
                        .build())
                .setCompany(testCompany()
                        .setName(companyName)
                        .build())
        .build());

    }

    public static JobAdvertisement createJobWithDescription(JobAdvertisementId jobAdvertisementId, String title, String description) {
        return createJobWithDescription(jobAdvertisementId, title, description, JOBROOM, OwnerFixture.of(jobAdvertisementId).build());
    }

    public static JobAdvertisement createJobWithDescriptionAndOwnerCompanyId(JobAdvertisementId jobAdvertisementId, String title, String description, String companyId) {
        return createJobWithDescription(jobAdvertisementId, title, description, JOBROOM,
                OwnerFixture.of(jobAdvertisementId)
                        .setCompanyId(companyId)
                        .build()
        );
    }

    public static JobAdvertisement createJobWithLanguageSkills(JobAdvertisementId jobAdvertisementId, String title, String description, SourceSystem sourceSystem, LanguageSkill... languageSkills) {
        return createJobWithDescription(jobAdvertisementId, title, description, sourceSystem, OwnerFixture.of(jobAdvertisementId).build(), languageSkills);
    }

    public static JobAdvertisement createJobWithDescription(JobAdvertisementId jobAdvertisementId, String title, String description, SourceSystem sourceSystem, Owner owner, LanguageSkill... languageSkills) {
        return new JobAdvertisement.Builder()
                .setId(jobAdvertisementId)
                .setSourceSystem(sourceSystem)
                .setStatus(PUBLISHED_PUBLIC)
                .setOwner(owner)
                .setPublication(testPublication().setPublicDisplay(true).build())
                .setJobContent(
                        JobContentFixture.of(jobAdvertisementId)
                                .setJobDescriptions(singletonList(testJobDescription()
                                        .setDescription(description)
                                        .setTitle(title)
                                        .build()))
                                .setLanguageSkills(
                                        languageSkills.length == 0
                                                ? singletonList(testLanguageSkill().build())
                                                : asList(languageSkills))
                                .build())
                .build();
    }

    public static JobAdvertisement createJobWithWorkload(JobAdvertisementId jobAdvertisementId, int workloadPercentageMin, int workloadPercentageMax) {
        return testJobAdvertisementWithContent(jobAdvertisementId,
                JobContentFixture.of(jobAdvertisementId)
                        .setEmployment(
                                testEmployment()
                                        .setWorkloadPercentageMin(workloadPercentageMin)
                                        .setWorkloadPercentageMax(workloadPercentageMax)
                                        .build())
                        .build());
    }

    public static JobAdvertisement createJobWithX28Code(JobAdvertisementId jobAdvertisementId, String x28Codes) {
        return testJobAdvertisementWithContent(jobAdvertisementId,
                JobContentFixture.of(jobAdvertisementId)
                        .setX28OccupationCodes(x28Codes)
                        .build());
    }

    public static JobAdvertisement createJobWithOccupation(JobAdvertisementId id, Occupation occupation) {
        return testJobAdvertisementWithContent(id,
                JobContentFixture.of(id)
                        .setOccupations(singletonList(occupation))
                        .build()
        );
    }

    public static JobAdvertisement createJobWithLocation(JobAdvertisementId jobAdvertisementId, Location location) {
        return testJobAdvertisementWithContent(jobAdvertisementId,
                JobContentFixture.of(jobAdvertisementId)
                        .setLocation(location)
                        .build()
        );
    }

    public static JobAdvertisement testJobAdvertisementWithContent(JobAdvertisementId jobAdvertisementId, JobContent jobContent) {
        return new JobAdvertisement.Builder()
                .setId(jobAdvertisementId)
                .setSourceSystem(JOBROOM)
                .setStatus(PUBLISHED_PUBLIC)
                .setOwner(OwnerFixture.of(jobAdvertisementId).build())
                .setPublication(testPublication().setPublicDisplay(true).build())
                .setJobContent(jobContent)
                .build();
    }
}
