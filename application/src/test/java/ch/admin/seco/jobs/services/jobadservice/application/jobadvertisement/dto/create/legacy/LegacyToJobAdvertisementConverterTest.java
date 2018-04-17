package ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.create.legacy;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.*;
import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.create.CreateLocationDto;
import org.junit.Test;

import ch.admin.seco.jobs.services.jobadservice.application.jobadvertisement.dto.create.CreateJobAdvertisementDto;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.LanguageLevel;
import ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement.Salutation;

public class LegacyToJobAdvertisementConverterTest {

	@Test
	public void shouldConvertLegacyDto() {
		// GIVEN
		LegacyLocationDto legacyLocationDto = new LegacyLocationDto("locationPostalCode", "locationCity", "locationCountryCode", "locationAdditionalDetails");
		LegacyLanguageSkillDto legacyLanguageSkillDto = new LegacyLanguageSkillDto("de", LegacyLanguageLevelEnum.BASIC_KNOWLEDGE, LegacyLanguageLevelEnum.BASIC_KNOWLEDGE);
		LegacyJobDto legacyJobDto = new LegacyJobDto(
				"title",
				"description",
				10,
				100,
				true,
				LocalDate.of(2018, 3, 1),
				true,
				LocalDate.of(2018, 4, 1),
				Collections.singletonList(legacyLanguageSkillDto),
				legacyLocationDto
		);
		LegacyCompanyDto legacyCompanyDto = new LegacyCompanyDto("companyName", "companyStreet", "companyHouseNumber", "companyPostalCode", "companyCity", new LegacyPostboxDto("postboxNumber", "postboxCity", "postboxPostalCode"), "companyCountryCode", "companyPhoneNumber", "companyEmail", "companyWebsite");
		LegacyContactDto legacyContactDto = new LegacyContactDto(LegacyTitleEnum.MISTER, "contactFirstName", "contactLastName", "contactPhoneNumber", "contactEmail");
		LegacyJobAdvertisementDto legacyJobAdvertisementDto = new LegacyJobAdvertisementDto(
				LocalDate.of(2018, 1, 1),
				LocalDate.of(2018, 2, 1),
				"reference",
				"url",
				"applicationUrl",
				legacyJobDto,
				legacyCompanyDto,
				legacyContactDto
		);

		// WHEN
		CreateJobAdvertisementDto convertedDto = LegacyToJobAdvertisementConverter.convert(legacyJobAdvertisementDto);

		// THEN
		assertThat(convertedDto).isNotNull();
		assertThat(convertedDto.isReportToAvam()).isFalse();
		assertThat(convertedDto.getExternalUrl()).isEqualTo("url");

		ContactDto contact = convertedDto.getContact();
		assertThat(contact).isNotNull();
		assertThat(contact.getSalutation()).isEqualTo(Salutation.MR);
		assertThat(contact.getFirstName()).isEqualTo("contactFirstName");
		assertThat(contact.getLastName()).isEqualTo("contactLastName");
		assertThat(contact.getPhone()).isEqualTo("contactPhoneNumber");
		assertThat(contact.getEmail()).isEqualTo("contactEmail");
		assertThat(contact.getLanguageIsoCode()).isEqualTo("de");

		PublicationDto publication = convertedDto.getPublication();
		assertThat(publication).isNotNull();
		assertThat(publication.getStartDate()).isEqualTo(LocalDate.of(2018, 1, 1));
		assertThat(publication.getEndDate()).isEqualTo(LocalDate.of(2018, 2, 1));
		assertThat(publication.isEuresDisplay()).isFalse();
		assertThat(publication.isEuresAnonymous()).isFalse();
		assertThat(publication.isPublicDisplay()).isTrue();
		assertThat(publication.isPublicAnonymous()).isFalse();
		assertThat(publication.isRestrictedDisplay()).isTrue();
		assertThat(publication.isRestrictedAnonymous()).isFalse();

		ApplyChannelDto applyChannel = convertedDto.getApplyChannel();
		assertThat(applyChannel).isNotNull();
		assertThat(applyChannel.getMailAddress()).isNull();
		assertThat(applyChannel.getEmailAddress()).isEqualTo("companyEmail");
		assertThat(applyChannel.getPhoneNumber()).isEqualTo("companyPhoneNumber");
		assertThat(applyChannel.getFormUrl()).isEqualTo("applicationUrl");
		assertThat(applyChannel.getAdditionalInfo()).isNull();

		List<JobDescriptionDto> jobDescriptions = convertedDto.getJobDescriptions();
		assertThat(jobDescriptions).isNotNull();
		assertThat(jobDescriptions).isNotEmpty();
		JobDescriptionDto jobDescription = jobDescriptions.get(0);
		assertThat(jobDescription).isNotNull();
		assertThat(jobDescription.getDescription()).isEqualTo("description");
		assertThat(jobDescription.getTitle()).isEqualTo("title");
		assertThat(jobDescription.getLanguageIsoCode()).isEqualTo("de");

		PublicContactDto publicContact = convertedDto.getPublicContact();
		assertThat(publicContact).isNotNull();
		assertThat(publicContact.getSalutation()).isEqualTo(Salutation.MR);
		assertThat(publicContact.getFirstName()).isEqualTo("contactFirstName");
		assertThat(publicContact.getLastName()).isEqualTo("contactLastName");
		assertThat(publicContact.getPhone()).isEqualTo("contactPhoneNumber");
		assertThat(publicContact.getEmail()).isEqualTo("contactEmail");

		CompanyDto companyDto = convertedDto.getCompany();
		assertThat(companyDto).isNotNull();
		assertThat(companyDto.getName()).isEqualTo("companyName");
		assertThat(companyDto.getStreet()).isEqualTo("companyStreet");
		assertThat(companyDto.getHouseNumber()).isEqualTo("companyHouseNumber");
		assertThat(companyDto.getPostalCode()).isEqualTo("companyPostalCode");
		assertThat(companyDto.getCity()).isEqualTo("companyCity");
		assertThat(companyDto.getPostOfficeBoxNumber()).isEqualTo("postboxNumber");
		assertThat(companyDto.getPostOfficeBoxCity()).isEqualTo("postboxCity");
		assertThat(companyDto.getPostOfficeBoxPostalCode()).isEqualTo("postboxPostalCode");
		assertThat(companyDto.getCountryIsoCode()).isEqualTo("companyCountryCode");
		assertThat(companyDto.getPhone()).isEqualTo("companyPhoneNumber");
		assertThat(companyDto.getEmail()).isEqualTo("companyEmail");
		assertThat(companyDto.getWebsite()).isEqualTo("companyWebsite");

		CreateLocationDto location = convertedDto.getLocation();
		assertThat(location).isNotNull();
		assertThat(location.getPostalCode()).isEqualTo("locationPostalCode");
		assertThat(location.getCity()).isEqualTo("locationCity");
		assertThat(location.getCountryIsoCode()).isEqualTo("locationCountryCode");
		assertThat(location.getRemarks()).isEqualTo("locationAdditionalDetails");

		EmploymentDto employment = convertedDto.getEmployment();
		assertThat(employment).isNotNull();
		assertThat(employment.getStartDate()).isEqualTo(LocalDate.of(2018, 3, 1));
		assertThat(employment.getEndDate()).isEqualTo(LocalDate.of(2018, 4, 1));
		assertThat(employment.isImmediately()).isTrue();
		assertThat(employment.isPermanent()).isTrue();
		assertThat(employment.getWorkloadPercentageMin()).isEqualTo(10);
		assertThat(employment.getWorkloadPercentageMax()).isEqualTo(100);

		List<LanguageSkillDto> languageSkills = convertedDto.getLanguageSkills();
		assertThat(languageSkills).isNotNull();
		assertThat(languageSkills).isNotEmpty();
		LanguageSkillDto languageSkill = languageSkills.get(0);
		assertThat(languageSkill).isNotNull();
		assertThat(languageSkill.getLanguageIsoCode()).isEqualTo("de");
		assertThat(languageSkill.getSpokenLevel()).isEqualTo(LanguageLevel.BASIC);
		assertThat(languageSkill.getWrittenLevel()).isEqualTo(LanguageLevel.BASIC);

		assertThat(convertedDto.getOccupation()).isNull();
	}
}