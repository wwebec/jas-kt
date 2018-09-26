package ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.fixture;

import static ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.fixture.X28CompanyDtoFixture.testX28CompanyDto;

import java.time.LocalDate;
import java.util.Collections;

import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.EmploymentDto;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.x28.CreateJobAdvertisementFromX28Dto;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.x28.X28CompanyDto;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.x28.X28LanguageSkillDto;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.x28.X28LocationDto;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.x28.X28OccupationDto;
import ch.admin.seco.jobs.services.jobadservice.core.time.TimeMachine;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.LanguageLevel;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.WorkExperience;

public class CreateJobAdvertisementFromX28DtoTestFixture {

    public static CreateJobAdvertisementFromX28Dto createCreateJobAdvertisementDto(X28CompanyDto x28CompanyDto) {
        return new CreateJobAdvertisementFromX28Dto()
                .setStellennummerEgov(null)
                .setStellennummerAvam(null)
                .setTitle("title")
                .setDescription("description")
                .setNumberOfJobs(null)
                .setFingerprint("fingerprint")
                .setExternalUrl("url")
                .setJobCenterCode(null)
                .setCompany(x28CompanyDto)
                .setContact(null)
                .setEmployment(new EmploymentDto(LocalDate.of(2018, 1, 1), LocalDate.of(2018, 12, 31), false, false, false, 80, 100, null))
                .setCompany(new X28CompanyDto("name", "street", "houseNumber", "postalCode", "city", "CH", null, null, null, "phone", "email", "website", false))
                .setLocation(new X28LocationDto(null, "city", "postalCode", null))
                .setOccupations(Collections.singletonList(new X28OccupationDto("avamCode", WorkExperience.MORE_THAN_1_YEAR, "educationCode")))
                .setProfessionCodes("1,2")
                .setLanguageSkills(Collections.singletonList(new X28LanguageSkillDto("de", LanguageLevel.PROFICIENT, LanguageLevel.PROFICIENT)))
                .setPublicationStartDate(TimeMachine.now().toLocalDate())
                .setPublicationEndDate(null)
                .setCompanyAnonymous(false);
    }

    public static CreateJobAdvertisementFromX28Dto testCreateJobAdvertisementFromX28Dto() {
        return createCreateJobAdvertisementDto(testX28CompanyDto());
    }
}
