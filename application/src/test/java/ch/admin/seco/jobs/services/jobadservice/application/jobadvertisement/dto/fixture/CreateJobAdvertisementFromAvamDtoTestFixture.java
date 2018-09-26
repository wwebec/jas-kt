package ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.fixture;

import static ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.JobAdvertisementApplicationServiceForAvamTest.STELLENNUMMER_AVAM;
import static ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.fixture.PublicationDtoTestFixture.testPublicationDto;
import static ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.fixture.PublicationDtoTestFixture.testPublicationDtoWithCompanyAnonymous;
import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.LanguageLevel.PROFICIENT;
import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.WorkExperience.MORE_THAN_1_YEAR;
import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.fixture.CompanyFixture.testCompany;
import static java.time.LocalDate.now;
import static java.util.Arrays.asList;

import java.time.LocalDate;

import org.assertj.core.util.Sets;

import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.ApplyChannelDto;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.CompanyDto;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.ContactDto;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.EmploymentDto;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.LanguageSkillDto;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.OccupationDto;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.PublicationDto;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.create.CreateJobAdvertisementFromAvamDto;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.create.CreateLocationDto;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.Company;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.Salutation;

public class CreateJobAdvertisementFromAvamDtoTestFixture {

    public static CreateJobAdvertisementFromAvamDto testCreateJobAdvertisementDto() {
        return testCreateJobAdvertisementDto(testCompany().build(), testPublicationDto());
    }

    public static CreateJobAdvertisementFromAvamDto testCreateJobAdvertisementDtoWithCompanyAnonymous() {
        return testCreateJobAdvertisementDto(testCompany().build(), testPublicationDtoWithCompanyAnonymous());
    }

    private static CreateJobAdvertisementFromAvamDto testCreateJobAdvertisementDto(Company company, PublicationDto publicationDto) {
        return new CreateJobAdvertisementFromAvamDto(
                STELLENNUMMER_AVAM,
                "title",
                "description",
                "de",
                null,
                true,
                LocalDate.of(2018, 1, 1),
                "jobCenter",
                now(),
                new EmploymentDto(LocalDate.of(2018, 1, 1), LocalDate.of(2018, 12, 31), false, false, false, 80, 100, Sets.newHashSet()),
                new ApplyChannelDto("mailAddress", "emailAddress", "phoneNumber", "formUrl", "additionalInfo"),
                CompanyDto.toDto(company),
                new ContactDto(Salutation.MR, "firstName", "lastName", "phone", "email", "de"),
                new CreateLocationDto("remarks", "city", "postalCode", "CH"),
                asList(new OccupationDto("avamCode", MORE_THAN_1_YEAR, "educationCode")),
                asList(new LanguageSkillDto("de", PROFICIENT, PROFICIENT)),
                publicationDto
        );
    }


}
