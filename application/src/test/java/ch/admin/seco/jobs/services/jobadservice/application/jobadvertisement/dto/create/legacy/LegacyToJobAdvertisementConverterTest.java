package ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.create.legacy;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Locale;

import org.junit.Before;
import org.junit.Test;

import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.create.CreateJobAdvertisementDto;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.LanguageLevel;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.Salutation;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.WorkExperience;

public class LegacyToJobAdvertisementConverterTest {

	private LegacyToJobAdvertisementConverter testedObject;

	@Before
	public void setUp() {
		testedObject = new LegacyToJobAdvertisementConverter();
	}

	@Test
	public void shouldConvertLegacyDto() {
		// GIVEN
		LegacyLocationDto legacyLocationDto = new LegacyLocationDto("CH", "1234", "2222", "Bern", "details");
		LegacyLanguageSkillDto legacyLanguageSkillDto = new LegacyLanguageSkillDto(LegacyLanguageEnum.DE, LegacyLanguageLevelEnum.BASIC, LegacyLanguageLevelEnum.BASIC);
		LegacyJobDto legacyJobDto = new LegacyJobDto(
				"title",
				new LegacyOccupationDto("avam", LegacyDegreeEnum.PRIMAR_OBLIGATORISCHE_SCHULE, LegacyExperienceEnum.BETWEEN_1_AND_3_YEARS),
				"description",
				10,
				100,
				true,
				LocalDate.of(2018, 4, 1),
				true,
				LocalDate.of(2018, 5, 1),
				LegacyDrivingLicenseLevelEnum.A,
				Collections.singletonList(legacyLanguageSkillDto),
				legacyLocationDto
		);
		LegacyCompanyDto legacyCompanyDto = new LegacyCompanyDto("company", "street", "12", "1234", "Bern", "11", "12", "Bern", "CH");
		LegacyContactDto legacyContactDto = new LegacyContactDto(Salutation.MR, "firstName", "lastName", "+41234512346", "email@email.com");
		LegacyApplicationDto legacyApplicationDto = new LegacyApplicationDto(true, true, "email@email.com", "http://asdf.com", true, "+41234512346", "info");
		LegacyPublicationDto legacyPublicationDto = new LegacyPublicationDto(true, true);
		LegacyJobAdvertisementDto legacyJobAdvertisementDto = new LegacyJobAdvertisementDto(
				legacyJobDto,
				legacyCompanyDto,
				legacyContactDto,
				legacyApplicationDto,
				legacyPublicationDto,
				Locale.GERMAN);

		// WHEN
		CreateJobAdvertisementDto convertedDto = testedObject.convert(legacyJobAdvertisementDto);

		// THEN
		assertThat(convertedDto).isNotNull();
		assertThat(convertedDto.getContact().getSalutation()).isEqualTo(Salutation.MR);
		assertThat(convertedDto.getContact().getFirstName()).isEqualTo("firstName");
		assertThat(convertedDto.getContact().getLastName()).isEqualTo("lastName");
		assertThat(convertedDto.getContact().getPhone()).isEqualTo("+41234512346");
		assertThat(convertedDto.getContact().getEmail()).isEqualTo("email@email.com");
		assertThat(convertedDto.getContact().getLanguageIsoCode()).isEqualTo("de");
		assertThat(convertedDto.getPublicContact().getSalutation()).isEqualTo(Salutation.MR);
		assertThat(convertedDto.getPublicContact().getFirstName()).isEqualTo("firstName");
		assertThat(convertedDto.getPublicContact().getLastName()).isEqualTo("lastName");
		assertThat(convertedDto.getPublicContact().getPhone()).isEqualTo("+41234512346");
		assertThat(convertedDto.getPublicContact().getEmail()).isEqualTo("email@email.com");
		assertThat(convertedDto.getJobDescriptions().get(0).getDescription()).isEqualTo("description");
		assertThat(convertedDto.getJobDescriptions().get(0).getTitle()).isEqualTo("title");
		assertThat(convertedDto.getJobDescriptions().get(0).getLanguageIsoCode()).isEqualTo("de");
		assertThat(convertedDto.getCompany().getName()).isEqualTo("company");
		assertThat(convertedDto.getCompany().getCity()).isEqualTo("Bern");
		assertThat(convertedDto.getCompany().getCountryIsoCode()).isEqualTo("CH");
		assertThat(convertedDto.getCompany().getHouseNumber()).isEqualTo("12");
		assertThat(convertedDto.getCompany().getStreet()).isEqualTo("street");
		assertThat(convertedDto.getCompany().getPostalCode()).isEqualTo("1234");
		assertThat(convertedDto.getCompany().getPostOfficeBoxCity()).isEqualTo("Bern");
		assertThat(convertedDto.getCompany().getPostOfficeBoxNumber()).isEqualTo("11");
		assertThat(convertedDto.getCompany().getPostOfficeBoxPostalCode()).isEqualTo("12");
		assertThat(convertedDto.getLocation().getCity()).isEqualTo("Bern");
		assertThat(convertedDto.getLocation().getRemarks()).isEqualTo("details");
		assertThat(convertedDto.getLocation().getCommunalCode()).isEqualTo("2222");
		assertThat(convertedDto.getLocation().getPostalCode()).isEqualTo("1234");
		assertThat(convertedDto.getLocation().getCountryIsoCode()).isEqualTo("CH");
		assertThat(convertedDto.getLocation().getCountryIsoCode()).isEqualTo("CH");
		assertThat(convertedDto.getEmployment().getStartDate()).isEqualTo(LocalDate.of(2018, 4, 1));
		assertThat(convertedDto.getEmployment().getEndDate()).isEqualTo(LocalDate.of(2018, 5, 1));
		assertThat(convertedDto.getEmployment().getImmediately()).isTrue();
		assertThat(convertedDto.getEmployment().getPermanent()).isTrue();
		assertThat(convertedDto.getEmployment().getWorkloadPercentageMin()).isEqualTo(10);
		assertThat(convertedDto.getEmployment().getWorkloadPercentageMax()).isEqualTo(100);
		assertThat(convertedDto.getOccupation().getAvamOccupationCode()).isEqualTo("avam");
		assertThat(convertedDto.getOccupation().getEducationCode()).isEqualTo("PRIMAR_OBLIGATORISCHE_SCHULE");
		assertThat(convertedDto.getOccupation().getWorkExperience()).isEqualTo(WorkExperience.MORE_THAN_1_YEAR);
		assertThat(convertedDto.getLanguageSkills().get(0).getLanguageIsoCode()).isEqualTo("DE");
		assertThat(convertedDto.getLanguageSkills().get(0).getSpokenLevel()).isEqualTo(LanguageLevel.BASIC);
		assertThat(convertedDto.getLanguageSkills().get(0).getWrittenLevel()).isEqualTo(LanguageLevel.BASIC);
	}
}