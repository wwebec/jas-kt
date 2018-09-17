package ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement;

import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisementTestDataProvider.createContact;
import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisementTestDataProvider.createJobContent;
import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisementTestDataProvider.createOwner;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.catchThrowableOfType;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ch.admin.seco.jobs.services.jobadservice.core.conditions.ConditionException;
import ch.admin.seco.jobs.services.jobadservice.core.domain.events.DomainEventMockUtils;
import ch.admin.seco.jobs.services.jobadservice.core.time.TimeMachine;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.events.JobAdvertisementEvent;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.events.JobAdvertisementEvents;
import ch.admin.seco.jobs.services.jobadservice.domain.jobcenter.JobCenter;
import ch.admin.seco.jobs.services.jobadservice.domain.jobcenter.JobCenterAddress;

public class JobAdvertisementTest {

    private DomainEventMockUtils domainEventMockUtils;

    @Before
    public void setUp() {
        domainEventMockUtils = new DomainEventMockUtils();
    }

    @After
    public void tearDown() {
        domainEventMockUtils.clearEvents();
    }

    // @Test
    public void testUpdate() {
        //Prepare
        JobAdvertisement jobAdvertisement = new JobAdvertisement.Builder()
                .setId(JobAdvertisementTestDataProvider.JOB_ADVERTISEMENT_ID_01)
                .setOwner(createOwner(JobAdvertisementTestDataProvider.JOB_ADVERTISEMENT_ID_01))
                .setContact(createContact(JobAdvertisementTestDataProvider.JOB_ADVERTISEMENT_ID_01))
                .setJobContent(createJobContent(JobAdvertisementTestDataProvider.JOB_ADVERTISEMENT_ID_01))
                .setPublication(new Publication.Builder().build())
                .setSourceSystem(SourceSystem.JOBROOM)
                .setStatus(JobAdvertisementStatus.CREATED)
                .build();

        //Execute
        jobAdvertisement.update(
                new JobAdvertisementUpdater.Builder(null)
                        .setCompany(
                                new Company.Builder()
                                        .setName("name")
                                        .setStreet("street")
                                        .setHouseNumber("houseNumber")
                                        .setPostalCode("postalCode")
                                        .setCity("city")
                                        .setCountryIsoCode("countryIsoCode")
                                        .setPostOfficeBoxNumber("postOfficeBoxNumber")
                                        .setPostOfficeBoxPostalCode("postOfficeBoxPostalCode")
                                        .setPostOfficeBoxCity("postOfficeBoxCity")
                                        .setPhone("phone")
                                        .setEmail("email")
                                        .setWebsite("website")
                                        .build()
                        )
                        .build()
        );

        //Validate
        assertThat(jobAdvertisement.getStatus()).isEqualTo(JobAdvertisementStatus.CREATED);

        Company company = jobAdvertisement.getJobContent().getCompany();
        assertThat(company).isNotNull();
        assertThat(company.getName()).isEqualTo("name");
        assertThat(company.getStreet()).isEqualTo("street");
        assertThat(company.getHouseNumber()).isEqualTo("houseNumber");
        assertThat(company.getPostalCode()).isEqualTo("postalCode");
        assertThat(company.getCity()).isEqualTo("city");
        assertThat(company.getCountryIsoCode()).isEqualTo("countryIsoCode");
        assertThat(company.getPostOfficeBoxNumber()).isEqualTo("postOfficeBoxNumber");
        assertThat(company.getPostOfficeBoxPostalCode()).isEqualTo("postOfficeBoxPostalCode");
        assertThat(company.getPostOfficeBoxCity()).isEqualTo("postOfficeBoxCity");
        assertThat(company.getPhone()).isEqualTo("phone");
        assertThat(company.getEmail()).isEqualTo("email");
        assertThat(company.getWebsite()).isEqualTo("website");

        JobAdvertisementEvent jobAdvertisementEvent = domainEventMockUtils.assertSingleDomainEventPublished(JobAdvertisementEvents.JOB_ADVERTISEMENT_UPDATED.getDomainEventType());
        assertThat(jobAdvertisementEvent.getAggregateId()).isEqualTo(JobAdvertisementTestDataProvider.JOB_ADVERTISEMENT_ID_01);
    }

    @Test
    public void testInspect() {
        //Prepare
        JobAdvertisement jobAdvertisement = new JobAdvertisement.Builder()
                .setId(JobAdvertisementTestDataProvider.JOB_ADVERTISEMENT_ID_01)
                .setOwner(createOwner(JobAdvertisementTestDataProvider.JOB_ADVERTISEMENT_ID_01))
                .setContact(createContact(JobAdvertisementTestDataProvider.JOB_ADVERTISEMENT_ID_01))
                .setJobContent(createJobContent(JobAdvertisementTestDataProvider.JOB_ADVERTISEMENT_ID_01))
                .setPublication(new Publication.Builder().build())
                .setSourceSystem(SourceSystem.JOBROOM)
                .setStatus(JobAdvertisementStatus.CREATED)
                .setStellennummerEgov("stellennummerEgov")
                .build();

        //Execute
        jobAdvertisement.inspect();

        //Validate
        assertThat(jobAdvertisement.getStatus()).isEqualTo(JobAdvertisementStatus.INSPECTING);

        JobAdvertisementEvent jobAdvertisementEvent = domainEventMockUtils.assertSingleDomainEventPublished(JobAdvertisementEvents.JOB_ADVERTISEMENT_INSPECTING.getDomainEventType());
        assertThat(jobAdvertisementEvent.getAggregateId()).isEqualTo(JobAdvertisementTestDataProvider.JOB_ADVERTISEMENT_ID_01);
    }

    //@Test
    public void testApprove() {
    }

    //@Test
    public void testReject() {
    }

    //@Test
    public void testCancel() {
    }

    //@Test
    public void testRefining() {
    }

    //@Test
    public void testPublishRestricted() {
    }

    //@Test
    public void testPublishPublic() {
    }

    //@Test
    public void testArchive() {
    }

    @Test
    public void testShortTermValidation() {
        //Prepare
        final JobContent jobContent = createJobContent(JobAdvertisementTestDataProvider.JOB_ADVERTISEMENT_ID_01);
        jobContent.setEmployment(new Employment.Builder()
                .setStartDate(TimeMachine.now().toLocalDate())
                .setEndDate(TimeMachine.now().plusDays(31).toLocalDate())
                .setShortEmployment(true)
                .setImmediately(false)
                .setPermanent(true)
                .setWorkloadPercentageMin(80)
                .setWorkloadPercentageMax(100)
                .build());
        final JobAdvertisement.Builder jobAdvertisementBuilder = new JobAdvertisement.Builder()
                .setId(JobAdvertisementTestDataProvider.JOB_ADVERTISEMENT_ID_01)
                .setOwner(createOwner(JobAdvertisementTestDataProvider.JOB_ADVERTISEMENT_ID_01))
                .setContact(createContact(JobAdvertisementTestDataProvider.JOB_ADVERTISEMENT_ID_01))
                .setJobContent(jobContent)
                .setPublication(new Publication.Builder().build())
                .setSourceSystem(SourceSystem.JOBROOM)
                .setStatus(JobAdvertisementStatus.CREATED);

        //Execute
        final ConditionException exception = catchThrowableOfType(jobAdvertisementBuilder::build, ConditionException.class);

        //Validate
        assertThat(exception).isNotNull();
        assertThat(exception.getMessage()).contains("Employment is short-term and permanent at the same time");
    }

    @Test
    public void testUpdateJobCenter() {
        final JobAdvertisement jobAdvertisement = new JobAdvertisement.Builder()
                .setId(JobAdvertisementTestDataProvider.JOB_ADVERTISEMENT_ID_01)
                .setJobCenterCode("jobCenterCode")
                .setOwner(createOwner(JobAdvertisementTestDataProvider.JOB_ADVERTISEMENT_ID_01))
                .setContact(createContact(JobAdvertisementTestDataProvider.JOB_ADVERTISEMENT_ID_01))
                .setJobContent(createJobContent(JobAdvertisementTestDataProvider.JOB_ADVERTISEMENT_ID_01))
                .setPublication(new Publication.Builder().setCompanyAnonymous(true).build())
                .setSourceSystem(SourceSystem.JOBROOM)
                .setStatus(JobAdvertisementStatus.CREATED)
                .build();
        final JobCenter jobCenter = new JobCenter(
                "jobCenterId",
                "jobCenterCode",
                "jobCenterEmail",
                "jobCenterPhone",
                "jobCenterFax",
                true,
                new JobCenterAddress(
                        "jobCenterName",
                        "jobCenterCity",
                        "jobCenterStreet",
                        "jobCenterHouseNumber",
                        "jobCenterZipCode"
                )
        );

        jobAdvertisement.updateJobCenter(jobCenter);

        assertThat(jobAdvertisement.getJobContent().getDisplayCompany().getName()).isEqualTo("jobCenterName");
    }

    @Test
    public void testShouldNotUpdateJobCenter() {
        JobContent jobContent = createJobContent(JobAdvertisementTestDataProvider.JOB_ADVERTISEMENT_ID_01);
        jobContent.setDisplayCompany(new Company.Builder(new JobCenter(
                "jobCenterOtherId",
                "jobCenterOtherCode",
                "jobCenterOtherEmail",
                "jobCenterOtherPhone",
                "jobCenterOtherFax",
                true,
                new JobCenterAddress(
                        "jobCenterOtherName",
                        "jobCenterOtherCity",
                        "jobCenterOtherStreet",
                        "jobCenterOtherHouseNumber",
                        "jobCenterOtherZipCode"
                )
        )).build());
        final JobAdvertisement jobAdvertisement = new JobAdvertisement.Builder()
                .setId(JobAdvertisementTestDataProvider.JOB_ADVERTISEMENT_ID_01)
                .setJobCenterCode("jobCenterCodeOther")
                .setOwner(createOwner(JobAdvertisementTestDataProvider.JOB_ADVERTISEMENT_ID_01))
                .setContact(createContact(JobAdvertisementTestDataProvider.JOB_ADVERTISEMENT_ID_01))
                .setJobContent(jobContent)
                .setPublication(new Publication.Builder().setCompanyAnonymous(true).build())
                .setSourceSystem(SourceSystem.JOBROOM)
                .setStatus(JobAdvertisementStatus.CREATED)
                .build();
        final JobCenter jobCenter = new JobCenter(
                "jobCenterId",
                "jobCenterCode",
                "jobCenterEmail",
                "jobCenterPhone",
                "jobCenterFax",
                true,
                new JobCenterAddress(
                        "jobCenterName",
                        "jobCenterCity",
                        "jobCenterStreet",
                        "jobCenterHouseNumber",
                        "jobCenterZipCode"
                )
        );

        assertThatThrownBy(() -> jobAdvertisement.updateJobCenter(jobCenter))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("can not be different");
    }
}
