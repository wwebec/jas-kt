package ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement;

import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.fixture.CompanyFixture.testCompany;
import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.fixture.EmploymentFixture.testEmployment;
import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.fixture.JobAdvertisementFixture.testJobAdvertisement;
import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.fixture.JobAdvertisementIdFixture.job01;
import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.fixture.PublicationFixture.testPublication;
import static ch.admin.seco.jobs.services.jobadservice.domain.jobcenter.fixture.JobCenterTestFixture.testJobCenter;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.catchThrowableOfType;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ch.admin.seco.jobs.services.jobadservice.core.conditions.ConditionException;
import ch.admin.seco.jobs.services.jobadservice.core.domain.events.DomainEventMockUtils;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.events.JobAdvertisementEvent;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.events.JobAdvertisementEvents;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.fixture.JobContentFixture;
import ch.admin.seco.jobs.services.jobadservice.domain.jobcenter.JobCenter;

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

    //@Test
    public void testUpdate() {
        //given
        JobAdvertisement jobAdvertisement = testJobAdvertisement().build();

        //when
        jobAdvertisement.update(new JobAdvertisementUpdater.Builder(null)
                        .setCompany(testCompany()
                                .build()
                        )
                        .build()
        );

        //then
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
        assertThat(jobAdvertisementEvent.getAggregateId()).isEqualTo(job01.id());
    }

    @Test
    public void testInspect() {
        //given
        JobAdvertisement jobAdvertisement = testJobAdvertisement().build();

        //when
        jobAdvertisement.inspect();

        //then
        assertThat(jobAdvertisement.getStatus()).isEqualTo(JobAdvertisementStatus.INSPECTING);

        JobAdvertisementEvent jobAdvertisementEvent = domainEventMockUtils.assertSingleDomainEventPublished(JobAdvertisementEvents.JOB_ADVERTISEMENT_INSPECTING.getDomainEventType());
        assertThat(jobAdvertisementEvent.getAggregateId()).isEqualTo(job01.id());
    }

    @Test
    public void testShortTermValidation() {
        //given
        JobAdvertisement.Builder jobAdBuilder = testJobAdvertisement()
                .setJobContent(
                        JobContentFixture.of(job01.id())
                                .setEmployment(testEmployment()
                                        .setShortEmployment(true)
                                        .setPermanent(true)
                                        .build()
                                ).build())
                .setPublication(
                        testPublication()
                                .setPublicDisplay(true)
                                .build());

        //when
        ConditionException exception = catchThrowableOfType(jobAdBuilder::build, ConditionException.class);

        //then
        assertThat(exception).isNotNull();
        assertThat(exception.getMessage()).contains("Employment is short-term and permanent at the same time");
    }

    @Test
    public void testUpdateJobCenter() {
        JobAdvertisement jobAdvertisement = testJobAdvertisement()
                        .setJobCenterCode("jobCenterCode")
                        .build();
        JobCenter jobCenter = testJobCenter();
        jobAdvertisement.updateJobCenter(jobCenter);

        assertThat(jobAdvertisement.getJobContent().getDisplayCompany().getName()).isEqualTo("name");
    }

    @Test
    public void testShouldNotUpdateJobCenter() {
        JobAdvertisementId id = job01.id();
        JobContent jobContent = JobContentFixture.of(id).build();
        jobContent.setDisplayCompany(testCompany().build());
        JobAdvertisement jobAdvertisement = testJobAdvertisement()
                .setJobCenterCode("jobCenterCodeOther")
                .build();
        JobCenter jobCenter = testJobCenter();

        assertThatThrownBy(() -> jobAdvertisement.updateJobCenter(jobCenter))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("can not be different");
    }
}
