package ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.fixture;

import static ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.fixture.X28CompanyDtoTestFixture.testX28CompanyDto;
import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.LanguageLevel.PROFICIENT;
import static ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.WorkExperience.MORE_THAN_1_YEAR;
import static java.time.LocalDate.now;
import static java.util.Collections.singletonList;

import java.time.LocalDate;

import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.EmploymentDto;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.x28.CreateJobAdvertisementFromX28Dto;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.x28.X28CompanyDto;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.x28.X28LanguageSkillDto;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.x28.X28LocationDto;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.x28.X28OccupationDto;

public class CreateJobAdvertisementFromX28DtoTestFixture {

    public static CreateJobAdvertisementFromX28Dto createCreateJobAdvertisementDto(X28CompanyDto x28CompanyDto) {
        return new CreateJobAdvertisementFromX28Dto(
                null,
                null,
                "title",
                "description",
                null,
                "fingerprint",
                "url",
                null,
                null,
                new EmploymentDto(LocalDate.of(2018, 1, 1), LocalDate.of(2018, 12, 31), false, false, false, 80, 100, null),
                x28CompanyDto,
                new X28LocationDto("remarks", "city", "postalCode", "CH"),
                singletonList(new X28OccupationDto("avamCode", MORE_THAN_1_YEAR, "educationCode")),
                "1,2",
                singletonList(new X28LanguageSkillDto("de", PROFICIENT, PROFICIENT)),
                now(),
                null,
                false
        );
    }

    public static CreateJobAdvertisementFromX28Dto testCreateJobAdvertisementFromX28Dto() {
        return createCreateJobAdvertisementDto(testX28CompanyDto());
    }
}
