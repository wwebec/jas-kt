package ch.admin.seco.jobs.services.jobadservice.infrastructure.messagebroker.x28;

import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.fixture.JobAdvertisementIdFixture.job01;
import static java.time.LocalDate.now;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.support.TransactionTemplate;

import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.JobAdvertisementApplicationService;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.EmploymentDto;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.x28.X28CreateJobAdvertisementDto;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.x28.X28CompanyDto;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.x28.X28ContactDto;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.x28.X28LanguageSkillDto;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.x28.X28LocationDto;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.x28.X28OccupationDto;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisement;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisementId;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisementRepository;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.JobAdvertisementStatus;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.LanguageLevel;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.Publication;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.Salutation;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.SourceSystem;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.WorkExperience;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.fixture.ContactFixture;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.fixture.JobContentFixture;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.fixture.OwnerFixture;

@RunWith(SpringRunner.class)
@DataJpaTest
public class X28AdapterTest {

    @Autowired
    private X28MessageLogRepository x28MessageLogRepository;

    @MockBean
    private JobAdvertisementRepository jobAdvertisementRepository;

    @Autowired
    private TransactionTemplate transactionTemplate;

    @MockBean
    private JobAdvertisementApplicationService jobAdvertisementApplicationService;

    private X28Adapter sut; //System Under Test

    @Before
    public void setUp() {
        this.sut = new X28Adapter(jobAdvertisementApplicationService, jobAdvertisementRepository, transactionTemplate, x28MessageLogRepository);
    }

    @Test
    public void shouldCreateFromX28() {
        when(jobAdvertisementRepository.findByStellennummerEgov(any())).thenReturn(Optional.empty());
        when(jobAdvertisementRepository.findByStellennummerAvam(any())).thenReturn(Optional.empty());
        X28CreateJobAdvertisementDto x28Dto = createJobAdvertisementFromX28Dto();

        sut.handleCreateFromX28Action(x28Dto);

        verify(jobAdvertisementApplicationService, times(1)).createFromX28(any());
        verify(jobAdvertisementApplicationService, never()).updateFromX28(any());
    }

    @Test
    public void shouldUpdatefromEgov() {
        when(jobAdvertisementRepository.findByStellennummerEgov(any())).thenReturn(Optional.of(createExternalJobWithStatus(job01.id(), "fingerprint", JobAdvertisementStatus.PUBLISHED_PUBLIC)));
        when(jobAdvertisementRepository.findByStellennummerAvam(any())).thenReturn(Optional.empty());
        X28CreateJobAdvertisementDto x28Dto = createJobAdvertisementFromX28Dto();

        sut.handleCreateFromX28Action(x28Dto);

        verify(jobAdvertisementApplicationService, never()).createFromX28(any());
        verify(jobAdvertisementApplicationService, times(1)).updateFromX28(any());
    }

    @Test
    public void shouldUpdatefromAvam() {
        when(jobAdvertisementRepository.findByStellennummerEgov(any())).thenReturn(Optional.empty());
        when(jobAdvertisementRepository.findByStellennummerAvam(any())).thenReturn(Optional.of(createExternalJobWithStatus(job01.id(), "fingerprint", JobAdvertisementStatus.PUBLISHED_PUBLIC)));
        X28CreateJobAdvertisementDto x28Dto = createJobAdvertisementFromX28Dto();

        sut.handleCreateFromX28Action(x28Dto);

        verify(jobAdvertisementApplicationService, never()).createFromX28(any());
        verify(jobAdvertisementApplicationService, times(1)).updateFromX28(any());
    }

    private JobAdvertisement createExternalJobWithStatus(JobAdvertisementId jobAdvertisementId, String fingerprint, JobAdvertisementStatus status) {
        return new JobAdvertisement.Builder()
                .setId(jobAdvertisementId)
                .setFingerprint(fingerprint)
                .setOwner(OwnerFixture.of(jobAdvertisementId).build())
                .setContact(ContactFixture.of(jobAdvertisementId).build())
                .setJobContent(JobContentFixture.of(jobAdvertisementId).build())
                .setPublication(new Publication.Builder().setEndDate(now()).build())
                .setSourceSystem(SourceSystem.EXTERN)
                .setStellennummerEgov(jobAdvertisementId.getValue())
                .setStellennummerAvam(null)
                .setStatus(status)
                .build();
    }

    private X28CreateJobAdvertisementDto createJobAdvertisementFromX28Dto() {
        return new X28CreateJobAdvertisementDto()
                .setStellennummerEgov("stellennummerEgov")
                .setStellennummerAvam("stellennummerAvam")
                .setTitle("title")
                .setDescription("description")
                .setNumberOfJobs("numberOfJobs")
                .setFingerprint("fingerprint")
                .setExternalUrl("externalUrl")
                .setJobCenterCode("jobCenterCode")
                .setContact(new X28ContactDto(Salutation.MR, "firstName", "lastName", "phone", "email", "de"))
                .setEmployment(
                        new EmploymentDto()
                                .setStartDate(LocalDate.of(2018, 1, 1))
                                .setEndDate(LocalDate.of(2018, 12, 31))
                                .setShortEmployment(false)
                                .setImmediately(false)
                                .setPermanent(false)
                                .setWorkloadPercentageMin(100)
                                .setWorkloadPercentageMax(100)
                                .setWorkForms(null))
                .setCompany(new X28CompanyDto("companyName", "companyStreet", "companyHouseNumber", "companyPostalCode", "companyCity", "CH", null, null, null, "companyPhone", "companyEmail", "companyWebside", false))
                .setLocation(new X28LocationDto(null, "locationCity", "locationPostalCode", "CH"))
                .setOccupations(Collections.singletonList(new X28OccupationDto("avamOccupationCode", WorkExperience.MORE_THAN_1_YEAR, "educationCode")))
                .setProfessionCodes("professionCodes")
                .setLanguageSkills(Collections.singletonList(new X28LanguageSkillDto("de", LanguageLevel.PROFICIENT, LanguageLevel.INTERMEDIATE)))
                .setPublicationStartDate(LocalDate.of(2018, 1, 1))
                .setPublicationEndDate(LocalDate.of(2018, 12, 31))
                .setCompanyAnonymous(false);
    }

}
