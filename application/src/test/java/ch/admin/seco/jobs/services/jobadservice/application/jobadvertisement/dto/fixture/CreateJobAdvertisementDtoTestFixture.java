package ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.fixture;

import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.fixture.CompanyFixture.testCompany;

import java.time.LocalDate;
import java.util.Collections;

import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.ApplyChannelDto;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.CompanyDto;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.ContactDto;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.EmploymentDto;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.JobDescriptionDto;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.LanguageSkillDto;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.OccupationDto;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.PublicContactDto;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.PublicationDto;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.create.CreateJobAdvertisementDto;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.create.CreateLocationDto;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.Company;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.LanguageLevel;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.Salutation;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.WorkExperience;

public class CreateJobAdvertisementDtoTestFixture {

    public static CreateJobAdvertisementDto testCreateJobAdvertisementDto(Company company) {
        return new CreateJobAdvertisementDto(
                false,
                null,
                null,
                new ContactDto(Salutation.MR, "firstName", "lastName", "phone", "email", "de"),
                new PublicationDto(LocalDate.of(2018, 1, 1), LocalDate.of(2018, 3, 1), false, false, false, false, false),
                null,
                Collections.singletonList(new JobDescriptionDto("de", "title", "description")),
                CompanyDto.toDto(company),
                null,
                new EmploymentDto(LocalDate.of(2018, 1, 1), LocalDate.of(2018, 12, 31), false, false, false, 80, 100, null),
                new CreateLocationDto("remarks", "city", "postalCode", "CH"),
                new OccupationDto("avamCode", WorkExperience.MORE_THAN_1_YEAR, "educationCode"),
                Collections.singletonList(new LanguageSkillDto("de", LanguageLevel.PROFICIENT, LanguageLevel.PROFICIENT)),
                new ApplyChannelDto("mailAddress", "emailAddress", "phoneNumber", "formUrl", "additionalInfo"),
                new PublicContactDto(Salutation.MR, "firstName", "lastName", "phone", "email")
        );
    }

    public  static CreateJobAdvertisementDto testCreateJobAdvertisementDto() {
        return testCreateJobAdvertisementDto(testCompany().build());
    }
}
