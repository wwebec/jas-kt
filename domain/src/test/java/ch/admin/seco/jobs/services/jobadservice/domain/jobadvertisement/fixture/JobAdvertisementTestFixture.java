package ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.fixture;

import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisementStatus.CREATED;
import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisementStatus.INSPECTING;
import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.SourceSystem.EXTERN;
import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.SourceSystem.JOBROOM;
import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.fixture.ApplyChannelFixture.testApplyChannel;
import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.fixture.CompanyFixture.testCompany;
import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.fixture.CompanyFixture.testCompanyEmpty;
import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.fixture.EmploymentFixture.testEmployment;
import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.fixture.JobAdvertisementIdFixture.job01;
import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.fixture.JobContentTestFixture.prepareJobContentBuilder;
import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.fixture.JobContentTestFixture.testJobContent;
import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.fixture.JobDescriptionFixture.testJobDescription;
import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.fixture.JobDescriptionFixture.testJobDescriptionEmpty;
import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.fixture.LanguageSkillFixture.testLanguageSkill;
import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.fixture.LocationFixture.testLocation;
import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.fixture.OccupationFixture.testOccupation;
import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.fixture.PublicContactFixture.of;
import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.fixture.PublicationFixture.testPublication;
import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.fixture.PublicationFixture.testPublicationEmpty;
import static java.time.LocalDate.now;
import static java.util.Collections.singletonList;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.Employment;
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

    public static JobAdvertisement.Builder prepareJobAdvertisementBuilder(JobContent jobContent, Publication publication) {
        return new JobAdvertisement.Builder()
                .setId(job01.id())
                .setSourceSystem(JOBROOM)
                .setStatus(CREATED)
                .setJobContent(jobContent)
                .setOwner(OwnerFixture.of(job01.id()).build())
                .setContact(ContactFixture.of(job01.id()).build())
                .setPublication(publication);
    }

    public static JobAdvertisement.Builder prepareJobAdvertisementBuilder(JobContent jobContent) {
        return prepareJobAdvertisementBuilder(jobContent, testPublication()
                .setPublicDisplay(true)
                .build());
    }

    public static JobAdvertisement testJobAdvertisementWithId01() {
        return prepareJobAdvertisementBuilder(testJobContent(job01.id()), testPublication()
                .setPublicDisplay(true)
                .build()).build();
    }

    public static JobAdvertisement testJobAdvertisement() {
        return prepareJobAdvertisementBuilder(testJobContent(job01.id()))
                .setStellennummerEgov("stellennummerEgov")
                .build();
    }

    public static JobAdvertisement testJobAdvertisementJobWithStatusAndReportingObligationEndDate(JobAdvertisementId jobAdvertisementId, JobAdvertisementStatus status, LocalDate reportingObligationEndDate) {
        return new JobAdvertisement.Builder()
                .setId(jobAdvertisementId)
                .setOwner(OwnerFixture.of(jobAdvertisementId).build())
                .setSourceSystem(JOBROOM)
                .setPublication(testPublication().setPublicDisplay(true).build())
                .setJobContent(testJobContent(jobAdvertisementId))
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

    public static JobAdvertisement testJobAdvertisementWithCreatedStatus() {
        return testJobAdvertisement(CREATED);
    }

    public static JobAdvertisement testJobAdvertisementWithInspectingStatus() {
        return testJobAdvertisement(INSPECTING);
    }

    public static JobAdvertisement testJobAdvertisementWithAnonymousCompany(JobAdvertisementId id, JobAdvertisementStatus status, String jobCenterCode) {
        Publication publication = testPublicationEmpty()
                .setCompanyAnonymous(true)
                .build();
        JobContent jobContent = prepareJobContentBuilder(id)
                .setJobDescriptions(singletonList(testJobDescription().build()))
                .setLanguageSkills(singletonList(testLanguageSkill().build()))
                .setDisplayCompany(testCompanyEmpty()
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
                .setPublication(publication)
                .setSourceSystem(JOBROOM)
                .setStellennummerEgov(id.getValue())
                .setStatus(status)

                .setJobCenterCode(jobCenterCode)
                .setStellennummerAvam(STELLENNUMMER_AVAM)
                .build();
    }

    public static JobAdvertisement testJobAdvertisement(JobAdvertisementStatus status) {
        return new JobAdvertisement.Builder()
                .setId(job01.id())
                .setOwner(OwnerFixture.of(job01.id()).build())
                .setSourceSystem(JOBROOM)
                .setContact(ContactFixture.of(job01.id()).build())
                .setJobContent(testJobContent(job01.id()))
                .setPublication(testPublication().setPublicDisplay(true).build())
                .setStellennummerEgov(job01.id().getValue())
                .setStellennummerAvam(STELLENNUMMER_AVAM)
                .setStatus(status)
                .build();
    }

    public static JobAdvertisement testJobAdvertisement(SourceSystem sourceSystem, boolean reportToAvam) {
        final JobAdvertisementId id = new JobAdvertisementId("id");
        return new JobAdvertisement.Builder()
                .setId(id)
                .setOwner(OwnerFixture.of(id).build())
                .setSourceSystem(sourceSystem)
                .setPublication(testPublication().setPublicDisplay(true).build())
                .setJobContent(testJobContent(id))
                .setStatus(CREATED)
                .setReportToAvam(reportToAvam)
                .setReportingObligation(reportToAvam)
                .build();
    }

    public static JobAdvertisement testJobAdvertisement(JobAdvertisementId id, JobContent jobContent, String jobCenterCodeOther) {
        return new JobAdvertisement.Builder()
                .setId(id)
                .setJobCenterCode(jobCenterCodeOther)
                .setOwner(OwnerFixture.of(id).build())
                .setContact(ContactFixture.of(id).build())
                .setJobContent(jobContent)
                .setPublication(testPublication()
                        .setCompanyAnonymous(true)
                        .build())
                .setSourceSystem(JOBROOM)
                .setStatus(CREATED)
                .build();
    }

    private static JobAdvertisement testJobAdvertisementWithStatusAndPublication(JobAdvertisementId jobAdvertisementId, JobAdvertisementStatus status, Publication publication) {
        return new JobAdvertisement.Builder()
                .setId(jobAdvertisementId)
                .setOwner(OwnerFixture.of(jobAdvertisementId).build())
                .setContact(ContactFixture.of(jobAdvertisementId).build())
                .setJobContent(testJobContent(jobAdvertisementId))
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
                .setJobContent(testJobContent(jobAdvertisementId))
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
        return new JobAdvertisement.Builder()
                .setId(jobAdvertisementId)
                .setSourceSystem(JOBROOM)
                .setStatus(JobAdvertisementStatus.PUBLISHED_PUBLIC)
                .setOwner(OwnerFixture.of(jobAdvertisementId).build())
                .setPublication(testPublication().setPublicDisplay(true).build())
                .setJobContent(testJobContent(jobAdvertisementId))
                .build();

    }

    public static JobAdvertisement createJobWithOwnerAndPublicationStartDate(JobAdvertisementId jobAdvertisementId, String companyId, LocalDate startDate) {
        Publication publication = new Publication.Builder()
                .setStartDate(startDate)
                .setEndDate(startDate.plusDays(5))
                .build();

        return new JobAdvertisement.Builder()
                .setId(jobAdvertisementId)
                .setSourceSystem(JOBROOM)
                .setStatus(JobAdvertisementStatus.PUBLISHED_PUBLIC)
                .setOwner(OwnerFixture.of(jobAdvertisementId)
                        .setCompanyId(companyId)
                        .build())
                .setPublication(publication)
                .setJobContent(new JobContent.Builder()
                        .setJobDescriptions(singletonList(JobDescriptionFixture.of(jobAdvertisementId).build()))
                        .setDisplayCompany(CompanyFixture.of(jobAdvertisementId).build())
                        .setCompany(CompanyFixture.of(jobAdvertisementId).build())
                        .setLanguageSkills(singletonList(testLanguageSkill().build()))
                        .setEmployment(testEmployment().setShortEmployment(true).build())
                        .setPublicContact(of(jobAdvertisementId).build())
                        .setApplyChannel(testApplyChannel().build())
                        .setLocation(testLocation().build())
                        .setOccupations(singletonList(testOccupation().build()))
                        .build())
                .build();
    }


    public static JobAdvertisement createJobWithoutPublicDisplayAndWithoutRestrictedDisplay(JobAdvertisementId jobAdvertisementId) {
        return new JobAdvertisement.Builder()
                .setId(jobAdvertisementId)
                .setSourceSystem(JOBROOM)
                .setStatus(JobAdvertisementStatus.PUBLISHED_PUBLIC)
                .setOwner(OwnerFixture.of(jobAdvertisementId).build())
                .setPublication(testPublication().build())
                .setJobContent(testJobContent(jobAdvertisementId))
                .setReportingObligation(true)
                .build();

    }

    public static JobAdvertisement createJobWithPublicDisplayAndWithRestrictedDisplay(JobAdvertisementId jobAdvertisementId) {
        return new JobAdvertisement.Builder()
                .setId(jobAdvertisementId)
                .setSourceSystem(JOBROOM)
                .setStatus(JobAdvertisementStatus.PUBLISHED_PUBLIC)
                .setOwner(OwnerFixture.of(jobAdvertisementId).build())
                .setPublication(testPublication()
                        .setPublicDisplay(true)
                        .setRestrictedDisplay(true)
                        .build())
                .setJobContent(testJobContent(jobAdvertisementId))
                .setReportingObligation(true)
                .build();

    }

    public static JobAdvertisement createJobWithoutPublicDisplayAndWithRestrictedDisplay(JobAdvertisementId jobAdvertisementId) {
        return new JobAdvertisement.Builder()
                .setId(jobAdvertisementId)
                .setSourceSystem(JOBROOM)
                .setStatus(JobAdvertisementStatus.PUBLISHED_PUBLIC)
                .setOwner(OwnerFixture.of(jobAdvertisementId).build())
                .setPublication(testPublication()
                        .setRestrictedDisplay(true)
                        .build())
                .setJobContent(testJobContent(jobAdvertisementId))
                .setReportingObligation(true)
                .build();

    }

    public static JobAdvertisement createJobWithPublicDisplayAndWithoutRestrictedDisplay(JobAdvertisementId jobAdvertisementId) {
        return new JobAdvertisement.Builder()
                .setId(jobAdvertisementId)
                .setSourceSystem(JOBROOM)
                .setStatus(JobAdvertisementStatus.PUBLISHED_PUBLIC)
                .setOwner(OwnerFixture.of(jobAdvertisementId).build())
                .setPublication(testPublication()
                        .setPublicDisplay(true)
                        .build())
                .setJobContent(testJobContent(jobAdvertisementId))
                .setReportingObligation(true)
                .build();

    }

    public static JobAdvertisement createRestrictedJob(JobAdvertisementId jobAdvertisementId) {
        return new JobAdvertisement.Builder()
                .setId(jobAdvertisementId)
                .setSourceSystem(JOBROOM)
                .setStatus(JobAdvertisementStatus.PUBLISHED_RESTRICTED)
                .setOwner(OwnerFixture.of(jobAdvertisementId).build())
                .setPublication(testPublication().setPublicDisplay(true).build())
                .setJobContent(testJobContent(jobAdvertisementId))
                .setReportingObligation(true)
                .build();

    }

    public static JobAdvertisement createJobWithContractType(JobAdvertisementId jobAdvertisementId, boolean isPermanent) {
        return new JobAdvertisement.Builder()
                .setId(jobAdvertisementId)
                .setSourceSystem(JOBROOM)
                .setStatus(JobAdvertisementStatus.PUBLISHED_PUBLIC)
                .setOwner(OwnerFixture.of(jobAdvertisementId).build())
                .setPublication(testPublication().setPublicDisplay(true).build())
                .setJobContent(new JobContent.Builder()
                        .setJobDescriptions(singletonList(JobDescriptionFixture.of(jobAdvertisementId).build()))
                        .setDisplayCompany(CompanyFixture.of(jobAdvertisementId).build())
                        .setCompany(CompanyFixture.of(jobAdvertisementId).build())
                        .setLanguageSkills(singletonList(testLanguageSkill().build()))
                        .setEmployment(new Employment.Builder()
                                .setStartDate(now())
                                .setShortEmployment(false)
                                .setImmediately(false)
                                .setPermanent(isPermanent)
                                .setWorkloadPercentageMin(80)
                                .setWorkloadPercentageMax(100)
                                .build())
                        .setPublicContact(PublicContactFixture.of(jobAdvertisementId).build())
                        .setApplyChannel(testApplyChannel().build())
                        .setLocation(testLocation().build())
                        .setOccupations(singletonList(testOccupation().build()))
                        .build())
                .build();

    }

    public static JobAdvertisement createJobWithCompanyName(JobAdvertisementId jobAdvertisementId, String companyName) {
        return new JobAdvertisement.Builder()
                .setId(jobAdvertisementId)
                .setSourceSystem(JOBROOM)
                .setStatus(JobAdvertisementStatus.PUBLISHED_PUBLIC)
                .setOwner(OwnerFixture.of(jobAdvertisementId).build())
                .setPublication(testPublication().setPublicDisplay(true).build())
                .setJobContent(new JobContent.Builder()
                        .setJobDescriptions(singletonList(JobDescriptionFixture.of(jobAdvertisementId).build()))
                        .setDisplayCompany(testCompany()
                                .setName(companyName)
                                .build())
                        .setCompany(testCompany()
                                .setName(companyName)
                                .build())
                        .setLanguageSkills(singletonList(testLanguageSkill().build()))
                        .setEmployment(testEmployment().setShortEmployment(true).build())
                        .setPublicContact(PublicContactFixture.of(jobAdvertisementId).build())
                        .setApplyChannel(testApplyChannel().build())
                        .setLocation(testLocation().build())
                        .setOccupations(singletonList(testOccupation().build()))
                        .build())
                .build();

    }

    public static JobAdvertisement createJobWithDescription(JobAdvertisementId jobAdvertisementId, String title, String description) {
        return createJobWithDescription(jobAdvertisementId, title, description, JOBROOM);
    }

    public static JobAdvertisement createJobWithDescriptionAndOwnerCompanyId(JobAdvertisementId jobAdvertisementId, String title, String description, String companyId) {
        return createJobWithDescription(jobAdvertisementId, title, description, JOBROOM,
                OwnerFixture.of(jobAdvertisementId)
                .setCompanyId(companyId)
                .build()
        );
    }


    public static JobAdvertisement createJobWithDescription(JobAdvertisementId jobAdvertisementId, String title, String description, SourceSystem sourceSystem) {
        return createJobWithDescription(jobAdvertisementId, title, description, sourceSystem, OwnerFixture.of(jobAdvertisementId).build());
    }

    public static JobAdvertisement createJobWithLanguageSkills(JobAdvertisementId jobAdvertisementId, String title, String description, SourceSystem sourceSystem, LanguageSkill... languageSkills) {
        return createJobWithDescription(jobAdvertisementId, title, description, sourceSystem, OwnerFixture.of(jobAdvertisementId).build(), languageSkills);
    }

    public static JobAdvertisement createJobWithDescription(JobAdvertisementId jobAdvertisementId, String title, String description, SourceSystem sourceSystem, Owner owner, LanguageSkill... languageSkills) {
        List<LanguageSkill> skills = languageSkills.length == 0 ? singletonList(testLanguageSkill().build()) : Arrays.asList(languageSkills);
        return new JobAdvertisement.Builder()
                .setId(jobAdvertisementId)
                .setSourceSystem(sourceSystem)
                .setStatus(JobAdvertisementStatus.PUBLISHED_PUBLIC)
                .setOwner(owner)
                .setPublication(testPublication().setPublicDisplay(true).build())
                .setJobContent(new JobContent.Builder()
                        .setJobDescriptions(singletonList(testJobDescription()
                                .setDescription(description)
                                .setTitle(title)
                                .build()))
                        .setDisplayCompany(CompanyFixture.of(jobAdvertisementId).build())
                        .setCompany(CompanyFixture.of(jobAdvertisementId).build())
                        .setLanguageSkills(skills)
                        .setEmployment(testEmployment().setShortEmployment(true).build())
                        .setPublicContact(PublicContactFixture.of(jobAdvertisementId).build())
                        .setApplyChannel(testApplyChannel().build())
                        .setLocation(testLocation().build())
                        .setOccupations(singletonList(testOccupation().build()))
                        .build())
                .build();
    }

    public static JobAdvertisement createJobWithWorkload(JobAdvertisementId jobAdvertisementId, int workloadPercentageMin, int workloadPercentageMax) {

        Employment employment = new Employment.Builder()
                .setStartDate(now())
                .setEndDate(now().plusDays(31))
                .setShortEmployment(false)
                .setImmediately(false)
                .setPermanent(false)
                .setWorkloadPercentageMin(workloadPercentageMin)
                .setWorkloadPercentageMax(workloadPercentageMax)
                .build();

        return new JobAdvertisement.Builder()
                .setId(jobAdvertisementId)
                .setSourceSystem(JOBROOM)
                .setStatus(JobAdvertisementStatus.PUBLISHED_PUBLIC)
                .setOwner(OwnerFixture.of(jobAdvertisementId).build())
                .setPublication(testPublication().setPublicDisplay(true).build())
                .setJobContent(new JobContent.Builder()
                        .setJobDescriptions(singletonList(JobDescriptionFixture.of(jobAdvertisementId).build()))
                        .setDisplayCompany(CompanyFixture.of(jobAdvertisementId).build())
                        .setCompany(CompanyFixture.of(jobAdvertisementId).build())
                        .setLanguageSkills(singletonList(testLanguageSkill().build()))
                        .setEmployment(employment)
                        .setPublicContact(PublicContactFixture.of(jobAdvertisementId).build())
                        .setApplyChannel(testApplyChannel().build())
                        .setLocation(testLocation().build())
                        .setOccupations(singletonList(testOccupation().build()))
                        .build())
                .build();
    }

    public static JobAdvertisement createJobWithX28Code(JobAdvertisementId jobAdvertisementId, String x28Codes) {
        return new JobAdvertisement.Builder()
                .setId(jobAdvertisementId)
                .setSourceSystem(JOBROOM)
                .setStatus(JobAdvertisementStatus.PUBLISHED_PUBLIC)
                .setOwner(OwnerFixture.of(jobAdvertisementId).build())
                .setPublication(testPublication().setPublicDisplay(true).build())
                .setJobContent(new JobContent.Builder()
                        .setX28OccupationCodes(x28Codes)
                        .setJobDescriptions(singletonList(JobDescriptionFixture.of(jobAdvertisementId).build()))
                        .setDisplayCompany(CompanyFixture.of(jobAdvertisementId).build())
                        .setCompany(CompanyFixture.of(jobAdvertisementId).build())
                        .setLanguageSkills(singletonList(testLanguageSkill().build()))
                        .setEmployment(testEmployment().setShortEmployment(true).build())
                        .setPublicContact(PublicContactFixture.of(jobAdvertisementId).build())
                        .setApplyChannel(testApplyChannel().build())
                        .setLocation(testLocation().build())
                        .setOccupations(singletonList(testOccupation().build()))
                        .build())
                .build();
    }

    public static JobAdvertisement createJobWithOccupation(JobAdvertisementId jobAdvertisementId, Occupation occupation) {
        return new JobAdvertisement.Builder()
                .setId(jobAdvertisementId)
                .setSourceSystem(JOBROOM)
                .setStatus(JobAdvertisementStatus.PUBLISHED_PUBLIC)
                .setOwner(OwnerFixture.of(jobAdvertisementId).build())
                .setPublication(testPublication().setPublicDisplay(true).build())
                .setJobContent(new JobContent.Builder()
                        .setJobDescriptions(singletonList(JobDescriptionFixture.of(jobAdvertisementId).build()))
                        .setDisplayCompany(CompanyFixture.of(jobAdvertisementId).build())
                        .setCompany(CompanyFixture.of(jobAdvertisementId).build())
                        .setLanguageSkills(singletonList(testLanguageSkill().build()))
                        .setEmployment(testEmployment().setShortEmployment(true).build())
                        .setPublicContact(PublicContactFixture.of(jobAdvertisementId).build())
                        .setApplyChannel(testApplyChannel().build())
                        .setLocation(testLocation().build())
                        .setOccupations(singletonList(occupation))
                        .build())
                .build();
    }

    public static JobAdvertisement createJobWithLocation(JobAdvertisementId jobAdvertisementId, Location location) {
        return new JobAdvertisement.Builder()
                .setId(jobAdvertisementId)
                .setSourceSystem(JOBROOM)
                .setStatus(JobAdvertisementStatus.PUBLISHED_PUBLIC)
                .setOwner(OwnerFixture.of(jobAdvertisementId).build())
                .setPublication(testPublication().setPublicDisplay(true).build())
                .setJobContent(new JobContent.Builder()
                        .setJobDescriptions(singletonList(JobDescriptionFixture.of(jobAdvertisementId).build()))
                        .setDisplayCompany(CompanyFixture.of(jobAdvertisementId).build())
                        .setCompany(CompanyFixture.of(jobAdvertisementId).build())
                        .setLanguageSkills(singletonList(testLanguageSkill().build()))
                        .setEmployment(testEmployment().setShortEmployment(true).build())
                        .setPublicContact(PublicContactFixture.of(jobAdvertisementId).build())
                        .setApplyChannel(testApplyChannel().build())
                        .setLocation(location)
                        .setOccupations(singletonList(testOccupation().build()))
                        .build())
                .build();
    }

}
