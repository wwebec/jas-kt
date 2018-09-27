package ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.fixture;

import static ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.fixture.X28CompanyDtoFixture.testX28CompanyDto;

import java.time.LocalDate;
import java.util.Collections;

import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.EmploymentDto;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.x28.X28CreateJobAdvertisementDto;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.x28.X28CompanyDto;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.x28.X28LanguageSkillDto;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.x28.X28LocationDto;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.x28.X28OccupationDto;
import ch.admin.seco.jobs.services.jobadservice.core.time.TimeMachine;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.LanguageLevel;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.WorkExperience;

public class CreateJobAdvertisementFromX28DtoTestFixture {

    public static X28CreateJobAdvertisementDto createCreateJobAdvertisementDto(X28CompanyDto x28CompanyDto) {
        return new X28CreateJobAdvertisementDto()
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
                .setEmployment(new EmploymentDto()
                        .setStartDate(LocalDate.of(2018, 1, 1))
                        .setEndDate(LocalDate.of(2018, 12, 31))
                        .setShortEmployment(false)
                        .setImmediately(false)
                        .setPermanent(false)
                        .setWorkloadPercentageMin(80)
                        .setWorkloadPercentageMax(100)
                        .setWorkForms(null))
                .setCompany(new X28CompanyDto("name", "street", "houseNumber", "postalCode", "city", "CH", null, null, null, "phone", "email", "website", false))
                .setLocation(new X28LocationDto(null, "city", "postalCode", null))
                .setOccupations(Collections.singletonList(new X28OccupationDto("avamCode", WorkExperience.MORE_THAN_1_YEAR, "educationCode")))
                .setProfessionCodes("1,2")
                .setLanguageSkills(Collections.singletonList(new X28LanguageSkillDto("de", LanguageLevel.PROFICIENT, LanguageLevel.PROFICIENT)))
                .setPublicationStartDate(TimeMachine.now().toLocalDate())
                .setPublicationEndDate(null)
                .setCompanyAnonymous(false);
    }

    public static X28CreateJobAdvertisementDto testCreateJobAdvertisementFromX28Dto() {
        return createCreateJobAdvertisementDto(testX28CompanyDto());
    }
}
