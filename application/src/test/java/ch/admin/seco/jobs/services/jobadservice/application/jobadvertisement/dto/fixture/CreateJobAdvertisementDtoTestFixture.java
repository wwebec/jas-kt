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
        return new CreateJobAdvertisementDto()
                .setReportToAvam(false)
                .setExternalUrl(null)
                .setExternalReference(null)
                .setContact(new ContactDto()
                        .setSalutation(Salutation.MR)
                        .setFirstName("firstName")
                        .setLastName("lastName")
                        .setPhone("phone")
                        .setEmail("email")
                        .setLanguageIsoCode("de")
                )
                .setPublication(new PublicationDto()
                        .setStartDate(LocalDate.of(2018, 1, 1))
                        .setEndDate(LocalDate.of(2018, 3, 1))
                        .setEuresDisplay(false)
                        .setEuresAnonymous(false)
                        .setPublicDisplay(false)
                        .setRestrictedDisplay(false)
                        .setCompanyAnonymous(false)
                )
                .setNumberOfJobs(null)
                .setJobDescriptions(Collections.singletonList(new JobDescriptionDto()
                        .setTitle("de")
                        .setTitle("title")
                        .setDescription("description")))
                .setCompany(CompanyDto.toDto(company))
                .setEmployer(null)
                .setEmployment(new EmploymentDto()
                        .setStartDate(LocalDate.of(2018, 1, 1))
                        .setEndDate(LocalDate.of(2018, 12, 31))
                        .setShortEmployment(false)
                        .setImmediately(false)
                        .setPermanent(false)
                        .setWorkloadPercentageMin(80)
                        .setWorkloadPercentageMax(100)
                        .setWorkForms(null))
                .setLocation(new CreateLocationDto()
                        .setRemarks("remarks")
                        .setCity("city")
                        .setPostalCode("postalCode")
                        .setCountryIsoCode("CH"))
                .setOccupation(
                        new OccupationDto()
                                .setAvamOccupationCode("avamCode")
                                .setWorkExperience(WorkExperience.MORE_THAN_1_YEAR)
                                .setEducationCode("educationCode"))
                .setLanguageSkills(Collections.singletonList(new LanguageSkillDto()
                        .setLanguageIsoCode("de")
                        .setSpokenLevel(LanguageLevel.PROFICIENT)
                        .setWrittenLevel(LanguageLevel.PROFICIENT)))
                .setApplyChannel(
                        new ApplyChannelDto()
                                .setMailAddress("mailAddress")
                                .setEmailAddress("emailAddress")
                                .setPhoneNumber("phoneNumber")
                                .setFormUrl("formUrl")
                                .setAdditionalInfo("additionalInfo"))
                .setPublicContact(
                        new PublicContactDto()
                                .setSalutation(Salutation.MR)
                                .setFirstName("firstName")
                                .setLastName("lastName")
                                .setPhone("phone")
                                .setEmail("email"));
    }

    public static CreateJobAdvertisementDto testCreateJobAdvertisementDto() {
        return testCreateJobAdvertisementDto(testCompany().build());
    }
}
